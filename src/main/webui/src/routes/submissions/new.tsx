import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/submissions/new')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/submissions/new"!</div>
}
