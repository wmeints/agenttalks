import { env } from '$env/dynamic/private';

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
    const issuer = env.KEYCLOAK_ISSUER || process.env.KEYCLOAK_ISSUER;
    const clientId = env.KEYCLOAK_CLIENT_ID || process.env.KEYCLOAK_CLIENT_ID;
    const clientSecret = env.KEYCLOAK_CLIENT_SECRET || process.env.KEYCLOAK_CLIENT_SECRET;

    if (!issuer || !clientId || !clientSecret) {
        throw new Error('Missing required Keycloak configuration. Please set KEYCLOAK_ISSUER, KEYCLOAK_CLIENT_ID, and KEYCLOAK_CLIENT_SECRET environment variables.');
    }

    return {
        issuer,
        clientId,
        clientSecret
    };
}

export function getAppConfig(): AppConfig {
    const contentApiUrl = env.PUBLIC_CONTENT_API_URL || process.env.PUBLIC_CONTENT_API_URL || 'http://localhost:8080/graphql';
    
    return {
        keycloak: getKeycloakConfig(),
        contentApiUrl
    };
}