<script lang="ts">
	import '../app.css';
	import Navigation from '$lib/components/Navigation.svelte';
	
	let { children } = $props();
</script>

<div class="min-h-screen bg-gray-50 dark:bg-gray-900">
	<Navigation />
	<main class="flex-1">
		{@render children()}
	</main>
</div>
