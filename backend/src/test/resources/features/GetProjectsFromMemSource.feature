Feature: Get projects
  Geeting the projects associated with a specific user configuration

  @Ready
  Scenario: 1 project retrieved
    Given Current configuration has the username: ValentinManea and password: Parola12345-
    When I'm asking for my projects
    Then I should receive exactly 1 project