metadata description = 'Deploys monitoring components'

param logAnalyticsWorkspaceName string
param applicationInsightsName string
param location string = resourceGroup().location
param tags object = {}

module logAnalytics './loganalytics.bicep' = {
  name: 'logAnalytics'
  params: {
    name: logAnalyticsWorkspaceName
    location: location
    tags: tags
  }
}

module applicationInsights './applicationinsights.bicep' = {
  name: 'applicationInsights'
  params: {
    name: applicationInsightsName
    location: location
    tags: tags
    logAnalyticsWorkspaceId: logAnalytics.outputs.id
  }
}

output applicationInsightsId string = applicationInsights.outputs.id
output applicationInsightsName string = applicationInsights.outputs.name
output applicationInsightsConnectionString string = applicationInsights.outputs.connectionString
output applicationInsightsInstrumentationKey string = applicationInsights.outputs.instrumentationKey
output logAnalyticsWorkspaceId string = logAnalytics.outputs.id
output logAnalyticsWorkspaceName string = logAnalytics.outputs.name
output logAnalyticsWorkspaceCustomerId string = logAnalytics.outputs.customerId
