---
layout: page
title: fall9x's Project Portfolio Page
---

## Project: ChopChop

ChopChop is a food recipe and ingredient inventory management system, which aims to make it easier for people to manage their recipes and ingredients in an easy and effective manner. It features a keyboard-driven command interface and a GUI written in JavaFX.

A summary of code contributions can be found here: [reposense](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=fall9x).

### New Features

#### Implemented and developed ChopChop's mouse-friendly GUI

This feature was developed to replace AB3's dark themed, single pane UI; as a cooking app, a cheerier theme seemed to be more appropriate. This GUI implementation also takes into the account the user's experience when they are cooking — redundant information should not be shown. Hence, I decided to implement different panels for each of ChopChop's models rather fitting everything into a single pane in AB3-like applications. The GUI is also mouse-friendly to improve the ease of using the app while cooking.

The implementation for this feature required rather deep understanding of the interactions between JavaFx objects due to the dynamic nature of the application — users can choose to switch panes through both mouse and cli input. Coming up with the colour scheme and design of the application was also rather complicated as it the colors had to mesh well together into a aesthetically pleasing cooking app.

PRs: [#47](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/47), [#94](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/94), [#95](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/95), [#99](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/99), [#109](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/109), [#128](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/128), [#150](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/150), [#153](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/153), [#177](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/177), [#185](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/185).



#### View Command

ChopChop's `view command` was created give fast typists the option of viewing their selected recipes using cli instead of through mouse input. The command opens our recipe display panel instead of simply dumping the output string into the command output box.

This feature was designed to improve the navigability of the different panels. It supports both arguments in the form of recipe names and arguments in the form of recipe index based on the current recipe panel display. 

PRs: [#127](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/127).



### Other Contributions

#### Project Management
Kept track of deadlines for project milestones, updated Gradle dependencies and GitHub workflow to encompass all tests.

#### Enhancements to Existing Features

1. Increased code coverage by writing a series of GUI and non-GUI tests ([#178](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/178), [#189](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/189), [#190](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/190), [#261](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/261)).



#### Documentation
**User Guide**

Added detailed walkthrough for the GUI ([#175](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/175), [#197](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/197), [#279](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/279)).

**Developer Guide**

Primary custodian for the Developer Guide, ensured consistent formatting, phrasing, and visuals. Added user stories, use cases, parts of the instructions for manual testing and implementation details for the UI ([#175](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/175), [#296](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/296), [#300](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/300), [#303](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/303), [#319](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/319), [#322](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/322), [#324](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/324), [#326](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/326), [#329](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/329)).




#### Community

Reviewed pull requests.

Contributed bug reports for other teams during PE-D, and responded quickly to any queries after PE-D: [ped repo](https://github.com/fall9x/ped/issues)
