using './main.bicep'

var imageHash = readEnvironmentVariable('GIT_HASH', 'latest')

param environmentName = readEnvironmentVariable('AZURE_ENV_NAME', 'dev')
param location = readEnvironmentVariable('AZURE_LOCATION', 'swedencentral')
param containerRegistryName = 'acragenttalks'
param containerRegistryResourceGroupName = 'rg-agenttalks-shared'
param applicationInsightsName = 'ai-agenttalks-${environmentName}'
param containerAppsEnvironmentName = 'cae-agenttalks-${environmentName}'
param logAnalyticsWorkspaceName = 'law-agenttalks-${environmentName}'
param databaseServerName = 'pgsql-agenttalks-${environmentName}'
param azureOpenAIAccountName = 'oai-agenttalks-${environmentName}'
param storageAccountName = 'saagenttalks${environmentName}'
param databaseServerAdminLogin = readEnvironmentVariable('DATABASE_ADMIN_USER')
param databaseServerAdminPassword = readEnvironmentVariable('DATABASE_ADMIN_PASSWORD')
param buzzsproutApiKey = readEnvironmentVariable('BUZZSPROUT_API_KEY')
param buzzsproutPodcastId = readEnvironmentVariable('BUZZSPROUT_PODCAST_ID')
param keycloakAdminPassword = readEnvironmentVariable('KEYCLOAK_ADMIN_PASSWORD')
param keycloakAdminUsername = readEnvironmentVariable('KEYCLOAK_ADMIN_USERNAME', 'admin')
param keycloakDashboardClientSecret = readEnvironmentVariable('KEYCLOAK_DASHBOARD_CLIENT_SECRET')
param frontendAuthenticationSecret = readEnvironmentVariable('FRONTEND_AUTHENTICATION_SECRET')

// The image names are derived from the container registry and a GIT hash that we set in the CI/CD pipeline.
param contentApiImageName = '${containerRegistryName}.azurecr.io/agenttalks/content-api:1.0.0-${imageHash}'
param readerApiImageName = '${containerRegistryName}.azurecr.io/agenttalks/reader-api:1.0.0-${imageHash}'
param podcastApiImageName = '${containerRegistryName}.azurecr.io/agenttalks/podcast-api:1.0.0-${imageHash}'
param dashboardImageName = '${containerRegistryName}.azurecr.io/agenttalks/dashboard:1.0.0-${imageHash}'
