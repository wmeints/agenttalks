import { AlertTriangle } from "lucide-react";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";

interface ErrorCardProps {
    errorMessage?: string;
    extendedDescription?: string;
}

export default function ErrorCard({
    errorMessage = "Something went wrong",
    extendedDescription = "An unexpected error occurred. Please try again later.",
}: ErrorCardProps) {
    return (
        <Card className="w-full max-w-md mx-auto border-destructive/50 bg-destructive/5">
            <CardHeader className="pb-3">
                <div className="flex items-center gap-2">
                    <AlertTriangle className="h-5 w-5 text-destructive" />
                    <CardTitle className="text-destructive">{errorMessage}</CardTitle>
                </div>
            </CardHeader>
            <CardContent>
                <CardDescription className="text-muted-foreground">
                    {extendedDescription}
                </CardDescription>
            </CardContent>
        </Card>
    );
}
