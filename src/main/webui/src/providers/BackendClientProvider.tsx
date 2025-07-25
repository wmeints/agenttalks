import React from "react";
import { ApolloProvider } from "@apollo/client";
import { useAuth } from "@clerk/clerk-react";
import { createApolloClient } from "@/lib/client";

interface BackendClientProviderProps {
  children?: React.ReactNode;
}

export default function BackendClientProvider({
  children,
}: BackendClientProviderProps) {
  const { getToken } = useAuth();

  const client = React.useMemo(() => {
    return createApolloClient(() => getToken({ template: "backend-token" }));
  }, [getToken]);

  return <ApolloProvider client={client}>{children}</ApolloProvider>;
}
