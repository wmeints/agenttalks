param name string
param location string = resourceGroup().location
param tags object = {}
param identityName string = 'id-podcast-api'
param imageName string = ''
param containerRegistryName string
param containerRegistryResourceGroupName string
param containerAppsEnvironmentName string
param serviceName string = 'podcast-api'
param databaseServerDomainName string
param databaseServerAdminUsername string
@secure()
param databaseServerAdminPassword string

var databaseUrl = 'jdbc:postgresql://${databaseServerDomainName}:5432/podcasts?sslmode=require'

resource containerAppsEnvironment 'Microsoft.App/managedEnvironments@2025-02-02-preview' existing = {
  name: containerAppsEnvironmentName
}

resource applicationIdentity 'Microsoft.ManagedIdentity/userAssignedIdentities@2025-01-31-preview' = {
  name: identityName
  location: location
}

module pullRoleAssignment '../core/security/registry-access.bicep' = {
  name: 'pull-role-assignment'
  scope: resourceGroup(containerRegistryResourceGroupName)
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    principalId: applicationIdentity.properties.principalId
  }
}

resource applicationService 'Microsoft.App/containerApps@2025-01-01' = {
  name: name
  location: location
  tags: union(tags, { 'azd-service-name': serviceName })
  dependsOn: [pullRoleAssignment]
  identity: {
    type: 'UserAssigned'
    userAssignedIdentities: {
      '${applicationIdentity.id}': {}
    }
  }
  properties: {
    managedEnvironmentId: containerAppsEnvironment.id
    configuration: {
      registries: [
        {
          server: '${containerRegistryName}.azurecr.io'
          identity: applicationIdentity.id
        }
      ]
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
          image: !empty(imageName) ? imageName : 'mcr.microsoft.com/azuredocs/containerapps-helloworld:latest'
          resources: {
            cpu: 1
            memory: '2Gi'
          }
          env: [
            {
              name: 'QUARKUS_DATASOURCE_JDBC_URL'
              value: databaseUrl
            }
            {
              name: 'QUARKUS_DATASOURCE_USERNAME'
              value: databaseServerAdminUsername
            }
            {
              name: 'QUARKUS_DATASOURCE_PASSWORD'
              secretRef: 'database-password'
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
