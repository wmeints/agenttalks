metadata description = 'Deploy a log analytics workspace'

param location string = resourceGroup().location
param name string
param tags object = {}

resource logAnalytics 'Microsoft.OperationalInsights/workspaces@2025-02-01' = {
  name: name
  location: location
  tags: tags
  properties: {
    retentionInDays: 30
    sku: { 
      name: 'PerGB2018' 
    }
  }
}

output id string = logAnalytics.id
output name string = logAnalytics.name
output customerId string = logAnalytics.properties.customerId
