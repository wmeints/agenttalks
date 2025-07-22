Feature: Content submissions
  As a podcast content contributor
  I want to submit interesting articles and resources
  So that they can be included in the weekly AI podcast

  Content submissions allow authenticated users to submit URLs to articles, blog posts,
  and other online resources related to AI and technology. The system automatically
  processes these submissions by downloading the content, generating AI-powered summaries,
  and making them available for inclusion in weekly podcast episodes.

  Business Rules:
  - Users must be authenticated to submit content
  - A valid URL is required for content submission
  - Instructions are optional but can help guide the AI summarization
  - Submissions are processed asynchronously through an event-driven workflow
  - Content goes through the following states: SUBMITTED -> SUMMARIZED -> PROCESSING -> PROCESSED

  Background:
    Given I am logged in as a content contributor

  Scenario Outline: Submit content to the podcast
    When I submit content with URL "<url>" and instructions "<instructions>"
    Then the content submission should be "<result>"
    And the status of the content submission is "<status>"

    Examples:
      | url                                   | instructions                        | result     | status    |
      | https://example.com/ai-breakthrough   |                                     | successful | SUBMITTED |
      | https://example.com/ml-research       | Focus on the practical applications | successful | SUBMITTED |
      | https://openai.com/blog/latest-update | Summarize key technical details     | successful | SUBMITTED |
      |                                       | This should fail without URL        | failed     |           |
      | invalid-url                           | This URL format is invalid          | failed     |           |

Scenario: Retrieve content submissions
    Given the following content submissions exist:
      
      | url                                   | instructions                        | status    |
      | https://example.com/ai-breakthrough   |                                     | SUBMITTED |
      | https://example.com/ml-research       | Focus on the practical applications | SUBMITTED |
      | https://openai.com/blog/latest-update | Summarize key technical details     | SUBMITTED |

    When I request the list of content submissions
    Then I should receive 3 content submissions
    And the submissions should include:
    
      | url                                   | instructions                        | status    |
      | https://example.com/ai-breakthrough   |                                     | SUBMITTED |
      | https://example.com/ml-research       | Focus on the practical applications | SUBMITTED |
      | https://openai.com/blog/latest-update | Summarize key technical details     | SUBMITTED |