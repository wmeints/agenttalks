import { RouterProvider, createRouter } from "@tanstack/react-router";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { routeTree } from "./routeTree.gen";

import BackendClientProvider from "@/providers/BackendClientProvider";
import { ClerkProvider } from "@clerk/clerk-react";
import "./index.css";

// Create a new router instance
const router = createRouter({
  routeTree,
  context: {
    isSignedIn: false,
  },
});

// Register the router instance for type safety
declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}

// Import your Publishable Key
const PUBLISHABLE_KEY = 'pk_test_YWNjZXB0ZWQtZ29zaGF3ay05Ny5jbGVyay5hY2NvdW50cy5kZXYk';

if (!PUBLISHABLE_KEY) {
  throw new Error("Add your Clerk Publishable Key to the .env file");
}

const rootElement = document.getElementById("root")!;

if (!rootElement.innerHTML) {
  const root = createRoot(rootElement);
  root.render(
    <StrictMode>
      <ClerkProvider publishableKey={PUBLISHABLE_KEY}>
        <BackendClientProvider>
          <RouterProvider router={router} />
        </BackendClientProvider>
      </ClerkProvider>
    </StrictMode>,
  );
}
