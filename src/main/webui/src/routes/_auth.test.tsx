import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { useAuth } from '@clerk/clerk-react'
import { Route } from './_auth'

vi.mocked(useAuth)

describe('Auth Route', () => {
  it('renders outlet when user is signed in', () => {
    vi.mocked(useAuth).mockReturnValue({ isSignedIn: true })
    
    const AuthComponent = Route.component as React.ComponentType
    render(<AuthComponent />)

    expect(screen.getByTestId('outlet')).toBeInTheDocument()
    expect(screen.queryByTestId('redirect-to-signin')).not.toBeInTheDocument()
  })

  it('renders redirect to sign in when user is not signed in', () => {
    vi.mocked(useAuth).mockReturnValue({ isSignedIn: false })
    
    const AuthComponent = Route.component as React.ComponentType
    render(<AuthComponent />)

    expect(screen.getByTestId('redirect-to-signin')).toBeInTheDocument()
    expect(screen.queryByTestId('outlet')).not.toBeInTheDocument()
  })

  it('calls useAuth hook', () => {
    vi.mocked(useAuth).mockReturnValue({ isSignedIn: true })
    
    const AuthComponent = Route.component as React.ComponentType
    render(<AuthComponent />)

    expect(useAuth).toHaveBeenCalled()
  })
})