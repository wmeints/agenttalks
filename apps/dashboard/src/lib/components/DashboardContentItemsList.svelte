<script lang="ts">
	import {
		Card,
		CardContent,
		CardDescription,
		CardHeader,
		CardTitle,
	} from "$lib/components/ui/card";
	import { getRelativeDate } from "$lib/date-utils";
	import { truncateText } from "$lib/text-utils";

	type ContentItem = {
		readonly id: number;
		readonly title: string | null;
		readonly url: string;
		readonly summary: string | null;
		readonly dateCreated: Date;
	};

	type Props = {
		contentItems: ContentItem[];
		lastUpdated?: Date;
		showFooter?: boolean;
	};

	let { 
		contentItems, 
		lastUpdated = new Date(), 
		showFooter = true 
	}: Props = $props();
</script>

<Card>
	<CardHeader>
		<CardTitle>This Week's Content Items</CardTitle>
		<CardDescription>
			Collected content ready for your next podcast episode
		</CardDescription>
	</CardHeader>
	<CardContent>
		<div class="space-y-4">
			{#each contentItems as item (item.id)}
				<div
					class="p-4 border rounded-lg hover:bg-muted/50 transition-colors"
				>
					<div class="space-y-3">
						<!-- Title and URL -->
						<div class="flex items-start justify-between">
							<h4
								class="font-medium text-sm leading-relaxed flex-1 pr-4"
							>
								{item.title}
							</h4>
							<a
								href={item.url}
								target="_blank"
								rel="noopener noreferrer"
								class="text-primary hover:underline text-xs flex items-center space-x-1 flex-shrink-0"
							>
								<span>View</span>
								<svg
									class="w-3 h-3"
									fill="none"
									stroke="currentColor"
									viewBox="0 0 24 24"
								>
									<path
										stroke-linecap="round"
										stroke-linejoin="round"
										stroke-width="2"
										d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"
									></path>
								</svg>
							</a>
						</div>

						<!-- Summary -->
						<p
							class="text-sm text-muted-foreground leading-relaxed"
						>
							{truncateText(item.summary || '')}
						</p>

						<!-- Date -->
						<div
							class="flex items-center text-xs text-muted-foreground"
						>
							<span class="flex items-center space-x-1">
								<svg
									class="w-3 h-3"
									fill="none"
									stroke="currentColor"
									viewBox="0 0 24 24"
								>
									<path
										stroke-linecap="round"
										stroke-linejoin="round"
										stroke-width="2"
										d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
									></path>
								</svg>
								<span
									>{getRelativeDate(
										item.dateCreated || new Date(),
									)}</span
								>
							</span>
						</div>
					</div>
				</div>
			{/each}
		</div>

		{#if contentItems.length === 0}
			<div class="text-center py-8 text-muted-foreground">
				<svg
					class="w-12 h-12 mx-auto mb-4 opacity-50"
					fill="none"
					stroke="currentColor"
					viewBox="0 0 24 24"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
					></path>
				</svg>
				<p class="text-sm">
					No content items collected this week
				</p>
				<p class="text-xs mt-1">
					Start collecting content to see it here
				</p>
			</div>
		{/if}

		{#if showFooter}
			<div class="mt-6 pt-4 border-t">
				<div class="flex items-center justify-between">
					<p class="text-xs text-muted-foreground">
						Last updated: {lastUpdated.toLocaleTimeString()}
					</p>
					<a href="/content" class="text-xs text-primary hover:underline">
						View all content →
					</a>
				</div>
			</div>
		{/if}
	</CardContent>
</Card>
