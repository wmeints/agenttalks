import { graphql } from "@/gql";
import { useQuery } from "@apollo/client";
import { Card, CardContent } from "../ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../ui/table";
import NoDataCard from "../cards/no-data-card";
import ErrorCard from "../cards/error-card";
import LoadingCard from "../cards/loading-card";

const fetchPendingSubmissionsQuery = graphql(`
  query fetchPendingSubmissionsQuery {
    pendingSubmissions {
        id
        title
        url
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
        <Card>
            <CardContent>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Title</TableHead>
                            <TableHead>URL</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {data?.pendingSubmissions.map((submission) => (
                            <TableRow>
                                <TableCell>{submission?.title}</TableCell>
                                <TableCell>{submission?.url}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </CardContent>
        </Card>
    );
}