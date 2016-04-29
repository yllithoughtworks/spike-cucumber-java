Feature: Idendity Service
  Scenario: fetch Users
    Given In case there are totally N users
    Given There is NO following users:
      | email                |
      | GaugeTest1@localhost |
      | GaugeTest2@localhost |
    Given In case there are totally N users
    When Create following test users:
      | email                | name       |
      | GaugeTest1@localhost | GaugeTest1 |
      | GaugeTest2@localhost | GaugeTest2 |
    Then There should have N plus 2 users
    Then User GaugeTest2@localhost can be found with name GaugeTest2
    When Delete a user GaugeTest2@localhost
    Then There should have N plus 1 users
    Then User GaugeTest2@localhost can NOT be found
