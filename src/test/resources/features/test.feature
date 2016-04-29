Feature: CucumberTest

  Scenario: Let's try it
    Given I have 10 cukes
    When I eat 1 cukes
    Then There are 9 cukes left
  Scenario: eat 2 times
    Given I have 10 cukes
    When I eat 2 cukes
    When I eat 2 cukes
    Then There are 6 cukes left
