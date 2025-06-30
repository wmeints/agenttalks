<script lang="ts">
	import {
		Card,
		CardContent,
		CardDescription,
		CardHeader,
		CardTitle,
	} from "$lib/components/ui/card";
	import ContentItem from "$lib/components/ContentItem.svelte";
	import type { PageProps } from "./$houdini";
	import EmptyContentList from "@/components/EmptyContentList.svelte";
	import ContentItemPager from "@/components/ContentItemPager.svelte";

	let { data }: PageProps = $props();
	let { ContentQuery } = $derived(data);

	let pageIndex = $state(0);
	let contentItems = $derived($ContentQuery?.data?.submissions || []);
	let totalItems = $derived(contentItems?.length || 0);

	async function nextPage() {
		pageIndex++;
		await ContentQuery.fetch({ variables: { pageIndex, pageSize: 10 } });
	}

	async function previousPage() {
		if (pageIndex > 0) {
			pageIndex--;
		}

		await ContentQuery.fetch({ variables: { pageIndex, pageSize: 10 } });
	}
</script>

<svelte:head>
	<title>Content Items - Agenttalks Dashboard</title>
</svelte:head>

<div class="container mx-auto p-6 pt-8">
	<div class="mb-8">
		<h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">
			Content Items
		</h1>
		<p class="text-gray-600 dark:text-gray-400">
			Manage and review content items for podcast episodes
		</p>
	</div>

	<!-- Content Items List -->
	<Card>
		<CardHeader>
			<CardTitle>Content Items</CardTitle>
			<CardDescription>
				Manage and review content items for podcast episodes
			</CardDescription>
		</CardHeader>
		<CardContent>
			<div class="space-y-4">
				{#if contentItems.length === 0}
					<EmptyContentList />
				{:else}
					{#each contentItems as item (item!.id)}
						<ContentItem {item} />
					{/each}
				{/if}
			</div>
			<ContentItemPager
				{pageIndex}
				{totalItems}
				{previousPage}
				{nextPage}
			/>
		</CardContent>
	</Card>
</div>
