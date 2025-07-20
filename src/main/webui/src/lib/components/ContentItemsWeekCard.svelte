<script lang="ts">
	import {
		Card,
		CardContent,
		CardHeader,
		CardTitle,
	} from "$lib/components/ui/card";
	import { Badge } from "$lib/components/ui/badge";
	import { formatDate } from "$lib/date-utils";

	interface Props {
		submissionsLastWeek: number;
		weekStart: Date;
	}

	let { submissionsLastWeek, weekStart }: Props = $props();
</script>

<Card class="relative overflow-hidden">
	<CardHeader
		class="flex flex-row items-center justify-between space-y-0 pb-2"
	>
		<CardTitle class="text-sm font-medium"
			>Content Items Submitted</CardTitle
		>
		<svg
			xmlns="http://www.w3.org/2000/svg"
			viewBox="0 0 24 24"
			fill="none"
			stroke="currentColor"
			stroke-linecap="round"
			stroke-linejoin="round"
			stroke-width="2"
			class="h-4 w-4 text-muted-foreground"
		>
			<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path>
			<circle cx="9" cy="7" r="4"></circle>
			<path d="M22 21v-2a4 4 0 0 0-3-3.87"></path>
			<path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
		</svg>
	</CardHeader>
	<CardContent>
		<div class="text-2xl font-bold">
			{submissionsLastWeek}
		</div>
		<p class="text-xs text-muted-foreground">
			Submissions collected since {formatDate(weekStart)}
		</p>
		<div class="mt-4">
			<Badge variant="secondary" class="text-xs">This Week</Badge>
		</div>
	</CardContent>
</Card>
