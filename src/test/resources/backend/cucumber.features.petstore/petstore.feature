Feature: Selling a Pet

  Scenario Outline: User adds a pet
    When the user adds a new pet with <Id>, <Name>, <Pet> and <Status>
    Then the pet <Name> exist
    And the pet exists at database with ID <Id>
    Examples:
      | Id    | Name      | Pet   | Status      |
      | 666   |"Ralf"    |"Dog"  | "available"  |

  Scenario: User updates a pet
    When the user updates the pet with id 666 status to "sold"
    Then the response is the updated id 666

  Scenario: User deletes a pet
    When the user deletes the pet with id 666
    Then the pet with id 666 do not exists

  Scenario Outline: User gets available pets
    When there are pets with <status>
    Then the result is not empty
    And all results have the same property value <status>
    Examples:
      | status      |
      | "available" |


#    When the user adds a new pet with <Name>, <Pet> and <Status>
#    And the user updates a pet status to "available"
#    And the user deletes a pet
#    Then the pet is deleted
#
#    When the user gets all pets with "available" status
#    Then the pet status is {string}