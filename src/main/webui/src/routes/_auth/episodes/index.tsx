import EpisodesTable from "@/components/episodes/episodes-table";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/episodes/")({
  component: RouteComponent,
});

function RouteComponent() {
  return (
    <div className="container mx-auto">
      <EpisodesTable />
    </div>
  );
}
