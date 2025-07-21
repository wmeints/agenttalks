import SubmissionsTable from '@/components/submissions/submissions-table'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/submissions/')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <div className="container mx-auto">
      <SubmissionsTable />
    </div>
  )
}
