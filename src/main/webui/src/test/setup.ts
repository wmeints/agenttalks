import '@testing-library/jest-dom'
import { vi } from 'vitest'
import React from 'react'

// Mock Clerk
vi.mock('@clerk/clerk-react', () => ({
  useAuth: vi.fn(() => ({ isSignedIn: true })),
  RedirectToSignIn: vi.fn(() => React.createElement('div', { 'data-testid': 'redirect-to-signin' }, 'Please sign in')),
}))

// Mock TanStack Router
vi.mock('@tanstack/react-router', async () => {
  const actual = await vi.importActual('@tanstack/react-router')
  return {
    ...actual,
    createRootRoute: vi.fn((config) => ({
      ...config,
      component: config.component,
    })),
    createFileRoute: vi.fn((path) => (config) => ({
      ...config,
      path,
      component: config.component,
    })),
    Outlet: vi.fn(() => React.createElement('div', { 'data-testid': 'outlet' }, 'Outlet')),
  }
})

// Mock TanStack Router Devtools
vi.mock('@tanstack/react-router-devtools', () => ({
  TanStackRouterDevtools: vi.fn(() => React.createElement('div', { 'data-testid': 'devtools' }, 'Devtools')),
}))

// Mock Sonner toast
vi.mock('@/components/ui/sonner', () => ({
  Toaster: vi.fn(() => React.createElement('div', { 'data-testid': 'toaster' }, 'Toaster')),
}))

// Mock NavigationBar
vi.mock('@/components/navigation-bar', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'navigation-bar' }, 'Navigation')),
}))

// Mock Apollo Client
vi.mock('@apollo/client', () => ({
  useQuery: vi.fn(),
  useMutation: vi.fn(),
  gql: vi.fn(),
}))

// Mock components
vi.mock('@/components/cards/error-card', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'error-card' }, 'Error Card')),
}))

vi.mock('@/components/cards/loading-card', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'loading-card' }, 'Loading Card')),
}))

vi.mock('@/components/cards/metric-card', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'metric-card' }, 'Metric Card')),
}))

vi.mock('@/components/submissions/pending-submissions-table', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'pending-submissions-table' }, 'Pending Submissions Table')),
}))

vi.mock('@/components/episodes/episodes-table', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'episodes-table' }, 'Episodes Table')),
}))

vi.mock('@/components/submissions/submissions-table', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'submissions-table' }, 'Submissions Table')),
}))

vi.mock('@/components/submissions/submit-content-help', () => ({
  default: vi.fn(() => React.createElement('div', { 'data-testid': 'submit-content-help' }, 'Submit Content Help')),
}))

// Mock sonner toast
vi.mock('sonner', () => ({
  toast: {
    promise: vi.fn(),
  },
}))