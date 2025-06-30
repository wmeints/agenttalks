<script lang="ts">
	import { Badge } from "$lib/components/ui/badge";
	import PodcastEpisodesCard from "$lib/components/PodcastEpisodesCard.svelte";
	import ContentItemsWeekCard from "$lib/components/ContentItemsWeekCard.svelte";
	import DashboardContentItemsList from "$lib/components/DashboardContentItemsList.svelte";
	import { getWeekStart, formatDate } from "$lib/date-utils";
	import type { PageProps } from "./$houdini";
	import { Button, buttonVariants } from "@/components/ui/button";
    import { Plus } from "lucide-svelte";

	let { data }: PageProps = $props();
	let { dashboard } = $derived(data);

	let statistics = $dashboard.data?.statistics || { 
		submissionsLastWeek: 0,
		totalEpisodes: 0,
	};

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
	<div class="md:hidden mb-6">
		<a href="/content/submit" class={`${buttonVariants({ size: "lg" })} w-full`}>
			<Plus />
			Submit Content
		</a>
	</div>
	<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-2">
		<ContentItemsWeekCard
			submissionsLastWeek={statistics.submissionsLastWeek}
			{weekStart}
		/>
		<PodcastEpisodesCard episodeCount={statistics.totalEpisodes} />
	</div>
	<div class="mt-8">
		<DashboardContentItemsList {contentItems} {lastUpdated} />
	</div>
</div>
