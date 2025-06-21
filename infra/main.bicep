param environmentName string
param location string
param applicationInsightsName string
param containerAppsEnvironmentName string
param logAnalyticsWorkspaceName string
param azureOpenAIAccountName string
param containerRegistryName string
param containerRegistryResourceGroupName string
param databaseServerName string
param databaseServerAdminLogin string
@secure()
param databaseServerAdminPassword string
param storageAccountName string
param contentApiImageName string
param readerApiImageName string
param podcastApiImageName string
param dashboardImageName string
@secure()
param gmailServiceAccountCredentials string

var tags = {
  'env-name': environmentName
}

module monitor './core/monitor/monitoring.bicep' = {
  name: 'monitoring'
  params: {
    applicationInsightsName: applicationInsightsName
    logAnalyticsWorkspaceName: logAnalyticsWorkspaceName
    location: location
    tags: tags
  }
}

module containerApps './core/host/container-apps.bicep' = {
  name: 'container-apps'
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
  params: {
    name: databaseServerName
    administratorLogin: databaseServerAdminLogin
    administratorPassword: databaseServerAdminPassword
  }
}

module openaiAccount './core/openai/openai.bicep' = {
  name: 'openai-account'
  params: {
    name: azureOpenAIAccountName
    location: location
  }
}

module storageAccount './core/storage/storage.bicep' = {
  name: 'storage-account'
  params: {
    name: storageAccountName
    location: location
    tags: tags
  }
}

module contentApi './app/content-api.bicep' = {
  name: 'content-api'
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    databaseServerAdminPassword: databaseServerAdminPassword
    databaseServerAdminUsername: databaseServerAdminLogin
    databaseServerDomainName: databaseServer.outputs.domainName
    gmailServiceAccountCredentials: gmailServiceAccountCredentials
    serviceName: 'content-api'
    name: 'content-api'
    location: location
    tags: tags
    imageName: contentApiImageName
  }
}

module readerApi './app/reader-api.bicep' = {
  name: 'reader-api'
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    azureOpenAIAccountName: azureOpenAIAccountName
    serviceName: 'reader-api'
    name: 'reader-api'
    location: location
    tags: tags
    imageName: readerApiImageName
  }
}

module podcastApi './app/podcast-api.bicep' = {
  name: 'podcast-api'
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    azureOpenAIAccountName: azureOpenAIAccountName
    databaseServerAdminPassword: databaseServerAdminPassword
    databaseServerAdminUsername: databaseServerAdminLogin
    databaseServerDomainName: databaseServer.outputs.domainName
    storageAccountName: storageAccount.outputs.name
    serviceName: 'podcast-api'
    name: 'podcast-api'
    location: location
    tags: tags
    imageName: podcastApiImageName
  }
}

module temporalApp './app/temporal.bicep' = {
  name: 'temporal-app'
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
  params: {
    containerAppsEnvironmentName: containerAppsEnvironmentName
    location: location
    tags: tags
  }
}

module dashboardApp './app/dashboard.bicep' = {
  name: 'dashboard-app'
  params: {
    containerRegistryName: containerRegistryName
    containerRegistryResourceGroupName: containerRegistryResourceGroupName
    containerAppsEnvironmentName: containerAppsEnvironmentName
    imageName: dashboardImageName
    serviceName: 'dashboard'
    name: 'dashboard'
    location: location
    tags: tags
  }
}
