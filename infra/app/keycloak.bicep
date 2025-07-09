param name string
param location string = resourceGroup().location
param tags object = {}
param containerAppsEnvironmentName string
param databaseServerDomainName string
param databaseServerAdminUsername string
@secure()
param databaseServerAdminPassword string
@secure()
param keycloakAdminPassword string
param keycloakAdminUsername string = 'admin'

var databaseUrl = 'jdbc:postgresql://${databaseServerDomainName}:5432/keycloak?sslmode=require'

resource containerAppsEnvironment 'Microsoft.App/managedEnvironments@2025-02-02-preview' existing = {
  name: containerAppsEnvironmentName
}

resource keycloakApp 'Microsoft.App/containerApps@2025-01-01' = {
  name: name
  location: location
  tags: union(tags, { 'service-name': 'keycloak' })
  properties: {
    managedEnvironmentId: containerAppsEnvironment.id
    configuration: {
      secrets: [
        {
          name: 'database-password'
          value: databaseServerAdminPassword
        }
        {
          name: 'keycloak-admin-password'
          value: keycloakAdminPassword
        }
      ]
      ingress: {
        external: true
        targetPort: 8080
        allowInsecure: false
        traffic: [
          {
            weight: 100
            latestRevision: true
          }
        ]
      }
    }
    template: {
      containers: [
        {
          name: 'keycloak'
          image: 'quay.io/keycloak/keycloak:26.2.4'
          resources: {
            cpu: 1
            memory: '2Gi'
          }
          args: [
            'start'
          ]
          env: [
            {
              name: 'KC_HOSTNAME_STRICT'
              value: 'false'
            }
            {
              name: 'KC_HOSTNAME_STRICT_HTTPS'
              value: 'false'
            }
            {
              name: 'KC_HTTP_ENABLED'
              value: 'true'
            }
            {
              name: 'KC_PROXY'
              value: 'edge'
            }
            {
              name: 'KC_DB'
              value: 'postgres'
            }
            {
              name: 'KC_DB_URL'
              value: databaseUrl
            }
            {
              name: 'KC_DB_USERNAME'
              value: databaseServerAdminUsername
            }
            {
              name: 'KC_DB_PASSWORD'
              secretRef: 'database-password'
            }
            {
              name: 'KEYCLOAK_ADMIN'
              value: keycloakAdminUsername
            }
            {
              name: 'KEYCLOAK_ADMIN_PASSWORD'
              secretRef: 'keycloak-admin-password'
            }
            {
              name: 'KC_HEALTH_ENABLED'
              value: 'true'
            }
            {
              name: 'KC_METRICS_ENABLED'
              value: 'true'
            }
          ]
          probes: [
            {
              type: 'Liveness'
              httpGet: {
                path: '/health/live'
                port: 8080
                scheme: 'HTTP'
              }
              initialDelaySeconds: 60
              periodSeconds: 30
              timeoutSeconds: 10
              failureThreshold: 3
            }
            {
              type: 'Readiness'
              httpGet: {
                path: '/health/ready'
                port: 8080
                scheme: 'HTTP'
              }
              initialDelaySeconds: 30
              periodSeconds: 10
              timeoutSeconds: 5
              failureThreshold: 3
            }
          ]
        }
      ]
      scale: {
        minReplicas: 1
        maxReplicas: 2
      }
    }
  }
}

output name string = keycloakApp.name
output fqdn string = keycloakApp.properties.configuration.ingress.fqdn
output url string = 'https://${keycloakApp.properties.configuration.ingress.fqdn}'
