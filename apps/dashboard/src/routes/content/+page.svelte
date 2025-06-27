<script lang="ts">
	import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Badge } from '$lib/components/ui/badge';

	// Mock content items data
	let contentItems = [
		{
			id: 1,
			title: "AI breakthrough in medical diagnosis",
			summary: "Researchers develop new AI system that can diagnose rare diseases with 95% accuracy, potentially revolutionizing healthcare diagnostics worldwide.",
			url: "https://technews.com/ai-medical-breakthrough",
			source: "TechNews",
			category: "Technology",
			date: new Date('2025-06-26'),
			status: "processed"
		},
		{
			id: 2,
			title: "Climate summit reaches historic agreement",
			summary: "World leaders agree on ambitious carbon reduction targets and establish $100B fund for developing nations to transition to clean energy.",
			url: "https://enviro-report.com/climate-summit-agreement",
			source: "Environmental Report",
			category: "Environment",
			date: new Date('2025-06-25'),
			status: "processed"
		},
		{
			id: 3,
			title: "New space mission launches successfully",
			summary: "NASA's latest Mars exploration mission launches with advanced rovers designed to search for signs of ancient microbial life.",
			url: "https://spaceweekly.com/mars-mission-launch",
			source: "Space Weekly",
			category: "Science",
			date: new Date('2025-06-24'),
			status: "processed"
		},
		{
			id: 4,
			title: "Economic indicators show growth trends",
			summary: "Latest quarterly data reveals unexpected economic growth driven by technology sector innovation and increased consumer spending.",
			url: "https://financialtimes.com/economic-growth-trends",
			source: "Financial Times",
			category: "Business",
			date: new Date('2025-06-23'),
			status: "pending"
		},
		{
			id: 5,
			title: "Breakthrough in renewable energy storage",
			summary: "Scientists develop new battery technology that could store renewable energy for months, solving intermittency challenges.",
			url: "https://energytoday.com/battery-breakthrough",
			source: "Energy Today",
			category: "Technology",
			date: new Date('2025-06-22'),
			status: "processed"
		},
		{
			id: 6,
			title: "Global trade agreements reshape markets",
			summary: "New international trade partnerships emerge as countries adapt to changing economic landscapes and digital commerce growth.",
			url: "https://tradereport.com/global-agreements",
			source: "Trade Report",
			category: "Business",
			date: new Date('2025-06-21'),
			status: "archived"
		}
	];

	function formatDate(date: Date) {
		return date.toLocaleDateString('en-US', { 
			month: 'short', 
			day: 'numeric' 
		});
	}

	function getStatusColor(status: string) {
		switch (status) {
			case 'processed': return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300';
			case 'pending': return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300';
			case 'archived': return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300';
			default: return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300';
		}
	}

	function getCategoryColor(category: string) {
		switch (category) {
			case 'Technology': return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300';
			case 'Environment': return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300';
			case 'Science': return 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-300';
			case 'Business': return 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-300';
			default: return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300';
		}
	}

	let selectedCategory = 'all';
	let selectedStatus = 'all';

	$: filteredItems = contentItems.filter(item => {
		const categoryMatch = selectedCategory === 'all' || item.category === selectedCategory;
		const statusMatch = selectedStatus === 'all' || item.status === selectedStatus;
		return categoryMatch && statusMatch;
	});

	$: categories = [...new Set(contentItems.map(item => item.category))];
	$: statuses = [...new Set(contentItems.map(item => item.status))];
</script>

<svelte:head>
	<title>Content Items - Newscast Dashboard</title>
</svelte:head>

<div class="container mx-auto p-6 pt-8">
	<div class="mb-8">
		<h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">Content Items</h1>
		<p class="text-gray-600 dark:text-gray-400">Manage and review content items for podcast episodes</p>
	</div>

	<!-- Filters -->
	<div class="mb-6 flex flex-col sm:flex-row gap-4">
		<div class="flex flex-col">
			<label for="category" class="text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Category</label>
			<select 
				id="category"
				bind:value={selectedCategory}
				class="px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
			>
				<option value="all">All Categories</option>
				{#each categories as category}
					<option value={category}>{category}</option>
				{/each}
			</select>
		</div>

		<div class="flex flex-col">
			<label for="status" class="text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Status</label>
			<select 
				id="status"
				bind:value={selectedStatus}
				class="px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
			>
				<option value="all">All Statuses</option>
				{#each statuses as status}
					<option value={status}>{status.charAt(0).toUpperCase() + status.slice(1)}</option>
				{/each}
			</select>
		</div>
	</div>

	<!-- Content Items Grid -->
	<div class="space-y-4">
		{#each filteredItems as item}
			<Card class="hover:shadow-lg transition-shadow duration-200">
				<CardContent class="p-6">
					<div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
						<div class="flex-1">
							<div class="flex items-start justify-between mb-2">
								<h3 class="text-lg font-semibold text-gray-900 dark:text-white">
									{item.title}
								</h3>
								<div class="flex gap-2 ml-4">
									<Badge class={getStatusColor(item.status)}>
										{item.status}
									</Badge>
									<Badge class={getCategoryColor(item.category)}>
										{item.category}
									</Badge>
								</div>
							</div>
							<p class="text-gray-600 dark:text-gray-400 mb-3 line-clamp-2">
								{item.summary}
							</p>
							<div class="flex items-center gap-4 text-sm text-gray-500 dark:text-gray-400">
								<span>{item.source}</span>
								<span>•</span>
								<span>{formatDate(item.date)}</span>
							</div>
						</div>
						<div class="flex gap-2 lg:flex-col lg:w-32">
							<button class="flex-1 lg:flex-none bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200">
								View Article
							</button>
							{#if item.status === 'pending'}
								<button class="flex-1 lg:flex-none bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200">
									Process
								</button>
							{:else if item.status === 'processed'}
								<button class="flex-1 lg:flex-none bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200">
									Archive
								</button>
							{/if}
						</div>
					</div>
				</CardContent>
			</Card>
		{/each}
	</div>

	{#if filteredItems.length === 0}
		<div class="text-center py-12">
			<p class="text-gray-500 dark:text-gray-400 text-lg">No content items found matching your filters.</p>
		</div>
	{/if}

	<!-- Add New Content Button -->
	<div class="mt-8 text-center">
		<button class="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-md font-medium transition-colors duration-200">
			+ Add Content Item
		</button>
	</div>
</div>
