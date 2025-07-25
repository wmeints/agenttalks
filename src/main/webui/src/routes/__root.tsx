import { Outlet, createRootRoute } from "@tanstack/react-router";
import NavigationBar from "@/components/navigation-bar";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { Toaster } from '@/components/ui/sonner'
export const Route = createRootRoute({
  component: () => <RootComponent />,
});

function RootComponent() {
  return (
    <>
      <NavigationBar />
      <div className="container mx-auto mt-6">
        <Outlet />
      </div>
      <TanStackRouterDevtools />
      <Toaster position="top-center" />
    </>
  );
}
