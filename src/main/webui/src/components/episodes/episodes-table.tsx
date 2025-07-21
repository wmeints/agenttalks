import { graphql } from "@/gql";
import { useState } from "react";
import { useQuery } from "@apollo/client";
import LoadingCard from "../loading-card";
import { Card, CardContent } from "../ui/card";
import Pager from "../pager";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../ui/table";
import ErrorCard from "../error-card";
import NoDataCard from "../no-data-card";

const fetchEpisodesQuery = graphql(`
  query fetchEpisodesQuery($pageIndex: Int!, $pageSize: Int!) {
    episodes(pageIndex: $pageIndex, pageSize: $pageSize) {
      totalPages
      totalItems
      items {
        id
        title
      }
    }
  }
`);

export default function EpisodesTable() {
  const [currentPage, setCurrentPage] = useState(1);
  const { data, error, loading } = useQuery(fetchEpisodesQuery, {
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

  if (data?.episodes.totalItems === 0) {
    return (
      <NoDataCard
        title="No episodes found"
        description="Looks like there are no episodes yet..."
      />
    );
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Title</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {data?.episodes.items.map((episode) => (
                <TableRow>
                  <TableCell>{episode?.title}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
      <Pager
        initialPage={currentPage}
        onPageChange={setCurrentPage}
        totalPages={data?.episodes.totalPages}
      />
    </div>
  );
}
