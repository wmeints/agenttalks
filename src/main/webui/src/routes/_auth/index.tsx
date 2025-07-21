import ErrorCard from "@/components/cards/error-card";
import LoadingCard from "@/components/cards/loading-card";
import MetricCard from "@/components/cards/metric-card";
import PendingSubmissionsTable from "@/components/submissions/pending-submissions-table";
import { graphql } from "@/gql";
import { useQuery } from "@apollo/client";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/")({
  component: RouteComponent,
});

const fetchStatisticsQuery = graphql(`
  query fetchStatisticsQuery {
    statistics {
      pendingSubmissions
      totalEpisodes
    }
  }
`);

function RouteComponent() {
  const { data, loading, error } = useQuery(fetchStatisticsQuery);

  if (loading) {
    return <LoadingCard />
  }

  if (error) {
    return (
      <ErrorCard
        errorMessage="Failed to load content for the page"
        extendedDescription="There's a problem loading your data. Please refresh the page to try again."
      />
    );
  }

  return (
    <div className="container mx-auto space-y-4">
      <div className="grid md:grid-cols-2 gap-4">
        <MetricCard
          metric={data?.statistics?.pendingSubmissions}
          label="Content submissions"
          description="The number of submissions ready for the next episode."
        />
        <MetricCard
          metric={data?.statistics?.totalEpisodes}
          label="Episodes"
          description="The number of episodes published."
        />
      </div>
      <h2 className="text-3xl font-bold">Pending submissions</h2>
      <PendingSubmissionsTable />
    </div>
  );
}
