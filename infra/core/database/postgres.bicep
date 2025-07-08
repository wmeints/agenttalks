param name string
param location string = resourceGroup().location
param tags object = {}
param administratorLogin string
@secure()
param administratorPassword string

resource postgresServer 'Microsoft.DBforPostgreSQL/flexibleServers@2025-01-01-preview' = {
  name: name
  location: location
  tags: tags
  sku: {
    name: 'Standard_B1ms'
    tier: 'Burstable'
  }
  properties: {
    version: '16'
    administratorLogin: administratorLogin
    administratorLoginPassword: administratorPassword
    storage: {
      storageSizeGB: 32
    }
    highAvailability: {
      mode: 'Disabled'
    }
    authConfig: {
      activeDirectoryAuth: 'Enabled'
      passwordAuth: 'Enabled'
      tenantId: subscription().tenantId
    }
  }

  resource contentApiDatabase 'databases' = {
    name: 'content'
  }

  resource podcastApiDatabase 'databases' = {
    name: 'podcasts'
  }

  resource temporalDatabase 'databases' = {
    name: 'temporal'
  }

  resource temporalVisibilityDatabase 'databases' = {
    name: 'temporal_visibility'
  }

  resource keycloakDatabase 'databases' = {
    name: 'keycloak'
  }

  resource allowInternalAddressess 'firewallRules' = {
    name: 'AllowInternalAddresses'
    properties: {
      startIpAddress: '0.0.0.0'
      endIpAddress: '0.0.0.0'
    }
  }
}

output name string = postgresServer.name
output domainName string = postgresServer.properties.fullyQualifiedDomainName
