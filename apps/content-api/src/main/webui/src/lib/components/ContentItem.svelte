<script lang="ts">
	import { Badge } from "$lib/components/ui/badge";
	import { getRelativeDate } from "@/date-utils";
	import { truncateText } from "@/text-utils";
    
	type ContentItemData = {
		id: number;
		title?: string | null;
		url: string;
		summary?: string | null;
		dateCreated?: Date;
		status?: string;
	};

    interface Props {
        item: ContentItemData;
    }

	let { item }: Props = $props();

	function getStatusColor(status: string) {
		switch (status) {
			case "processed":
				return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300";
			case "pending":
				return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300";
			case "archived":
				return "bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300";
			default:
				return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300";
		}
	}
</script>

<div class="p-4 border rounded-lg hover:bg-muted/50 transition-colors">
	<div class="space-y-3">
		<!-- Title and URL -->
		<div class="flex items-start justify-between">
			<h4 class="font-medium text-sm leading-relaxed flex-1 pr-4">
				{item.title || item.url}
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
		<p class="text-sm text-muted-foreground leading-relaxed">
			{truncateText(item.summary || "")}
		</p>

		<!-- Metadata row -->
		<div class="flex items-center justify-between text-xs text-muted-foreground">
			<div class="flex items-center space-x-3">
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
					<span>{getRelativeDate(item.dateCreated || new Date())}</span>
				</span>
			</div>
			<div class="flex items-center space-x-2">
				<Badge class={getStatusColor(item.status || "submitted")}>
					{item.status || "submitted"}
				</Badge>
			</div>
		</div>
	</div>
</div>
