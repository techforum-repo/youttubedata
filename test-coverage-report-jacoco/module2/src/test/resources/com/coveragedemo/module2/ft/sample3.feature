Feature: Cucumber Jacoco Coverage sample

 Scenario: Receive 60
    Given whenever a value is 50
    When getValue method of Sample3.java is called
    Then It should return 60
 
 Scenario: Receive 100
    Given whenever a value is 90
    When getValue method of Sample3.java is called
    Then It should return 100
