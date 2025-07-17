import { env } from "$env/dynamic/private";

export interface KeycloakConfig {
  issuer: string;
  clientId: string;
  clientSecret: string;
}

export interface AppConfig {
  keycloak: KeycloakConfig;
  contentApiUrl: string;
}

export function getKeycloakConfig(): KeycloakConfig {
  const issuer =
    env.KEYCLOAK_ISSUER ||
    process.env.KEYCLOAK_ISSUER ||
    "http://localhost:8180/realms/agenttalks";
  const clientId =
    env.KEYCLOAK_CLIENT_ID || process.env.KEYCLOAK_CLIENT_ID || "dashboard";
  const clientSecret =
    env.KEYCLOAK_CLIENT_SECRET ||
    process.env.KEYCLOAK_CLIENT_SECRET ||
    "default-secret";

  if (!issuer) {
    throw new Error(
      "Keycloak issuer is not defined. Please set KEYCLOAK_ISSUER environment variable.",
    );
  }

  if (!clientId) {
    throw new Error(
      "Keycloak client ID is not defined. Please set KEYCLOAK_CLIENT_ID environment variable.",
    );
  }

  if (!clientSecret) {
    throw new Error(
      "Keycloak client secret is not defined. Please set KEYCLOAK_CLIENT_SECRET environment variable.",
    );
  }

  return {
    issuer,
    clientId,
    clientSecret,
  };
}

export function getAppConfig(): AppConfig {
  const contentApiUrl =
    env.PUBLIC_CONTENT_API_URL ||
    process.env.PUBLIC_CONTENT_API_URL ||
    "http://localhost:8080/graphql";

  return {
    keycloak: getKeycloakConfig(),
    contentApiUrl,
  };
}
