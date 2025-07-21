import type React from "react";

import { Search } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

interface NoDataCardProps {
  title?: string;
  description?: string;
  actionText?: string;
  onAction?: () => void;
  icon?: React.ReactNode;
}

export default function NoDataCard({
  title = "No data found",
  description = "We couldn't find any results matching your criteria. Try adjusting your search or filters.",
  actionText,
  onAction,
  icon,
}: NoDataCardProps) {
  return (
    <Card className="w-full max-w-md mx-auto">
      <CardContent className="flex flex-col items-center justify-center p-8 text-center">
        <div className="mb-4 p-3 rounded-full bg-muted">
          {icon || <Search className="h-8 w-8 text-muted-foreground" />}
        </div>

        <h3 className="text-lg font-semibold text-foreground mb-2">{title}</h3>

        <p className="text-sm text-muted-foreground mb-6 max-w-sm leading-relaxed">
          {description}
        </p>

        {actionText && onAction && (
          <Button onClick={onAction} variant="outline">
            {actionText}
          </Button>
        )}
      </CardContent>
    </Card>
  );
}
