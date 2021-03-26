---
layout: page
title: hjl99's Project Portfolio Page
---

## Project: ChopChop

ChopChop is a food recipe and ingredient inventory management system, which aims to make it easier for people to manage their recipes and ingredients in an easy and effective manner. It features a keyboard-driven command interface and a GUI written in JavaFX.

A summary of code contributions can be found here: [reposense](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=hjl99).


### New Features

#### Filter Feature

This feature allows the user to filter recipes or ingredients with various criteria, in order to obtain a group of recipes or ingredients sharing certain characteristics. 

More than one multi-word search term in any sequence is allowed, so that greater flexibility is provided for the user. In addition, search terms (except for expiry date) do not have to be complete to fulfil a match. Unlike those of `find` commands, the search results of `filter` must meet all criteria input by the user. 

PRs: [#144](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/144), [#157](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/157).

##### Filter Recipes

The user can filter recipes with keywords in search fields `ingredient`, `name` and `tag`. 

##### Filter Ingredients

The user can filter ingredients with keywords in search fields `expiry` (expiry date), `name` and `tag`. To avoid confusion, only the earliest expiry date input will be parsed in the filtering process.


### Other Contributions

#### Project Management

Assisted in keeping track of deadlines and deliverables.

#### Enhancements to Existing Features

1. Built model for Recipe, added additional 'tag' attribute and modified parser accordingly. ([#40](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/40), [#115](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/115), [#116](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/116)).

2. Increased code coverage by writing tests for Recipe and its related commands, filter feature and attributes involved. ([#176](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/176), [#182](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/182)).

3. Provided sample data when the application is run locally for the first time. ([#289](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/289))

#### Documentation

**User Guide**

Added implementation details of filter feature for [Recipe](https://github.com/AY2021S1-CS2103T-T10-3/tp/blob/master/docs/UserGuide.md#547filtering-recipes--filterrecipe-jialei) and [Ingredient](https://github.com/AY2021S1-CS2103T-T10-3/tp/blob/master/docs/UserGuide.md#555filtering-ingredients--filteringredient-jialei); updated [annotated UI diagrams](https://github.com/AY2021S1-CS2103T-T10-3/tp/blob/master/docs/UserGuide.md#4navigating-the-user-interface). 

PRs: [#179](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/179), [#293](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/293), [#321](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/321).

**Developer Guide**

Added [Use Cases](https://github.com/AY2021S1-CS2103T-T10-3/tp/blob/master/docs/DeveloperGuide.md#a3use-cases), [Manual Testing Test Cases](https://github.com/AY2021S1-CS2103T-T10-3/tp/blob/master/docs/DeveloperGuide.md#binstructions-for-manual-testing) and UML diagrams for UI and Model components. 

PRs: [#33](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/33), [#309](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/309), [#321](https://github.com/AY2021S1-CS2103T-T10-3/tp/pull/321).


#### Community

Contributed detailed bug reports for other teams during PE-D: [ped repo](https://github.com/hjl99/ped/issues)
