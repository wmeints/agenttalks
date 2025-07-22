Feature: Podcast Episodes
  As a logged-in user
  I want to retrieve podcast episodes
  So that I can view available content

  Background:
    Given I am logged in

  Scenario: Retrieve episodes
    Given there are 3 episodes in the database:

      | episode_number | title                                 |
      |              1 | AI News Weekly - Episode 1            |
      |              2 | Machine Learning Updates              |
      |              3 | The Future of Artificial Intelligence |
      
    When I retrieve the episodes
    Then I should get all 3 episodes with the following details:

      | episode_number | title                                 |
      |              1 | AI News Weekly - Episode 1            |
      |              2 | Machine Learning Updates              |
      |              3 | The Future of Artificial Intelligence |
