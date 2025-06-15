metadata description = 'Deploys an azure container apps environment.'

param name string
param location string = resourceGroup().location
param tags object = { }

param applicationInsightsName string
param logAnalyticsWorkspaceName string
param logAnalyticsWorkspaceCustomerId string

resource containerAppsEnvironment 'Microsoft.App/managedEnvironments@2025-02-02-preview' = {
  name: name
  location: location
  tags: tags
  properties: {
    appLogsConfiguration: {
      destination: 'log-analytics'
      logAnalyticsConfiguration: {
        customerId: logAnalyticsWorkspaceCustomerId
        sharedKey: logAnalyticsWorkspace.listKeys().primarySharedKey
      }
    }
    appInsightsConfiguration: {
      connectionString:appInsights.properties.ConnectionString
    }
    openTelemetryConfiguration: {
      tracesConfiguration: {
        destinations: ['appInsights']
      }
      logsConfiguration: {
        destinations: ['appInsights']
      }
    }
  }
}

resource logAnalyticsWorkspace 'Microsoft.OperationalInsights/workspaces@2025-02-01' existing =  {
  name: logAnalyticsWorkspaceName
}

resource appInsights 'Microsoft.Insights/components@2020-02-02' existing = {
  name: applicationInsightsName
}

output defaultDomain string = containerAppsEnvironment.properties.defaultDomain
output id string = containerAppsEnvironment.id
output name string = containerAppsEnvironment.name
