Feature: Get projects
  Optional description of the feature

  @Ready
  Scenario: 1 project retrieved
    Given I'm authenticated with username: ValentinManea and password: Parola12345-
    When I'm asking for my projects
    Then I should receive exactly 1 project