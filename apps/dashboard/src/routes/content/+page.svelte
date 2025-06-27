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
			date: new Date('2025-06-26'),
			status: "processed"
		},
		{
			id: 2,
			title: "Climate summit reaches historic agreement",
			summary: "World leaders agree on ambitious carbon reduction targets and establish $100B fund for developing nations to transition to clean energy.",
			url: "https://enviro-report.com/climate-summit-agreement",
			source: "Environmental Report",
			date: new Date('2025-06-25'),
			status: "processed"
		},
		{
			id: 3,
			title: "New space mission launches successfully",
			summary: "NASA's latest Mars exploration mission launches with advanced rovers designed to search for signs of ancient microbial life.",
			url: "https://spaceweekly.com/mars-mission-launch",
			source: "Space Weekly",
			date: new Date('2025-06-24'),
			status: "processed"
		},
		{
			id: 4,
			title: "Economic indicators show growth trends",
			summary: "Latest quarterly data reveals unexpected economic growth driven by technology sector innovation and increased consumer spending.",
			url: "https://financialtimes.com/economic-growth-trends",
			source: "Financial Times",
			date: new Date('2025-06-23'),
			status: "pending"
		},
		{
			id: 5,
			title: "Breakthrough in renewable energy storage",
			summary: "Scientists develop new battery technology that could store renewable energy for months, solving intermittency challenges.",
			url: "https://energytoday.com/battery-breakthrough",
			source: "Energy Today",
			date: new Date('2025-06-22'),
			status: "processed"
		},
		{
			id: 6,
			title: "Global trade agreements reshape markets",
			summary: "New international trade partnerships emerge as countries adapt to changing economic landscapes and digital commerce growth.",
			url: "https://tradereport.com/global-agreements",
			source: "Trade Report",
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

	// Function to get relative date (like on homepage)
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

	function getStatusColor(status: string) {
		switch (status) {
			case 'processed': return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300';
			case 'pending': return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300';
			case 'archived': return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300';
			default: return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300';
		}
	}

	let selectedStatus = 'all';
	let currentPage = 1;
	const itemsPerPage = 10;

	$: filteredItems = contentItems.filter(item => {
		const statusMatch = selectedStatus === 'all' || item.status === selectedStatus;
		return statusMatch;
	});

	$: statuses = [...new Set(contentItems.map(item => item.status))];
	
	$: totalPages = Math.ceil(filteredItems.length / itemsPerPage);
	$: paginatedItems = filteredItems.slice(
		(currentPage - 1) * itemsPerPage,
		currentPage * itemsPerPage
	);

	// Reset to first page when filter changes
	$: if (selectedStatus) {
		currentPage = 1;
	}

	function goToPage(page: number) {
		if (page >= 1 && page <= totalPages) {
			currentPage = page;
		}
	}

	function nextPage() {
		if (currentPage < totalPages) {
			currentPage++;
		}
	}

	function previousPage() {
		if (currentPage > 1) {
			currentPage--;
		}
	}
</script>

<svelte:head>
	<title>Content Items - Agenttalks Dashboard</title>
</svelte:head>

<div class="container mx-auto p-6 pt-8">
	<div class="mb-8">
		<h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">Content Items</h1>
		<p class="text-gray-600 dark:text-gray-400">Manage and review content items for podcast episodes</p>
	</div>

	<!-- Filters -->
	<div class="mb-6 flex flex-col sm:flex-row gap-4">
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
				{#each paginatedItems as item (item.id)}
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
							
							<!-- Metadata row -->
							<div class="flex items-center justify-between text-xs text-muted-foreground">
								<div class="flex items-center space-x-3">
									<span class="flex items-center space-x-1">
										<svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
											<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
										</svg>
										<span>{getRelativeDate(item.date)}</span>
									</span>
									<span>•</span>
									<span>{item.source}</span>
								</div>
								<div class="flex items-center space-x-2">
									<Badge class={getStatusColor(item.status)}>
										{item.status}
									</Badge>
								</div>
							</div>
						</div>
					</div>
				{/each}
			</div>
			
			{#if filteredItems.length === 0}
				<div class="text-center py-8 text-muted-foreground">
					<svg class="w-12 h-12 mx-auto mb-4 opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
						<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
					</svg>
					<p class="text-sm">No content items found matching your filters</p>
					<p class="text-xs mt-1">Try adjusting your filter criteria</p>
				</div>
			{:else}
				<!-- Pagination Controls -->
				<div class="mt-6 pt-4 border-t">
					<div class="flex items-center justify-between">
						<p class="text-xs text-muted-foreground">
							Showing {(currentPage - 1) * itemsPerPage + 1} to {Math.min(currentPage * itemsPerPage, filteredItems.length)} of {filteredItems.length} items
						</p>
						
						{#if totalPages > 1}
							<div class="flex items-center space-x-2">
								<button 
									on:click={previousPage}
									disabled={currentPage === 1}
									class="px-3 py-1 text-xs border rounded-md hover:bg-muted/50 disabled:opacity-50 disabled:cursor-not-allowed"
								>
									Previous
								</button>
								
								<div class="flex items-center space-x-1">
									{#each Array(totalPages) as _, i}
										{@const page = i + 1}
										{#if page === 1 || page === totalPages || (page >= currentPage - 1 && page <= currentPage + 1)}
											<button 
												on:click={() => goToPage(page)}
												class="w-8 h-8 text-xs border rounded-md hover:bg-muted/50 {currentPage === page ? 'bg-primary text-primary-foreground' : ''}"
											>
												{page}
											</button>
										{:else if page === currentPage - 2 || page === currentPage + 2}
											<span class="text-xs text-muted-foreground">...</span>
										{/if}
									{/each}
								</div>
								
								<button 
									on:click={nextPage}
									disabled={currentPage === totalPages}
									class="px-3 py-1 text-xs border rounded-md hover:bg-muted/50 disabled:opacity-50 disabled:cursor-not-allowed"
								>
									Next
								</button>
							</div>
						{/if}
					</div>
				</div>
			{/if}
		</CardContent>
	</Card>
</div>
