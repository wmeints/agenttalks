<script lang="ts">
interface Props {
    pageIndex: number;
    totalItems: number;
    previousPage: () => Promise<void>;
    nextPage: () => Promise<void>;
}

let {totalItems, pageIndex, previousPage, nextPage } : Props = $props();

</script>

<div class="mt-6 pt-4 border-t">
    <div class="flex items-center justify-between">
        <div class="flex items-center space-x-2">
            {#if pageIndex > 0}
                <button
                    onclick={previousPage}
                    class="px-3 py-1 text-xs border rounded-md hover:bg-muted/50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    Previous
                </button>
            {/if}
            {#if totalItems > 0}
                <button
                    onclick={nextPage}
                    class="px-3 py-1 text-xs border rounded-md hover:bg-muted/50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    Next
                </button>
            {/if}
        </div>
    </div>
</div>
