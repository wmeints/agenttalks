import { graphql } from "@/gql";
import { useQuery } from "@apollo/client";
import { useState } from "react";
import ContentSubmissionCard from "../cards/content-submission-card";
import ErrorCard from "../cards/error-card";
import LoadingCard from "../cards/loading-card";
import NoDataCard from "../cards/no-data-card";
import Pager from "../pager";

const fetchSubmissionsQuery = graphql(`
  query fetchSubmissionsQuery($pageIndex: Int!, $pageSize: Int!) {
    submissions(pageIndex: $pageIndex, pageSize: $pageSize) {
      totalPages
      totalItems
      items {
        id
        title
        url
        summary
        status
      }
    }
  }
`);

export default function SubmissionsTable() {
  const [currentPage, setCurrentPage] = useState(1);
  const { data, error, loading } = useQuery(fetchSubmissionsQuery, {
    variables: { pageIndex: currentPage - 1, pageSize: 10 },
  });

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

  if (data?.submissions.totalItems === 0) {
    return (
      <NoDataCard
        title="No submissions found"
        description="Looks like there are no submissions yet..."
      />
    );
  }

  return (

    <div className="space-y-6">
      {data?.submissions.items.filter(submission => submission).map(submission => (<ContentSubmissionCard submission={submission!} key={submission!.id} />))}

      <Pager
        initialPage={currentPage}
        onPageChange={setCurrentPage}
        totalPages={data?.submissions.totalPages}
      />
    </div>
  );
}
