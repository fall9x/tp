---
layout: page
title: Testing guide
---

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Running tests

There are two ways to run tests.

* **Method 1: Using IntelliJ JUnit test runner**
  * To run all tests, right-click on the `src/test/java` folder and choose `Run 'All Tests'`
  * To run a subset of tests, you can right-click on a test package,
    test class, or a test and choose `Run 'ABC'`
* **Method 2: Using Gradle**
  * Open a console and run the command `gradlew clean test` (Mac/Linux: `./gradlew clean test`)
  
* **Method 3: Using Gradle (headless)**

  * Thanks to the https://github.com/TestFX/TestFX[TestFX] library we use, our GUI tests can be run in the _headless_ mode. In the headless mode, GUI tests do not show up on the screen. That means the developer can do other things on the Computer while the tests are running.

  * To run tests in headless mode, open a console and run the command `gradlew clean headless test` (Mac/Linux: `./gradlew clean headless test`)

--------------------------------------------------------------------------------------------------------------------

## Types of tests

This project has two types of tests:
1. **GUI Tests** - These are tests involving the GUI. They include:
    1. *Unit tests* that test the individual components. These are in `chopchop.address.ui` package.

1. **Non-GUI Tests** - These are tests not involving the GUI. They include:
    1. *Unit tests* targeting the lowest level methods/classes.<br>
       e.g. `chopchop.commons.StringUtilTest`
    1. *Integration tests* that are checking the integration of multiple code units (those code units are assumed to be working).<br>
       e.g. `chopchop.storage.StorageManagerTest`
    1. Hybrids of unit and integration tests. These test are checking multiple code units as well as how the are connected together.<br>
       e.g. `chopchop.logic.CommandParserTest`
