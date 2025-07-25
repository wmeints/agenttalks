import SubmissionsTable from '@/components/submissions/submissions-table'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/submissions/')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <div className="container mx-auto">
      <h1 className="text-3xl font-bold mb-6">Content Submissions</h1>
      <SubmissionsTable />
    </div>
  )
}
