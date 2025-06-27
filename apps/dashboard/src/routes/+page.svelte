<script lang="ts">
	import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Badge } from '$lib/components/ui/badge';
	import { onMount } from 'svelte';

	// Mock data - In a real app, this would come from your API/database
	let metrics = {
		contentItemsThisWeek: 0,
		podcastEpisodesCreated: 0,
		lastUpdated: new Date()
	};

	// Mock content items for this week
	let contentItems = [
		{
			id: 1,
			title: "AI breakthrough in medical diagnosis",
			summary: "Researchers develop new AI system that can diagnose rare diseases with 95% accuracy, potentially revolutionizing healthcare diagnostics worldwide.",
			url: "https://technews.com/ai-medical-breakthrough",
			date: new Date('2025-06-26')
		},
		{
			id: 2,
			title: "Climate summit reaches historic agreement",
			summary: "World leaders agree on ambitious carbon reduction targets and establish $100B fund for developing nations to transition to clean energy.",
			url: "https://enviro-report.com/climate-summit-agreement",
			date: new Date('2025-06-25')
		},
		{
			id: 3,
			title: "New space mission launches successfully",
			summary: "NASA's latest Mars exploration mission launches with advanced rovers designed to search for signs of ancient microbial life.",
			url: "https://spaceweekly.com/mars-mission-launch",
			date: new Date('2025-06-24')
		},
		{
			id: 4,
			title: "Economic indicators show growth trends",
			summary: "Latest quarterly data reveals unexpected economic growth driven by technology sector innovation and increased consumer spending.",
			url: "https://financialtimes.com/economic-growth-trends",
			date: new Date('2025-06-23')
		},
		{
			id: 5,
			title: "Breakthrough in renewable energy storage",
			summary: "Scientists develop new battery technology that could store renewable energy for months, solving intermittency challenges.",
			url: "https://energytoday.com/battery-breakthrough",
			date: new Date('2025-06-22')
		}
	];

	// Function to get the current week's start date
	function getWeekStart() {
		const now = new Date();
		const day = now.getDay();
		const diff = now.getDate() - day + (day === 0 ? -6 : 1); // adjust when day is sunday
		return new Date(now.setDate(diff));
	}

	// Function to format date
	function formatDate(date: Date) {
		return date.toLocaleDateString('en-US', {
			weekday: 'long',
			year: 'numeric',
			month: 'long',
			day: 'numeric'
		});
	}

	// Simulate fetching metrics data
	async function loadMetrics() {
		// In a real application, you would fetch this data from your backend
		// For now, we'll simulate it with some mock data
		
		// Simulate API delay
		await new Promise(resolve => setTimeout(resolve, 500));
		
		// Mock data - replace with actual API calls
		metrics = {
			contentItemsThisWeek: contentItems.length, // Use actual count of content items
			podcastEpisodesCreated: Math.floor(Math.random() * 8) + 2, // Random number between 2-10
			lastUpdated: new Date()
		};
	}

	onMount(() => {
		loadMetrics();
	});

	$: weekStart = getWeekStart();

	// Function to format relative date
	function getRelativeDate(date: Date) {
		const now = new Date();
		const diffTime = Math.abs(now.getTime() - date.getTime());
		const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
		
		if (diffDays === 1) return 'Today';
		if (diffDays === 2) return 'Yesterday';
		return `${diffDays - 1} days ago`;
	}

	// Function to truncate text
	function truncateText(text: string, maxLength: number = 120) {
		if (text.length <= maxLength) return text;
		return text.substring(0, maxLength) + '...';
	}
</script>

<div class="container mx-auto p-6">
	<div class="mb-8">
		<h1 class="text-4xl font-bold tracking-tight mb-2">Dashboard</h1>
		<p class="text-muted-foreground text-lg">
			Welcome to your newscast content management dashboard
		</p>
	</div>

	<!-- Metrics Grid -->
	<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-2">
		<!-- Content Items This Week Card -->
		<Card class="relative overflow-hidden">
			<CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
				<CardTitle class="text-sm font-medium">Content Items Collected</CardTitle>
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
				<div class="text-2xl font-bold">{metrics.contentItemsThisWeek}</div>
				<p class="text-xs text-muted-foreground">
					Items collected since {formatDate(weekStart)}
				</p>
				<div class="mt-4">
					<Badge variant="secondary" class="text-xs">
						This Week
					</Badge>
				</div>
			</CardContent>
		</Card>

		<!-- Podcast Episodes Created Card -->
		<Card class="relative overflow-hidden">
			<CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
				<CardTitle class="text-sm font-medium">Podcast Episodes Created</CardTitle>
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
					<polygon points="11 5,6 9,2 9,2 15,6 15,11 19,11 5"></polygon>
					<path d="M15.54 8.46a5 5 0 0 1 0 7.07"></path>
					<path d="M19.07 4.93a10 10 0 0 1 0 14.14"></path>
				</svg>
			</CardHeader>
			<CardContent>
				<div class="text-2xl font-bold">{metrics.podcastEpisodesCreated}</div>
				<p class="text-xs text-muted-foreground">
					Total episodes produced
				</p>
				<div class="mt-4">
					<Badge variant="outline" class="text-xs">
						All Time
					</Badge>
				</div>
			</CardContent>
		</Card>
	</div>

	<!-- Content Items List -->
	<div class="mt-8">
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
						<div class="p-4 border rounded-lg hover:bg-muted/50 transition-colors">
							<div class="space-y-3">
								<!-- Title and URL -->
								<div class="flex items-start justify-between">
									<h4 class="font-medium text-sm leading-relaxed flex-1 pr-4">{item.title}</h4>
									<a 
										href={item.url} 
										target="_blank" 
										rel="noopener noreferrer"
										class="text-primary hover:underline text-xs flex items-center space-x-1 flex-shrink-0"
									>
										<span>View</span>
										<svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
											<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"></path>
										</svg>
									</a>
								</div>
								
								<!-- Summary -->
								<p class="text-sm text-muted-foreground leading-relaxed">
									{truncateText(item.summary)}
								</p>
								
								<!-- Date -->
								<div class="flex items-center text-xs text-muted-foreground">
									<span class="flex items-center space-x-1">
										<svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
											<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
										</svg>
										<span>{getRelativeDate(item.date)}</span>
									</span>
								</div>
							</div>
						</div>
					{/each}
				</div>
				
				{#if contentItems.length === 0}
					<div class="text-center py-8 text-muted-foreground">
						<svg class="w-12 h-12 mx-auto mb-4 opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
							<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
						</svg>
						<p class="text-sm">No content items collected this week</p>
						<p class="text-xs mt-1">Start collecting content to see it here</p>
					</div>
				{/if}

				<div class="mt-6 pt-4 border-t">
					<div class="flex items-center justify-between">
						<p class="text-xs text-muted-foreground">
							Last updated: {metrics.lastUpdated.toLocaleTimeString()}
						</p>
						<button class="text-xs text-primary hover:underline">
							View all content →
						</button>
					</div>
				</div>
			</CardContent>
		</Card>
	</div>
</div>
