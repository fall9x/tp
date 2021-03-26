---
layout: page
title: zhiayang's Project Portfolio Page
---

## Project: ChopChop

ChopChop is a food recipe and ingredient inventory management system, which aims to make it easier for people to manage their recipes and ingredients in an easy and effective manner. It features a keyboard-driven command interface and a GUI written in JavaFX.

A summary of code contributions can be found here: [reposense](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=zhiayang).

### New Features

#### Tab Completion

This feature allows the user to press <kbd>tab</kbd> to complete the currently typed command, increasing the ease-of-use even for users not accustomed to command-line interfaces, and greatly reduces the tedium of typing commands compared to existing AB3-like applications.

The implementation of this feature was not entirely straightforward, as it needs to be written as a rather-complex state machine depending on the current state of the user input, the cursor position, and the parsed command.

PRs: [#117](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/117), [#142](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/142), [#149](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/149), [#159](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/159).

#### Edit Command Parsing

Due to the nature of recipes, editing them was not as simple as replacing fields in a Person like in AB3; recipes contain an (unordered) list of ingredients and an (ordered) list of steps, so it was important for the edit functionality to be flexible in accommodating the users' needs.

This feature required a significant extension of the existing command parser to support 'edit-arguments' which are regular argument names with extra components (eg. `/step:edit:3` instead of `/step`). It also required designing a number of data classes to encapsulate all the edit operations on a recipe so that the actual command can execute them.

PRs: [#120](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/120), [#126](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/126).


#### Help Command

ChopChop's help feature is unique in that instead of either (a) simply showing a link to the user guide, or (b) simply dumping the command usage string into the output box verbatim, it will provide a clickable link to the user guide, and even the specific section of the user guide for a given command, so the user can see the usage and examples etc. including step-by-step walkthroughs in a less constrained view.

This feature was designed to minimise changes to existing commands, and to reduce duplication of usage strings between the program and the user guide to eliminate the problem of synchronising them. Java reflection was used to automatically provide help for commands without needing changes to the commands themselves.

PRs: [#181](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/181).



### Other Contributions

#### Project Management
Managed release `v1.3` on GitHub, including feature/work allocation, bug triaging, and integration/cleanup code and testing.

#### Enhancements to Existing Features

1. Enhance ingredient model to consist of multiple sets of quantity+expiry date ([#59](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/59), [#75](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/75)).

2. Improve the visual aspect of the GUI ([#198](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/198)).

3. Increased code coverage by writing a series of comprehensive tests ([#195](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/195), [#202](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/202)).



#### Documentation
**User Guide**

Primary custodian for the User Guide, ensured consistent formatting, phrasing, and visuals. Added detailed walkthrough for the [EditRecipeCommand](https://AY2021S1-CS2103T-T10-3.github.io/tp/UserGuide.html#EditRecipeCommand), documentation for [tab completion](https://AY2021S1-CS2103T-T10-3.github.io/tp/UserGuide.html#TabCompletion), and [quantity/unit handling](https://AY2021S1-CS2103T-T10-3.github.io/tp/UserGuide.html#QuantitiesAndUnits) ([#194](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/194), [#264](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/264), [#265](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/265), [#266](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/266)).

**Developer Guide**

Added implementation details of the [parser](https://AY2021S1-CS2103T-T10-3.github.io/tp/DeveloperGuide.html#ImplCommandParser), [tab completer](https://AY2021S1-CS2103T-T10-3.github.io/tp/DeveloperGuide.html#ImplTabCompletion), and [help command](https://AY2021S1-CS2103T-T10-3.github.io/tp/DeveloperGuide.html#ImplHelpCommand) ([#268](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/268)).



#### Community

Reviewed pull requests with significant comments: [#43](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/43), [#56](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/56), [#97](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/97), [#130](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/130).

Contributed detailed bug reports for other teams during PE-D: [ped repo](https://github.com/zhiayang/ped/issues)
