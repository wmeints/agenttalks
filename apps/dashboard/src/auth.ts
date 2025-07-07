import { SvelteKitAuth, type DefaultSession } from '@auth/sveltekit';
import Keycloak from '@auth/sveltekit/providers/keycloak';
import { getKeycloakConfig } from '$lib/server/config';

declare module '@auth/core/types' {
    interface Session extends DefaultSession {
        accessToken?: string;
    }
}

const keycloakConfig = getKeycloakConfig();

export const { handle, signIn, signOut } = SvelteKitAuth({
    providers: [Keycloak({
        issuer: keycloakConfig.issuer,
        clientId: keycloakConfig.clientId,
        clientSecret: keycloakConfig.clientSecret,
    })],
    callbacks: {
        async jwt({ token, account }) {
            if (account) {
                token.accessToken = account.access_token;
            }

            return token;
        },
        async session({ session, token }) {
            session.accessToken = token.accessToken as string;
            return session;
        }
    }
});