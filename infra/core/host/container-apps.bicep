metadata description = 'Creates the hosting environment for the container apps.'

param name string
param location string = resourceGroup().location
param tags object = {}

param containerAppsEnvironmentName string
param containerRegistryName string
param containerRegistryResourceGroupName string
param logAnalyticsWorkspaceName string
param applicationInsightsName string
param logAnalyticsWorkspaceCustomerId string

module containerAppsEnvironment 'container-apps-environment.bicep' = {
  name: '${name}-container-apps-environment'
  params: {
    name: containerAppsEnvironmentName
    location: location
    tags: tags
    logAnalyticsWorkspaceName: logAnalyticsWorkspaceName
    logAnalyticsWorkspaceCustomerId: logAnalyticsWorkspaceCustomerId
    applicationInsightsName: applicationInsightsName
  }
}

module containerRegistry 'container-registry.bicep' = {
    name: '${name}-container-registry'
    scope: resourceGroup(containerRegistryResourceGroupName)
    params: {
        name: containerRegistryName
        location: location
        tags: tags
    }
}

output containerRegistryEndpoint string = containerRegistry.outputs.loginServer
