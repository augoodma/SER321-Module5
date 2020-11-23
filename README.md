## SER321-Module5 Activity 1
#Task 1:
1. The main structure of this code is that it distributes the merge sort algorithm across several localhost threads (theoretically over a network) to execute the task of sorting an array of integers.  It begins by breaking the array into halves and passing each half to either a Branch object which will halve it again or a Sorter object which will sort its segment.  Both object types utilize function 'peek' and 'remove' functions to accomplish their tasks.  The advantage of this distributed systems is that, as a recurisive function, merge sort can use a lot of memory.  This system breaks spreads memory consumption across threads.  The disadvantage is that the strengths of merge sort are its speed and run time consistency, which, by distributing over a network, is degraded/subject to network stability.

2. This experiment will run the following array test cases three times each and average:
	* Test case 1: provided array: [5, 1, 6, 2, 3, 4, 10, 634, 34, 23, 653, 23, 2, 6]
	* Test case 2: proved array (sorted): [1, 2, 2, 3, 4, 5, 6, 6, 10, 23, 23, 34, 634, 653]
	* Test case 3: array of 1000 random numbers (0-999) (same arry used for each tree configuration below)

	Test case 1 run times:
	* One Sorter: 52 ms, 48 ms, 51 ms. Average: 50.33 ms
	* One Branch, Two Sorters: 58 ms, 50 ms, 53 ms. Average: 53.67 ms
	* Three Branches, Four Sorters: 119 ms, 112 ms, 113 ms. Average: 114.67 ms

	Test case 2 run times:
	* One Sorter: 46 ms, 45 ms, 69 ms. Average: 53.33 ms
	* One Branch, Two Sorters: 51 ms, 70 ms, 70 ms. Average: 63.67 ms
	* Three Branches, Four Sorters: 108 ms, 107 ms, 139 ms. Average: 118 ms

	Test case 3 run times:
	* One Sorter: 865 ms, 625 ms, 621 ms. Average: 703.67 ms
	* One Branch, Two Sorters: 1890 ms, 1248 ms, 1394 ms. Average: 1510.67 ms
	* Three Branches, Four Sorters: 3495 ms, 3440 ms, 3486 ms. Average: 3473.67 ms

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
    * Random data: 8586 ms, 8641 ms, 8618 ms. Average:
    * Static data: 8466 ms, 8351 ms, 8739 ms. Average:

    Test case 5 run times:
    * Random data: 8992 ms, 8947 ms, 8911 ms. Average:
    * Static data: 9103 ms,  ms,  ms. Average:

