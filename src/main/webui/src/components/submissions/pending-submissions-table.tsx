import { graphql } from "@/gql";
import { useQuery } from "@apollo/client";
import ContentSubmissionCard from "../cards/content-submission-card";
import ErrorCard from "../cards/error-card";
import LoadingCard from "../cards/loading-card";
import NoDataCard from "../cards/no-data-card";

const fetchPendingSubmissionsQuery = graphql(`
  query fetchPendingSubmissionsQuery {
    pendingSubmissions {
        id
        title
        url
        summary
        status
    }
  }
`);

export default function PendingSubmissionsTable() {
    const { data, error, loading } = useQuery(fetchPendingSubmissionsQuery);

    if (loading) {
        return <LoadingCard />;
    }

    if (error) {
        return (
            <ErrorCard
                errorMessage="Failed to load content for the page"
                extendedDescription="There's a problem loading your data. Please refresh the page to try again."
            />
        );
    }

    if (data?.pendingSubmissions.length === 0) {
        return (
            <NoDataCard
                title="No submissions found"
                description="Looks like there are no submissions yet..."
            />
        );
    }

    return (
        <>
            {data?.pendingSubmissions.filter(submission => submission).map(submission => (<ContentSubmissionCard submission={submission!} key={submission!.id} />))}
        </>
    );
}