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
			publishDate: new Date('2025-06-26')
		},
		{
			id: 2,
			title: "Tech News Weekly #46",
			description: "Economic trends and renewable energy breakthroughs",
			status: "published",
			publishDate: new Date('2025-06-19')
		},
		{
			id: 3,
			title: "Tech News Weekly #45",
			description: "Medical AI and healthcare innovation spotlight",
			status: "published",
			publishDate: new Date('2025-06-12')
		},
		{
			id: 4,
			title: "Tech News Weekly #48",
			description: "Upcoming episode - Financial markets and tech policy",
			status: "draft",
			publishDate: new Date('2025-07-03')
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
	<title>Podcast Episodes - Agenttalks Dashboard</title>
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
					<CardTitle class="text-lg">{episode.title}</CardTitle>
					<CardDescription>{episode.description}</CardDescription>
				</CardHeader>
				<CardContent>
					<div class="space-y-3">
						<div class="flex justify-between text-sm text-gray-600 dark:text-gray-400">
							<span>Publish Date:</span>
							<span>{formatDate(episode.publishDate)}</span>
						</div>
					</div>
				</CardContent>
			</Card>
		{/each}
	</div>
</div>
