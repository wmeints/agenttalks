param name string
param location string = resourceGroup().location
param tags object = {}

resource cognitiveServicesAccount 'Microsoft.CognitiveServices/accounts@2025-04-01-preview' = {
  name: name
  location: location
  kind: 'OpenAI'
  sku: {
    name: 'S0'
  }
  tags: tags
  properties: {
    publicNetworkAccess: 'Enabled'
    apiProperties: {}
    allowProjectManagement: false
  }

  resource chatCompletionModel 'deployments' = {
    name: 'chatcompletions'
    sku: {
      name: 'GlobalStandard'
      capacity: 250
    }
    properties: {
      model: {
        format: 'OpenAI'
        name: 'gpt-4.1'
        version: '2025-04-14'
      }
    }
  }
}

output accountUrl string = cognitiveServicesAccount.properties.endpoint
output id string = cognitiveServicesAccount.id
output name string = cognitiveServicesAccount.name
