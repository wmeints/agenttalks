using './main.bicep'

var imageHash = readEnvironmentVariable('GIT_HASH', 'latest')

param environmentName = readEnvironmentVariable('AZURE_ENV_NAME', 'dev')
param location = readEnvironmentVariable('AZURE_LOCATION', 'swedencentral')
param containerRegistryName = 'acrnewscast'
param containerRegistryResourceGroupName = 'rg-newscast-shared'
param applicationInsightsName = 'ai-newscast-${environmentName}'
param containerAppsEnvironmentName = 'cae-newscast-${environmentName}'
param logAnalyticsWorkspaceName = 'law-newscast-${environmentName}'
param databaseServerName = 'pgsql-newscast-${environmentName}'
param azureOpenAIAccountName = 'oai-newscast-${environmentName}'
param storageAccountName = 'sanewscast${environmentName}'
param databaseServerAdminLogin = readEnvironmentVariable('DATABASE_ADMIN_USER')
param databaseServerAdminPassword = readEnvironmentVariable('DATABASE_ADMIN_PASSWORD')
param gmailServiceAccountCredentials = readEnvironmentVariable('GMAIL_SERVICE_ACCOUNT_CREDENTIALS') 
param buzzsproutApiKey = readEnvironmentVariable('BUZZSPROUT_API_KEY')
param buzzsproutPodcastId = readEnvironmentVariable('BUZZSPROUT_PODCAST_ID')
param keycloakAdminPassword = readEnvironmentVariable('KEYCLOAK_ADMIN_PASSWORD')
param keycloakAdminUsername = readEnvironmentVariable('KEYCLOAK_ADMIN_USERNAME', 'admin')
param keycloakDashboardClientSecret = readEnvironmentVariable('KEYCLOAK_DASHBOARD_CLIENT_SECRET')

// The image names are derived from the container registry and a GIT hash that we set in the CI/CD pipeline.
param contentApiImageName = '${containerRegistryName}.azurecr.io/newscast/content-api:1.0.0-${imageHash}'
param readerApiImageName = '${containerRegistryName}.azurecr.io/newscast/reader-api:1.0.0-${imageHash}'
param podcastApiImageName = '${containerRegistryName}.azurecr.io/newscast/podcast-api:1.0.0-${imageHash}'
param dashboardImageName = '${containerRegistryName}.azurecr.io/newscast/dashboard:1.0.0-${imageHash}'
