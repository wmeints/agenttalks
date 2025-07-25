import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { Route } from './index'

describe('Submissions Page Route', () => {
  it('renders the submissions page with title and table', () => {
    const SubmissionsComponent = Route.component as React.ComponentType
    render(<SubmissionsComponent />)

    expect(screen.getByText('Content Submissions')).toBeInTheDocument()
    expect(screen.getByTestId('submissions-table')).toBeInTheDocument()
  })

  it('applies correct styling', () => {
    const SubmissionsComponent = Route.component as React.ComponentType
    const { container } = render(<SubmissionsComponent />)

    const mainDiv = container.firstChild as HTMLElement
    expect(mainDiv).toHaveClass('container', 'mx-auto')
    
    const title = screen.getByText('Content Submissions')
    expect(title).toHaveClass('text-3xl', 'font-bold', 'mb-6')
  })
})