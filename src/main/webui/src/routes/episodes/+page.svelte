<script lang="ts">
	import {
		Card,
		CardContent,
	} from "$lib/components/ui/card";
	import EpisodeCard from "$lib/components/EpisodeCard.svelte";
	import type { PageProps } from "./$houdini";
    import { Volume2 } from "lucide-svelte";

	let { data }: PageProps = $props();
	let { episodes } = $derived(data);
	let items = $episodes.data?.episodes || [];
</script>

<svelte:head>
	<title>Podcast Episodes - Agenttalks Dashboard</title>
</svelte:head>

<div class="container mx-auto p-6 pt-8">
	<div class="mb-8">
		<h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">
			Podcast Episodes
		</h1>
		<p class="text-gray-600 dark:text-gray-400">
			Manage your podcast episodes and track performance
		</p>
	</div>

	<!-- Episodes Grid -->
	<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
		{#each items as episode}
			<EpisodeCard {episode} />
		{/each}
	</div>

	{#if items.length === 0}
		<Card>
			<CardContent>
				<div class="text-center py-16 text-muted-foreground">
					<div class="justify-center flex flex-row">
						<Volume2 class="w-16 h-16 opacity-50" />
					</div>
					<h3 class="text-lg font-medium mb-2">No episodes yet</h3>
					<p class="text-sm">
						Hmm, no episodes yet...
					</p>
					<p class="text-xs mt-1">
						Start collecting content and we'll create an episode next Friday!
					</p>
				</div>
			</CardContent>
		</Card>
	{/if}
</div>
