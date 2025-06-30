<script lang="ts">
	import {
		Card,
		CardContent,
		CardHeader,
		CardTitle,
	} from "$lib/components/ui/card";
	import { Badge } from "$lib/components/ui/badge";

	interface Props {
		episodeCount: number;
	}

	let { episodeCount }: Props = $props();
</script>

<Card class="relative overflow-hidden">
	<CardHeader
		class="flex flex-row items-center justify-between space-y-0 pb-2"
	>
		<CardTitle class="text-sm font-medium"
			>Podcast Episodes Created</CardTitle
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
			<polygon points="11 5,6 9,2 9,2 15,6 15,11 19,11 5"
			></polygon>
			<path d="M15.54 8.46a5 5 0 0 1 0 7.07"></path>
			<path d="M19.07 4.93a10 10 0 0 1 0 14.14"></path>
		</svg>
	</CardHeader>
	<CardContent>
		<div class="text-2xl font-bold">
			{episodeCount}
		</div>
		<p class="text-xs text-muted-foreground">
			Total episodes produced
		</p>
		<div class="mt-4">
			<Badge variant="outline" class="text-xs">All Time</Badge>
		</div>
	</CardContent>
</Card>
