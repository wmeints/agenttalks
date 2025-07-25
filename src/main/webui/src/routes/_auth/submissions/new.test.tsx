import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { useMutation } from '@apollo/client'
import { toast } from 'sonner'
import { Route } from './new'

vi.mocked(useMutation)
vi.mocked(toast.promise)

describe('New Submission Page Route', () => {
  const mockMutation = vi.fn()

  beforeEach(() => {
    vi.mocked(useMutation).mockReturnValue([mockMutation, { loading: false }] as [typeof mockMutation, { loading: boolean }])
    mockMutation.mockClear()
    vi.mocked(toast.promise).mockClear()
  })

  it('renders the form with all required fields', () => {
    const NewSubmissionComponent = Route.component as React.ComponentType
    render(<NewSubmissionComponent />)

    expect(screen.getByText('Submit content for the podcast')).toBeInTheDocument()
    expect(screen.getByLabelText('URL')).toBeInTheDocument()
    expect(screen.getByLabelText('Custom instructions')).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'Submit' })).toBeInTheDocument()
    expect(screen.getByTestId('submit-content-help')).toBeInTheDocument()
  })

  it('submits form with correct data', async () => {
    const user = userEvent.setup()
    const NewSubmissionComponent = Route.component as React.ComponentType
    render(<NewSubmissionComponent />)

    const urlInput = screen.getByLabelText('URL')
    const instructionsInput = screen.getByLabelText('Custom instructions')
    const submitButton = screen.getByRole('button', { name: 'Submit' })

    await user.type(urlInput, 'https://example.com/article')
    await user.type(instructionsInput, 'This is important content')
    await user.click(submitButton)

    expect(mockMutation).toHaveBeenCalledWith({
      variables: {
        url: 'https://example.com/article',
        instructions: 'This is important content',
      },
    })
    expect(toast.promise).toHaveBeenCalled()
  })

  it('applies correct styling to container', () => {
    const NewSubmissionComponent = Route.component as React.ComponentType
    const { container } = render(<NewSubmissionComponent />)

    const mainDiv = container.firstChild as HTMLElement
    expect(mainDiv).toHaveClass('max-w-2xl', 'mx-auto')
  })

  it('renders form placeholder text correctly', () => {
    const NewSubmissionComponent = Route.component as React.ComponentType
    render(<NewSubmissionComponent />)

    expect(screen.getByPlaceholderText('http://example.org/my-article')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Enter your custom instructions...')).toBeInTheDocument()
  })
})