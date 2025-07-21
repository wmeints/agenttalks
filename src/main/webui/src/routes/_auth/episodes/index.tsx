import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/episodes/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/_auth/episodes/"!</div>
}
