import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { Route } from './index'

describe('Episodes Page Route', () => {
  it('renders the episodes page with title and table', () => {
    const EpisodesComponent = Route.component as React.ComponentType
    render(<EpisodesComponent />)

    expect(screen.getByText('Podcast Episodes')).toBeInTheDocument()
    expect(screen.getByTestId('episodes-table')).toBeInTheDocument()
  })

  it('applies correct styling', () => {
    const EpisodesComponent = Route.component as React.ComponentType
    const { container } = render(<EpisodesComponent />)

    const mainDiv = container.firstChild as HTMLElement
    expect(mainDiv).toHaveClass('container', 'mx-auto')
    
    const title = screen.getByText('Podcast Episodes')
    expect(title).toHaveClass('text-3xl', 'font-bold', 'mb-6')
  })
})