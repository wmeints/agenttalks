import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { Route } from './__root'

describe('Root Route', () => {
  it('renders the root component with navigation and outlet', () => {
    const RootComponent = Route.component as React.ComponentType
    render(<RootComponent />)

    expect(screen.getByTestId('navigation-bar')).toBeInTheDocument()
    expect(screen.getByTestId('outlet')).toBeInTheDocument()
    expect(screen.getByTestId('devtools')).toBeInTheDocument()
    expect(screen.getByTestId('toaster')).toBeInTheDocument()
  })

  it('applies correct container styling', () => {
    const RootComponent = Route.component as React.ComponentType
    render(<RootComponent />)

    const container = screen.getByTestId('outlet').parentElement
    expect(container).toHaveClass('container', 'mx-auto', 'mt-6')
  })
})