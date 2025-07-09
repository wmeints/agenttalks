import { getKeycloakConfig } from "$lib/config";
import { SvelteKitAuth } from "@auth/sveltekit";
import Keycloak from "@auth/sveltekit/providers/keycloak";

declare module "@auth/core/types" {
  interface Session extends DefaultSession {
    accessToken?: string;
  }
}

const keycloakConfig = getKeycloakConfig();

export const { handle, signIn, signOut } = SvelteKitAuth({
  trustHost: true, // We run behind a reverse proxy, so this is mandatory.
  providers: [
    Keycloak({
      issuer: keycloakConfig.issuer,
      clientId: keycloakConfig.clientId,
      clientSecret: keycloakConfig.clientSecret,
    }),
  ],
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
    },
  },
});
