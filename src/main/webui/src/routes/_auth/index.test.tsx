import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { useQuery } from '@apollo/client'
import { Route } from './index'

vi.mocked(useQuery)

type MockQueryResult = {
  data: unknown
  loading: boolean
  error: unknown
}

describe('Index Page Route', () => {
  it('renders loading card when data is loading', () => {
    vi.mocked(useQuery).mockReturnValue({
      data: null,
      loading: true,
      error: null,
    } as MockQueryResult)
    
    const IndexComponent = Route.component as React.ComponentType
    render(<IndexComponent />)

    expect(screen.getByTestId('loading-card')).toBeInTheDocument()
    expect(screen.queryByTestId('error-card')).not.toBeInTheDocument()
  })

  it('renders error card when there is an error', () => {
    vi.mocked(useQuery).mockReturnValue({
      data: null,
      loading: false,
      error: new Error('Network error'),
    } as MockQueryResult)
    
    const IndexComponent = Route.component as React.ComponentType
    render(<IndexComponent />)

    expect(screen.getByTestId('error-card')).toBeInTheDocument()
    expect(screen.queryByTestId('loading-card')).not.toBeInTheDocument()
  })

  it('renders dashboard content when data is loaded successfully', () => {
    vi.mocked(useQuery).mockReturnValue({
      data: {
        statistics: {
          pendingSubmissions: 5,
          totalEpisodes: 10,
        },
      },
      loading: false,
      error: null,
    } as MockQueryResult)
    
    const IndexComponent = Route.component as React.ComponentType
    render(<IndexComponent />)

    expect(screen.getAllByTestId('metric-card')).toHaveLength(2)
    expect(screen.getByTestId('pending-submissions-table')).toBeInTheDocument()
    expect(screen.getByText('Pending submissions')).toBeInTheDocument()
  })

  it('applies correct container styling', () => {
    vi.mocked(useQuery).mockReturnValue({
      data: { statistics: { pendingSubmissions: 0, totalEpisodes: 0 } },
      loading: false,
      error: null,
    } as MockQueryResult)
    
    const IndexComponent = Route.component as React.ComponentType
    const { container } = render(<IndexComponent />)

    const mainDiv = container.firstChild as HTMLElement
    expect(mainDiv).toHaveClass('container', 'mx-auto', 'space-y-6', 'mb-6')
  })
})