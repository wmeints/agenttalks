<script lang="ts">
	import { Badge } from "$lib/components/ui/badge";
	import PodcastEpisodesCard from "$lib/components/PodcastEpisodesCard.svelte";
	import ContentItemsWeekCard from "$lib/components/ContentItemsWeekCard.svelte";
	import DashboardContentItemsList from "$lib/components/DashboardContentItemsList.svelte";
	import { getWeekStart, formatDate } from "$lib/date-utils";
	import type { PageProps } from "./$houdini";
	import { Button, buttonVariants } from "@/components/ui/button";

	let { data }: PageProps = $props();
	let { dashboard } = $derived(data);

	let statistics = $dashboard.data?.statistics;
	let contentItems = $dashboard.data?.recentSubmissions || [];
	let lastUpdated = new Date();

	let weekStart = getWeekStart();
</script>

<svelte:head>
	<title>Home - Agenttalks Dashboard</title>
</svelte:head>

<div class="container mx-auto p-6 pt-8">
	<div class="mb-8">
		<h1 class="text-4xl font-bold tracking-tight mb-2">Dashboard</h1>
		<p class="text-muted-foreground text-lg">
			Welcome to the agenttalks content management dashboard
		</p>
	</div>

	<!-- Mobile Submit Content Button -->
	<div class="md:hidden mb-6">
		<a href="/content/submit" class={`${buttonVariants({ size: "lg" })} w-full`}>
			<svg
				class="w-6 h-6 mr-2"
				fill="none"
				stroke="currentColor"
				viewBox="0 0 24 24"
			>
				<path
					stroke-linecap="round"
					stroke-linejoin="round"
					stroke-width="2"
					d="M12 4v16m8-8H4"
				></path>
			</svg>
			Submit Content
		</a>
	</div>

	<!-- Metrics Grid -->
	<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-2">
		<!-- Content Items This Week Card -->
		<ContentItemsWeekCard
			submissionsLastWeek={statistics?.submissionsLastWeek || 0}
			{weekStart}
		/>

		<!-- Podcast Episodes Created Card -->
		<PodcastEpisodesCard episodeCount={statistics?.totalEpisodes || 0} />
	</div>

	<!-- Content Items List -->
	<div class="mt-8">
		<DashboardContentItemsList {contentItems} {lastUpdated} />
	</div>
</div>
