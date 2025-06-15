param name string
param location string = resourceGroup().location
param tags object = {}
param containerAppsEnvironmentName string
param databaseServerDomainName string
param databaseServerAdminUsername string
@secure()
param databaseServerAdminPassword string

resource containerAppsEnvironment 'Microsoft.App/managedEnvironments@2025-02-02-preview' existing = {
  name: containerAppsEnvironmentName
}

resource applicationIdentity 'Microsoft.ManagedIdentity/userAssignedIdentities@2025-01-31-preview' = {
  name: 'id-temporal-app'
  location: location
}

resource applicationService 'Microsoft.App/containerApps@2025-01-01' = {
  name: name
  location: location
  tags: tags
  identity: {
    type: 'UserAssigned'
    userAssignedIdentities: {
      '${applicationIdentity.id}': {}
    }
  }
  properties: {
    managedEnvironmentId: containerAppsEnvironment.id
    configuration: {
      secrets: [
        {
          name: 'database-password'
          value: databaseServerAdminPassword
        }
      ]
    }
    template: {
      containers: [
        {
          name: 'app'
          image: 'temporalio/auto-setup:1.27.2.0'
          resources: {
            cpu: 1
            memory: '2Gi'
          }
          env: [
            {
              name: 'POSTGRES_USER'
              value: databaseServerAdminUsername
            }
            {
              name: 'POSTGRES_PWD'
              secretRef: 'database-password'
            }
            {
              name: 'DB_PORT'
              value: '5432'
            }
            {
              name: 'DB'
              value: 'postgres12'
            }
            {
              name: 'POSTGRES_SEEDS'
              value: databaseServerDomainName
            }
            {
              name: 'SQL_HOST_NAME'
              value: databaseServerDomainName
            }
            {
              name: 'POSTGRES_TLS_ENABLED'
              value: 'true'
            }
            {
              name: 'TEMPORAL_ADDRESS'
              value: 'temporal-app:7233'
            }
            {
              name: 'TEMPORAL_CLI_ADDRESS'
              value: 'temporal-app:7233'
            }
            {
              name: 'BIND_ON_IP'
              value: '0.0.0.0'
            }
          ]
        }
      ]
      scale: {
        minReplicas: 1
        maxReplicas: 1
      }
    }
  }
}
