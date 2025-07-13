param name string
param location string = resourceGroup().location
param tags object = {}
param imageName string = ''
param containerRegistryName string
param containerRegistryResourceGroupName string
param containerAppsEnvironmentName string
param serviceName string = 'reader-api'
param azureOpenAIAccountName string
@secure()
param applicationInsightsConnectionString string
param eventBusServiceUrl string

resource containerAppsEnvironment 'Microsoft.App/managedEnvironments@2025-02-02-preview' existing = {
  name: containerAppsEnvironmentName
}

resource applicationIdentity 'Microsoft.ManagedIdentity/userAssignedIdentities@2025-01-31-preview' = {
  name: 'id-reader-api'
  location: location
}

resource cognitiveServicesAccount 'Microsoft.CognitiveServices/accounts@2025-04-01-preview' existing = {
  name: azureOpenAIAccountName
}

module pullRoleAssignment '../core/security/registry-access.bicep' = {
  name: 'pull-role-assignment'
  scope: resourceGroup(containerRegistryResourceGroupName)
  params: {
    containerRegistryName: containerRegistryName
    principalId: applicationIdentity.properties.principalId
  }
}

resource applicationService 'Microsoft.App/containerApps@2025-01-01' = {
  name: name
  location: location
  tags: union(tags, { 'service-name': serviceName })
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
          name: 'openai-api-key'
          value: cognitiveServicesAccount.listKeys().key1
        }
      ]
    }
    template: {
      containers: [
        {
          name: 'app'
          image: imageName
          resources: {
            cpu: 1
            memory: '2Gi'
          }
          env: [
            {
              name: 'RABBITMQ_HOST'
              value: eventBusServiceUrl
            }
            {
              name: 'QUARKUS_LANGCHAIN4J_AZURE_OPENAI_ENDPOINT'
              value: 'https://${cognitiveServicesAccount.properties.endpoint}/openai/deployments/chatcompletions'
            }
            {
              name: 'QUARKUS_LANGCHAIN4J_AZURE_OPENAI_API_KEY'
              secretRef: 'openai-api-key'
            }
            {
              name: 'APPLICATIONINSIGHTS_CONNECTION_STRING'
              value: applicationInsightsConnectionString
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
