<script lang="ts">
    import {
        Card,
        CardContent,
        CardDescription,
        CardHeader,
        CardTitle,
    } from "$lib/components/ui/card";
    import { formatDate } from "@/date-utils";

    type Episode = {
        title: string;
        description: string;
        dateCreated: Date;
    };

    interface Props {
        episode: Episode;
    }

    let { episode }: Props = $props();
</script>

<Card class="hover:shadow-lg transition-shadow duration-200">
    <CardHeader>
        <CardTitle class="text-lg">{episode.title}</CardTitle>
        <CardDescription>{episode.description}</CardDescription>
    </CardHeader>
    <CardContent>
        <div class="space-y-3">
            <div
                class="flex justify-between text-sm text-gray-600 dark:text-gray-400"
            >
                <span>Publish Date:</span>
                <span>{formatDate(episode.dateCreated)}</span>
            </div>
        </div>
    </CardContent>
</Card>
