import { Card, CardContent } from "@/components/ui/card";
import { z } from 'zod'
import { createFileRoute } from "@tanstack/react-router";
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { graphql } from "@/gql";
import { useMutation } from "@apollo/client";
import SubmitContentHelp from "@/components/submissions/submit-content-help";
import { toast } from "sonner";
export const Route = createFileRoute("/_auth/submissions/new")({
  component: RouteComponent,
});

const submitContentMutation = graphql(`
  mutation submitContentMutation ($url: String!, $instructions: String!) {
    submitContent(input: {url: $url, instructions: $instructions }) {
      id
    }
  }`);

const formSchema = z.object({
  url: z.string().nonempty(),
  instructions: z.string()
});

function RouteComponent() {
  const [mutationFunction] = useMutation(submitContentMutation);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      url: '',
      instructions: ''
    }
  })

  async function onSubmit(values: z.infer<typeof formSchema>) {
    await mutationFunction({
      variables: {
        url: values.url,
        instructions: values.instructions
      }
    })

    toast('Content succesfully submitted', {
      description: "Thanks for submitting your content. We're processing it right now for the next podcast. This process may take up to a minute",
      duration: 30
    });
  }

  return <div className="max-w-2xl mx-auto">
    <div className="mb-8">
      <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-4">Submit content for the podcast</h1>
      <p className="text-gray-600 dakr:text-gray-400">
        Provide the URL to the content and optionally custom isntructions explaining what
        is important about the content. We'll automatically process the content for the next
        podcast.
      </p>
    </div>
    <Card>
      <CardContent>
        <Form {...form} >
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            <FormField
              control={form.control}
              name="url"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>URL</FormLabel>
                  <FormControl>
                    <Input placeholder="http://example.org/my-article" {...field} />
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="instructions"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Custom instructions</FormLabel>
                  <FormControl>
                    <Textarea placeholder="Enter your custom instructions..." {...field} />
                  </FormControl>
                </FormItem>
              )}
            />
            <Button type="submit" disabled={form.formState.isSubmitting}>Submit</Button>
          </form>
        </Form>
      </CardContent>
    </Card>
    <SubmitContentHelp />
  </div>
}
