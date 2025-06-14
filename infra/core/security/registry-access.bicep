param containerRegistryName string
param containerRegistryResourceGroupName string
param principalId string


var acrPullRoleDefinitionId = subscriptionResourceId('Microsoft.Authorization/roleDefinitions', '7f951dda-4ed3-4680-a7ca-43fe172d538d')

resource acrPullRoleAssignment 'Microsoft.Authorization/roleAssignments@2022-04-01' = {
  name: guid(containerRegistryName, principalId, 'acrpull')
  scope: containerRegistry
  properties: {
    roleDefinitionId: acrPullRoleDefinitionId
    principalId: principalId
    principalType: 'ServicePrincipal'
  }
}

resource containerRegistry 'Microsoft.ContainerRegistry/registries@2025-04-01' existing = {
  name: containerRegistryName
}
