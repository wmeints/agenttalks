<script lang="ts">
	import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Badge } from '$lib/components/ui/badge';

	// Mock podcast episodes data
	let episodes = [
		{
			id: 1,
			title: "Tech News Weekly #47",
			description: "AI developments, climate tech, and space exploration updates",
			status: "published",
			publishDate: new Date('2025-06-26'),
			duration: "25:30",
			downloads: 1250
		},
		{
			id: 2,
			title: "Tech News Weekly #46",
			description: "Economic trends and renewable energy breakthroughs",
			status: "published",
			publishDate: new Date('2025-06-19'),
			duration: "22:45",
			downloads: 1180
		},
		{
			id: 3,
			title: "Tech News Weekly #45",
			description: "Medical AI and healthcare innovation spotlight",
			status: "published",
			publishDate: new Date('2025-06-12'),
			duration: "28:15",
			downloads: 1320
		},
		{
			id: 4,
			title: "Tech News Weekly #48",
			description: "Upcoming episode - Financial markets and tech policy",
			status: "draft",
			publishDate: new Date('2025-07-03'),
			duration: "TBD",
			downloads: 0
		}
	];

	function formatDate(date: Date) {
		return date.toLocaleDateString('en-US', { 
			year: 'numeric', 
			month: 'long', 
			day: 'numeric' 
		});
	}

	function getStatusColor(status: string) {
		switch (status) {
			case 'published': return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300';
			case 'draft': return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300';
			case 'scheduled': return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300';
			default: return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300';
		}
	}
</script>

<svelte:head>
	<title>Podcast Episodes - Newscast Dashboard</title>
</svelte:head>

<div class="container mx-auto p-6 pt-8">
	<div class="mb-8">
		<h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">Podcast Episodes</h1>
		<p class="text-gray-600 dark:text-gray-400">Manage your podcast episodes and track performance</p>
	</div>

	<!-- Episodes Grid -->
	<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
		{#each episodes as episode}
			<Card class="hover:shadow-lg transition-shadow duration-200">
				<CardHeader>
					<div class="flex items-start justify-between">
						<CardTitle class="text-lg">{episode.title}</CardTitle>
						<Badge class={getStatusColor(episode.status)}>
							{episode.status}
						</Badge>
					</div>
					<CardDescription>{episode.description}</CardDescription>
				</CardHeader>
				<CardContent>
					<div class="space-y-3">
						<div class="flex justify-between text-sm text-gray-600 dark:text-gray-400">
							<span>Publish Date:</span>
							<span>{formatDate(episode.publishDate)}</span>
						</div>
						<div class="flex justify-between text-sm text-gray-600 dark:text-gray-400">
							<span>Duration:</span>
							<span>{episode.duration}</span>
						</div>
						{#if episode.status === 'published'}
							<div class="flex justify-between text-sm text-gray-600 dark:text-gray-400">
								<span>Downloads:</span>
								<span>{episode.downloads.toLocaleString()}</span>
							</div>
						{/if}
						<div class="pt-3">
							{#if episode.status === 'published'}
								<button class="w-full bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200">
									View Analytics
								</button>
							{:else}
								<button class="w-full bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200">
									Edit Episode
								</button>
							{/if}
						</div>
					</div>
				</CardContent>
			</Card>
		{/each}
	</div>

	<!-- Add New Episode Button -->
	<div class="mt-8 text-center">
		<button class="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-md font-medium transition-colors duration-200">
			+ Create New Episode
		</button>
	</div>
</div>
