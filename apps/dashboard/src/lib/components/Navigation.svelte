<script lang="ts">
	import { page } from '$app/stores';
    import { SignIn, SignOut } from '@auth/sveltekit/components';
	import { Menu, X, User, LogOut, LogIn } from 'lucide-svelte';
	import type { Session } from '@auth/sveltekit';
	
	let { session }: { session: Session | null } = $props();
	
	let mobileMenuOpen = $state(false);
	
	// Derive isLoggedIn from session
	let isLoggedIn = $derived(!!session);
	
	function toggleMobileMenu() {
		mobileMenuOpen = !mobileMenuOpen;
	}
	

	
	// Close mobile menu when route changes
	$effect(() => {
		if ($page.url) {
			mobileMenuOpen = false;
		}
	});
</script>

<nav class="bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-700 sticky top-0 z-50">
	<div class="container mx-auto px-4 sm:px-6 lg:px-8">
		<div class="flex justify-between h-16">
			<!-- Logo/Brand -->
			<div class="flex items-center">
				<a href="/" class="flex-shrink-0 flex items-center">
					<span class="text-xl font-bold text-gray-900 dark:text-white">
						Newscast Dashboard
					</span>
				</a>
			</div>

			<!-- Desktop Navigation -->
			<div class="hidden md:flex md:items-center md:space-x-8">
				<a 
					href="/episodes" 
					class="text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 px-3 py-2 rounded-md text-sm font-medium transition-colors duration-200"
					class:text-blue-600={$page.url.pathname === '/episodes'}
					class:dark:text-blue-400={$page.url.pathname === '/episodes'}
				>
					Podcast Episodes
				</a>
				<a 
					href="/content" 
					class="text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 px-3 py-2 rounded-md text-sm font-medium transition-colors duration-200"
					class:text-blue-600={$page.url.pathname === '/content'}
					class:dark:text-blue-400={$page.url.pathname === '/content'}
				>
					Content Items
				</a>
			</div>

			<!-- Desktop Auth -->
			<div class="hidden md:flex md:items-center md:space-x-4">
				{#if isLoggedIn}
					<div class="flex items-center space-x-3">
						<a
							href="/submit"
							class="flex items-center space-x-1 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200"
						>
							<span>Submit Content</span>
						</a>

						<SignOut>
							<div slot="submitButton" class="flex items-center space-x-1 text-gray-700 dark:text-gray-300 hover:text-red-600 dark:hover:text-red-400 px-3 py-2 rounded-md text-sm font-medium transition-colors duration-200">
								<LogOut size={16} />
								<span>Logout</span>
							</div>
						</SignOut>
					</div>
				{:else}	
				<SignIn>
					<div slot="submitButton" class="flex items-center space-x-1 text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 px-3 py-2 rounded-md text-sm font-medium transition-colors duration-200">
						<LogIn size={16} />
						<span>Login</span>
					</div>
				</SignIn>
				{/if}
			</div>

			<!-- Mobile menu button -->
			<div class="md:hidden flex items-center">
				<button
					onclick={toggleMobileMenu}
					class="inline-flex items-center justify-center p-2 rounded-md text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-gray-100 dark:hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-blue-500"
					aria-expanded={mobileMenuOpen}
				>
					<span class="sr-only">Open main menu</span>
					{#if mobileMenuOpen}
						<X size={24} />
					{:else}
						<Menu size={24} />
					{/if}
				</button>
			</div>
		</div>
	</div>

	<!-- Mobile menu -->
	{#if mobileMenuOpen}
		<div class="md:hidden">
			<div class="px-2 pt-2 pb-3 space-y-1 sm:px-3 bg-white dark:bg-gray-900 border-t border-gray-200 dark:border-gray-700">
				<a
					href="/episodes"
					class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors duration-200"
					class:text-blue-600={$page.url.pathname === '/episodes'}
					class:bg-blue-50={$page.url.pathname === '/episodes'}
				>
					Podcast Episodes
				</a>
				<a
					href="/content"
					class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors duration-200"
					class:text-blue-600={$page.url.pathname === '/content'}
					class:bg-blue-50={$page.url.pathname === '/content'}
				>
					Content Items
				</a>
				<a
					href="/submit"
					class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors duration-200"
					class:text-blue-600={$page.url.pathname === '/submit'}
					class:bg-blue-50={$page.url.pathname === '/submit'}
				>
					Submit Content
				</a>
			</div>
			
			<!-- Mobile Auth -->
			<div class="px-2 py-3 border-t border-gray-200 dark:border-gray-700">
				{#if isLoggedIn}
					<SignOut>
						<div slot="submitButton" class="flex items-center justify-center space-x-2 w-full text-gray-700 dark:text-gray-300 hover:text-red-600 dark:hover:text-red-400 hover:bg-gray-100 dark:hover:bg-gray-800 px-3 py-2 rounded-md text-base font-medium transition-colors duration-200">
							<LogOut size={16} />
							<span>Logout</span>
						</div>
					</SignOut>
				{:else}
					<SignIn>
						<div slot="submitButton" class="flex items-center justify-center space-x-2 w-full text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-gray-100 dark:hover:bg-gray-800 px-3 py-2 rounded-md text-base font-medium transition-colors duration-200">
							<LogIn size={16} />
							<span>Login</span>
						</div>
					</SignIn>
				{/if}
			</div>
		</div>
	{/if}
</nav>
