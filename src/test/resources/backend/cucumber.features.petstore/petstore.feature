Feature: Selling a Pet

  Scenario Outline: User adds a pet
    When the user adds a new pet with <Name>, <Pet> and <Status>
    Then the pet <Name> exist
    Examples:
      | Name      | Pet   | Status   |
      | "Ralf"    |"Dog"  | "Available"|
#      | "Cati"       |"Cat"    |"Available"|


#    When the user adds a new pet with <Name>, <Pet> and <Status>
#    And the user updates a pet status to "available"
#    And the user deletes a pet
#    Then the pet is deleted
#
#    When the user gets all pets with "available" status
#    Then the pet status is {string}


#  Scenario: User updates a pet
#    Given there is a pet named "scruffy" type "dog" status "available"
#    When the user updates the pet "scruffy" status to "sold"
#    Then the pet "scruffy" status is "sold"
#
#  Scenario: User deletes a pet
#    Given there is a pet named "scruffy" type "dog" status "available"
#    When the user deletes the pet "scruffy"
#    Then the pet "scruffy" do not exists
#
#  Scenario: User gets available pets
#    Given there is a pet named "scruffy" type "dog" status "available"
#    Then there are "available" pets









