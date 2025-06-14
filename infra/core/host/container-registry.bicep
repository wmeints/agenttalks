metadata description = 'Deploys a container registry'


param name string
param location string = resourceGroup().location
param tags object = { }

resource containerRegistry 'Microsoft.ContainerRegistry/registries@2025-04-01' = {
  name: name
  location: location
  tags: tags
  sku: {
    name: 'Basic'
  }
  properties: {
    adminUserEnabled: false
    publicNetworkAccess: 'Enabled'
    networkRuleBypassOptions: 'AzureServices'
  }
}

output id string = containerRegistry.id
output loginServer string = containerRegistry.properties.loginServer
output name string = containerRegistry.name
