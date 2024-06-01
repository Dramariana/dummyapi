Feature: Testing User Controller API

  Scenario: Get list of users
    When the user requests to get list of users
    Then the response status code should be 200
    And the response should contain a list of users

  Scenario Outline: Create a new user
    When the user requests to create a new user with username "<userName>", email "<email>", firstName "<firstName>" and lastName "<lastName>"
    Then the response status code should be <statusCode>
    And the response should contain the newly created user information email "<email>", firstName "<firstName>" and lastName "<lastName>"
    Examples:
      | userName    | email             | firstName | lastName       | statusCode |
      | exampleUser | user15@example.com | prueba    | pruebaLastname | 200        |

  Scenario: Get user by ID
    When the user requests to get user details by ID
    Then the response status code should be 200
    And the response should contain the user details with the provided ID

  #Scenario: Update user by ID
   # When the user requests to update user details with new email "new_email@example.com" for the provided ID
    #Then the response status code should be 200
    #And the response should confirm the user details have been updated new email "new_email@example.com"

  Scenario: Delete user by ID
    When the user requests to delete user with the provided ID
    Then the response status code should be 200
    And the user should no longer exist in the system