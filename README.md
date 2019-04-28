# Broad Institute DSP Software Engineering MBTA query tool
## Requirements
A java 8 jdk must be installed.
## Overall Comments
A git tag "QUESTION_x" was applied when each question was finished

## Question 1 
### Running
```
./gradle run
```
### Discussions

The 'filtering' URL method was used to
* limit the amount of network traffic
* eliminate writing the small amount of conditional logic to include the 2 types of rail routes

## Question 2
### Running
```
./gradle run
```
### Discussions
Main problem I had was getting the unit test assertEquals to work properly with sets as a value of a map key.  Learned TreeSet is the way to modle unique data like routes through a given railway stop.





