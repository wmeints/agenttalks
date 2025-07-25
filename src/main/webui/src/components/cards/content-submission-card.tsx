import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { ExternalLink } from "lucide-react";
import { Button } from "../ui/button";

interface ContentSubmissionCardProps {
    submission: ContentSubmissionFields;
}

interface ContentSubmissionFields {
    url: string;
    title?: string | null;
    summary?: string | null;
}

export default function ContentSubmissionCard({
    submission,
}: ContentSubmissionCardProps) {
    const displayTitle = submission.title || submission.url;

    return (
        <Card className="w-full">
            <CardHeader>
                <CardTitle className="text-lg leading-tight">
                    {displayTitle}
                </CardTitle>
            </CardHeader>

            {submission.summary && (
                <CardContent>
                    <p className="text-md leading-relaxed">
                        {submission.summary}
                    </p>
                </CardContent>
            )}

            <CardFooter className="flex items-center justify-between">
                <span className="text-xs text-muted-foreground truncate flex-1 mr-4">
                    {submission.url}
                </span>
                <Button variant="ghost" asChild>
                    <a href={submission.url} target="_blank">
                        <ExternalLink />
                        Open
                    </a>
                </Button>
            </CardFooter>
        </Card>
    );
}
