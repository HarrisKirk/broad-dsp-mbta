# Broad Institute DSP Software Engineering MBTA query tool
## Requirements
A java 8 jdk must be installed.
## Overall Comments
A git tag "QUESTION_x" was applied when each question was finished

## Question 1 
### Running
```
./gradle runAssignment
or
./gradle rA

```
### Discussions

The 'filtering' URL method was used to
* limit the amount of network traffic
* eliminate writing the small amount of conditional logic to include the 2 types of rail routes

## Question 2
### Running
```
./gradle runAssignment
or
./gradle rA
```
### Discussions
Main problem I had was getting the unit test assertEquals to work properly with sets as a value of a map key.  Learned TreeSet is the way to model unique data like routes through a given railway stop.

## Question 3
### Running
```
./gradlew -PBEGIN_STOP="Riverside" -PEND_STOP="Wonderland" runAssignment
```
### Discussions and limitations
Running the program without stop paramters will simply not process that section of the report.

Does not handle a journey requiring more than 1 transfer.
Also may not handle the case of a stop having more than 1 routes coming through it.   It needs more testing.

Had trouble when realizing the case of more than one transfer would need a deeper, more general algorithm.   Wanted to submit this work before
the start of the week.   However, considering that most of these Boston MBTA stops are reachable with one transfer, I decided to commit this functionality.

## Sample output
```
./gradlew -PBEGIN_STOP="Riverside" -PEND_STOP="Wonderland" runAssignment

MBTA_REPORTER Start...

------------------
Question 1: Routes
------------------
Red Line
Mattapan Trolley
Orange Line
Green Line B
Green Line C
Green Line D
Green Line E
Blue Line

-----------------
Question 2: Stops
-----------------
(1) Route with fewest stops is: Mattapan Trolley......... (8 stops)
(2) Route with most stops is:   Green Line B............. (24 stops)
(3) Stops with multiple routes going through it.
Ashmont                  [Mattapan, Red]
State                    [Blue, Orange]
Government Center        [Green-E, Green-C, Blue, Green-D]
Haymarket                [Green-E, Orange, Green-C]
North Station            [Green-E, Orange, Green-C]
Downtown Crossing        [Red, Orange]
Copley                   [Green-E, Green-C, Green-D, Green-B]
Arlington                [Green-E, Green-C, Green-D, Green-B]
Boylston                 [Green-E, Green-C, Green-D, Green-B]
Park Street              [Green-E, Green-C, Green-D, Red, Green-B]
Hynes Convention Center  [Green-D, Green-C, Green-B]
Kenmore                  [Green-D, Green-C, Green-B]
Saint Paul Street        [Green-C, Green-B]

----------------------------
Question 3: Stop A to Stop B
----------------------------
Riverside to Wonderland -> [Green-D, Blue]

...MBTA_REPORTER [OK]

```







