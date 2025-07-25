import { FileText } from "lucide-react";

import { Card, CardContent } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";

export default function Component() {
  return (
    <Card className="w-full max-w-md mx-auto">
      <CardContent className="flex flex-col items-center justify-center p-6 space-y-4">
        <FileText className="h-12 w-12 text-muted-foreground" />
        <div className="text-center space-y-2">
          <p className="text-sm font-medium">Loading content</p>
          <div className="w-full">
            <Progress
              className="w-full h-2 [&>div]:animate-pulse [&>div]:bg-primary [&>div]:animate-[loading_2s_ease-in-out_infinite]"
              value={undefined}
            />
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
