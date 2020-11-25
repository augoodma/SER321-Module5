## SER321-Module5 Activity 2

!!!NOTE: THIS ASSIGNMENT WAS COMPLETED USING THE ORIGINALLY RELEASED HOMEWORK PDF!!!

1. The program demonstrates a peer-to-peer distribution algorithm following a leader node/follower node model.  The program, when running, calculates simple problems as assigned to nodes.  A leader node tracks the status of the other nodes and manages them.  If the leader node dies and the followers timeout from not communicating, they will choose a new leader.


2. The program begins by first running the starting leader node using:
```
gradle Leader -Phost=<host> -Pport=<port> -q --console=plain
```
Only after the leader node has started, startup follower nodes using:
```
gradle Follower -Phost=<host> -Pport=<port>  -PleaderHost=<leaderHost> -PleaderPort=<leaderPort> -q --console=plain
```
The leader address allows the followers to begin listening to the leader.
Occasionally, a follower will be chosen to perform a basic math problem.  The correct input is of the form:
```
calc <num1> <{+, -, *, /}> <num2>
```
If the leader dies, communication times out and the followers begin the process of the selecting a new leader.

3. Currently, the math problem functionality doesn't quite work. The communcication amongst nodes isn't passed and so the program locks up.  The logic I implemented has otherwise been found to sound after unit testing.  Additionally, the choose new leader functionality doesn't work for the same reasons as the math problem: communication issues.  The implementation of the algorithm is included for math problem and leader selection is included.