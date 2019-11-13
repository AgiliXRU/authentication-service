Feature: User authorization

  Scenario: Checking phone number by REST-protocol
    Given I use REST-service on address "http://localhost:8080" with version "1"
    And I want to send POST-request "/v1/auth/checkPhoneNumber"
    When I sending phoneNumber with value "+79991234567"
    Then I receive in JSON body

  Scenario: Setting PIN-code by REST-protocol
    Given I use REST-service on address "http://localhost:8080" with version "1"
    And I want to send POST-request "/v1/auth/setPinCode"
    When I sending phoneNumber with value "+79991234567" and PIN-code "1111"
    Then I receive in JSON body