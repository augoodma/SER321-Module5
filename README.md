## SER321-Module5 Activity 1

Task 1:

1. The main structure of this code is that it distributes the merge sort algorithm across several localhost threads (theoretically over a network) to execute the task of sorting an array of integers.  It begins by breaking the array into halves and passing each half to either a Branch object which will halve it again or a Sorter object which will sort its segment.  Both object types utilize function 'peek' and 'remove' functions to accomplish their tasks.  The advantage of this distributed systems is that, as a recurisive function, merge sort can use a lot of memory.  This system breaks spreads memory consumption across threads.  The disadvantage is that the strengths of merge sort are its speed and run time consistency, which, by distributing over a network, is degraded/subject to network stability.

2. This experiment will run the following array test cases three times each and average:
	* Test case 1: provided array (unsorted): [5, 1, 6, 2, 3, 4, 10, 634, 34, 23, 653, 23, 2, 6]
	* Test case 2: proved array (sorted):     [1, 2, 2, 3, 4, 5, 6, 6, 10, 23, 23, 34, 634, 653]
	* Test case 3: array of 1000 random numbers (0-999) (same array used for each tree configuration)

	Test case 1 run times:
	* One Sorter:                    44ms,  45ms,  46ms. Average:  45ms
	* One Branch, Two Sorters:       48ms,  49ms,  51ms. Average:  49ms
	* Three Branches, Four Sorters: 112ms, 117ms, 115ms. Average: 115ms
	* Gradle command:
	```
	gradle Starter -Ptest=1
	```

	Test case 2 run times:
	* One Sorter:                    51ms,  59ms,  45ms. Average:  52ms
	* One Branch, Two Sorters:       52ms,  73ms,  51ms. Average:  59ms
	* Three Branches, Four Sorters: 117ms, 153ms, 115ms. Average: 128ms
	* Gradle command:
	```
	gradle Starter -Ptest=2
	```

	Test case 3 run times:
	* One Sorter:                    654ms,  667ms,  653ms. Average:  658ms
	* One Branch, Two Sorters:      1309ms, 1393ms, 1422ms. Average: 1375ms
	* Three Branches, Four Sorters: 3442ms, 3467ms, 3576ms. Average: 3495ms
	* Gradle command:
	```
	gradle Starter -Ptest=3 -Psize=1000
	```

	Analysis:
	* Test case 1 and 2 are analyzed together to demonstrate unsorted vs sorted distributed merge sort.  Across the board, the sorted array was slightly less performant than the unsorted array which demonstrates that distribution of merge sort does not solve the traditional merge sort problem of not executing a sorted array faster than an unsorted array.  A larger sample size would likely show average times that are similar for sorted and unsorted.
	* Test case 3 demonstrates distributed unsorted merge sort at a large scale.  This test demonstrated that the advantage of merge sort's speed is quickly lost the more complex the tree structure becomes.  A single sorter handled this task signifcantly faster than multiple sorters.  In fact, inefficiently seem to scale with an increasing number of sorters.  Test cases 1 and 2 demonstrate this as well but not as dramatically.

3. This experiment will run the following tree test cases three times each and average:
	* Test case 4: extended tree
	            0
          1           2
       3     4     5     6
     7   8 9  10 11 12 13 14
	* Test case 5: lopsided tree
	           0
              1 2
             3 4
            5 6
           7 8
         9 10
       11 12
     13 14

    Test case 4 run times:
    * Random data: 2491 ms, 2391 ms, 2265 ms. Average: 2382ms
    * Static data: 1961 ms, 1963 ms, 1778 ms. Average: 1901ms
    * Gradle command:
	```
	gradle Starter -Ptest=4 -Psize=250
	```
	
    Test case 5 run times:
    * Random data: 2501 ms, 2491 ms, 2440 ms. Average: 2477ms
    * Static data: 1997 ms, 1949 ms, 1974 ms. Average: 1973ms
    * Gradle command:
	```
	gradle Starter -Ptest=5 -Psize=250
	```

    Analysis:
    * Test 4 with one additional level of depth over test case 3 was, on average, 4% slower for a random array of the same size. Test case 5 with twice the depth of test case 3, but with the same number of Branches an Sorters, was also, on average, 4% slower for a static array of the same size.  It appears that inceasing the overall number of nodes, regardless of type, has an negative effect on performance for distributed merge sort.  Likewise, increasing the depth of the array, also causes a degradation in performance.
    * Randomly generated data from test 4 and 5 performed, on average, 25% slower than static data from the same tests.  

