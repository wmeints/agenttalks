param location string = resourceGroup().location
param tags object = {}
param containerAppsEnvironmentName string

resource containerAppsEnvironment 'Microsoft.App/managedEnvironments@2025-02-02-preview' existing = {
  name: containerAppsEnvironmentName
}

resource applicationIdentity 'Microsoft.ManagedIdentity/userAssignedIdentities@2025-01-31-preview' = {
  name: 'id-rabbitmq-app'
  location: location
}

resource applicationService 'Microsoft.App/containerApps@2025-01-01' = {
  name: 'rabbitmq-app'
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
      ingress: {
        external: false
        targetPort: 5672
        transport: 'tcp'
        exposedPort: 5672
        additionalPortMappings: [
          {
            external: false
            exposedPort: 15672
            targetPort: 15672
          }
        ]
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
          name: 'app'
          image: 'rabbitmq:4.1.1-management'
          resources: {
            cpu: 1
            memory: '2Gi'
          }
        }
      ]
      scale: {
        minReplicas: 1
        maxReplicas: 1
      }
    }
  }
}

output fqdn string = applicationService.properties.configuration.ingress.fqdn
