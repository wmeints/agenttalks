targetScope = 'subscription'

param environmentName string
param location string
param resourceGroupName string
param applicationInsightsName string
param containerAppsEnvironmentName string
param logAnalyticsWorkspaceName string
param containerRegistryName string
param containerRegistryResourceGroupName string
param databaseServerName string
param databaseServerAdminLogin string
@secure()
param databaseServerAdminPassword string

var tags = {
  'azd-env-name': environmentName
}

resource rg 'Microsoft.Resources/resourceGroups@2025-04-01' existing = {
  name: resourceGroupName
}

module monitor './core/monitor/monitoring.bicep' = {
  name: 'monitoring'
  scope: rg
  params: {
    applicationInsightsName: applicationInsightsName
    logAnalyticsWorkspaceName: logAnalyticsWorkspaceName
    location: location
    tags: tags
  }
}

module containerApps './core/host/container-apps.bicep' = {
  name: 'container-apps'
  scope: rg
  params: {
    applicationInsightsName: monitor.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    logAnalyticsWorkspaceName: monitor.outputs.logAnalyticsWorkspaceName
    logAnalyticsWorkspaceCustomerId: monitor.outputs.logAnalyticsWorkspaceCustomerId
    name: 'app'
    location: location
    tags: tags
  }
}

module databaseServer 'core/database/postgres.bicep' = {
  name: 'database-server'
  scope: rg
  params: {
    name: databaseServerName
    administratorLogin: databaseServerAdminLogin
    administratorPassword: databaseServerAdminPassword
  }
}

module contentApi './app/content-api.bicep' = {
  name: 'content-api'
  scope: rg
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    databaseServerAdminPassword: databaseServerAdminPassword
    databaseServerAdminUsername: databaseServerAdminLogin
    databaseServerDomainName: databaseServer.outputs.domainName
    serviceName: 'content-api'
    name: 'content-api'
    location: location
    tags: tags
  }
}

module readerApi './app/reader-api.bicep' = {
  name: 'reader-api'
  scope: rg
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    serviceName: 'reader-api'
    name: 'reader-api'
    location: location
    tags: tags
  }
}

module podcastApi './app/podcast-api.bicep' = {
  name: 'podcast-api'
  scope: rg
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    databaseServerAdminPassword: databaseServerAdminPassword
    databaseServerAdminUsername: databaseServerAdminLogin
    databaseServerDomainName: databaseServer.outputs.domainName
    serviceName: 'podcast-api'
    name: 'podcast-api'
    location: location
    tags: tags
  }
}

module temporalApp './app/temporal.bicep' = {
  name: 'temporal-app'
  scope: rg
  params: {
    containerAppsEnvironmentName: containerAppsEnvironmentName
    databaseServerAdminPassword: databaseServerAdminPassword
    databaseServerAdminUsername: databaseServerAdminLogin
    databaseServerDomainName: databaseServer.outputs.domainName
    name: 'temporal-app'
    location: location
    tags: tags
  }
}

module rabbitmqApp './app/rabbitmq.bicep' = {
  name: 'rabbitmq-app'
  scope: rg
  params: {
    containerAppsEnvironmentName: containerAppsEnvironmentName
    location: location
    tags: tags
  }
}


output AZURE_CONTAINER_REGISTRY_ENDPOINT string = containerApps.outputs.containerRegistryEndpoint
