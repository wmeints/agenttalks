import EpisodesTable from "@/components/episodes/episodes-table";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/episodes/")({
  component: RouteComponent,
});

function RouteComponent() {
  return (
    <div className="container mx-auto">
      <h1 className="text-3xl font-bold mb-6">Podcast Episodes</h1>
      <EpisodesTable />
    </div>
  );
}