4. Looking at Wireshark, there is quite a lot of traffic.  It appears that for each node, when passing data down the tree, at each level, there is a three-way handshake, followed by the PSH of data from one node to the next.  As the function stack reduces, Wireshark shows a FIN and ACK.  Being a recursive algorithm, the amount of traffic likely grows exponentially with complexity of the tree.

Task 2:

1. N/A

2. N/A

3. N/A

4. Repeat of tests over a network:
	Test case 1 run times:
	* One Sorter:                   1683ms, 1667ms, 1679ms. Average: ms
	* One Branch, Two Sorters:      1667ms, 1670ms, 1646ms. Average: ms
	* Three Branches, Four Sorters: 1711ms, 1685ms, 1717ms. Average: ms
	* Gradle command (client):
	```
	gradle Starter -Phost=<host> -Ptest=1
	```
	* Gradle command (server):
	```
	gradle Remote -Ptest=1
	```

	Test case 2 run times:
	* One Sorter:                   1663ms, 1699ms, 1658ms. Average: ms
	* One Branch, Two Sorters:      1668ms, 1675ms, 1653ms. Average: ms
	* Three Branches, Four Sorters: 1726ms, 1675ms, 1686ms. Average: ms
	* Gradle command (client):
	```
	gradle Starter -Phost=<host> -Ptest=2
	```
	* Gradle command (server):
	```
	gradle Remote -Ptest=2
	```

	Test case 3 run times:
	* One Sorter:                   96939ms, 98900ms, 97089ms. Average: ms
	* One Branch, Two Sorters:      97488ms, 96953ms, 98290ms. Average: ms
	* Three Branches, Four Sorters: 97447ms, 97625ms, 97508ms. Average: ms
	* Gradle command (client):
	```
	gradle Starter -Phost=<host> -Ptest=3 -Psize=1000
	```
	* Gradle command (server):
	```
	gradle Remote -Ptest=3 -Psize=1000
	```

	Test case 4 run times:
    * Random data: 2491 ms, 2391 ms, 2265 ms. Average: 2382ms
    * Static data: 1961 ms, 1963 ms, 1778 ms. Average: 1901ms
	* Gradle command (client):
	```
	gradle Starter -Phost=<host> -Ptest=4 -Psize=250
	```
	* Gradle command (server):
	```
	gradle Remote -Ptest=4 -Psize=250
	```
	
    Test case 5 run times:
    * Random data: 2501 ms, 2491 ms, 2440 ms. Average: 2477ms
    * Static data: 1997 ms, 1949 ms, 1974 ms. Average: 1973ms
	* Gradle command (client):
	```
	gradle Starter -Phost=<host> -Ptest=5 -Psize=250
	```
	* Gradle command (server):
	```
	gradle Remote -Ptest=5 -Psize=250
	```

	Analysis:  Across the board, times have increased by several hundred percent.  I expected this type of significant increase because of how ineffiecient the code is with traffic as observed in Wireshark during Task 1.

5. I used as my second system, AWS EC2, which of course lies outside my local network.

6. YouTube link (test 1 demonstration): 

7. N/A

8. Distribution is not efficient for a couple reasons.  First, the algorithm is self-recursive and grows exponentially in size as branches / tree-depth is added.  Second, for each of these function calls on the stack, there is TCP connection with a threeway-handshake, which are generally slow and combined with exponential function calls, causes the huge latency I have observed.  I would not distribute 'this' sorting algorithm like this in real life. If we are using a different sorting algorithm that is not recursive or dependent on several network connections.