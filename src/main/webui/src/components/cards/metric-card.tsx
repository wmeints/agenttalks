import { Card, CardContent } from "@/components/ui/card";

interface MetricCardProps {
  metric: string | number;
  label: string;
  description: string;
  trend?: {
    value: string;
    isPositive: boolean;
  };
}

export default function MetricCard({
  metric,
  label,
  description,
  trend,
}: MetricCardProps) {
  return (
    <Card className="w-full">
      <CardContent className="p-6">
        <div className="space-y-2">
          {/* Large metric display */}
          <div className="text-3xl font-bold tracking-tight">{metric}</div>

          {/* Small label */}
          <div className="text-sm font-medium text-foreground">{label}</div>

          {/* Optional trend indicator */}
          {trend && (
            <div
              className={`text-xs font-medium ${trend.isPositive ? "text-green-600" : "text-red-600"}`}
            >
              {trend.isPositive ? "↗" : "↘"} {trend.value}
            </div>
          )}

          {/* Description in lighter font */}
          <p className="text-sm text-muted-foreground leading-relaxed">
            {description}
          </p>
        </div>
      </CardContent>
    </Card>
  );
}
