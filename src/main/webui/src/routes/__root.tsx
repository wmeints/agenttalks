import { Outlet, createRootRouteWithContext } from "@tanstack/react-router";
import NavigationBar from "@/components/navigation-bar";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";

interface ApplicationRouterContext {
  isSignedIn: boolean;
}

export const Route = createRootRouteWithContext<ApplicationRouterContext>()({
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
    </>
  );
}
