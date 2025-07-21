import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/submissions/new")({
  component: RouteComponent,
});

function RouteComponent() {
  return <div>Hello "/_auth/submissions/new"!</div>;
}
