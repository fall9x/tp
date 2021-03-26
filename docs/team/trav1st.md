---
layout: page
title: Travis Toh's Project Portfolio Page
---

## Project: ChopChop

ChopChop is a food recipe and ingredient inventory management system, which aims to make it easier for people to manage their recipes and ingredients in an easy and effective manner. It features a keyboard-driven command interface and a GUI written in JavaFX.

A summary of code contributions can be found here: [reposense](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=trav1st).

### New Features

#### Statistics

This feature saves persistent records of the recipes made and the ingredients consumed in the making of the recipes. The user can access these records with the commands described below. Relevant information are displayed in a Statistic panel on the bottom right hand side of ChopChop. The development of this feature followed a depth-first approach and it required changes to all major components of ChopChop.

PR: [#130](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/130).

#### Statistics commands

##### View top recipes command

Shows the recipes that were made the most number of times.

##### View recent recipes/ingredients commands

Shows the 10 most recently made recipes / recently used ingredients. It is capped at 10 to keep the list short and concise. If the user would like to view records that are even older (i.e. View the previous items that are not in this list of 10), the user can use view recipes made/ view ingredients used commands and specify the exact time frame.

##### View recipes/ingredients that were made/used commands

Shows the recipes made and ingredients used in the given time frame. If the user does not specify any time frame (i.e. `/before` and `/after` tags are missing in user's input), we assumed that the user wants to view recipes made / ingredients used on the day itself.

##### Clear recipes/ingredient usage records commands

Sometimes, the usage history can get too long and cluttered or the user simply wanted their history to be cleared for privacy reasons. Clear commands delete all saved records.It is an undoable command, so if the user deletes by accident the deleted data can be recovered by executing the `undo` command.


### Other Contributions

#### Project management

Contributed in ensuring the tests are sufficient and updated regularly.

#### Enhancements to Existing Features

1. Improved the functionality of CommandResult to account for outputs from Statistics commands ([#165](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/165)).

2. Added supports for adding 'Tags' to Ingredient ([#97](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/97)).

#### Documentation
**User Guide**

Author for the Statistic commands section of User Guide. Added detailed examples for the statistics commands ([StatisticsCommands](https://ay2021s1-cs2103t-t10-3.github.io/tp/UserGuide.html#56statistics-commands)).

**Developer Guide**

Ensured that the UML diagrams are updated and accurate. Added implementation details for the statistics feature ([StatisticsFeature](https://ay2021s1-cs2103t-t10-3.github.io/tp/DeveloperGuide.html#45statistics-feature)).


#### Community

Contributed detailed bug reports for other teams during PE-D: [ped repo](https://github.com/trav1st/ped/issues)
