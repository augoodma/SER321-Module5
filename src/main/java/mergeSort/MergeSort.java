package mergeSort;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

public class MergeSort {
  /**
   * Thread that declares the lambda and then initiates the work
   */

  public static int message_id = 0;

  public static JSONObject init(int[] array) {
    JSONArray arr = new JSONArray();
    for (var i : array) {
      arr.put(i);
    }
    JSONObject req = new JSONObject();
    req.put("method", "init");
    req.put("data", arr);
    return req;
  }

  public static JSONObject peek() {
    JSONObject req = new JSONObject();
    req.put("method", "peek");
    return req;
  }

  public static JSONObject remove() {
    JSONObject req = new JSONObject();
    req.put("method", "remove");
    return req;
  }
  
  public static void Test(int port) {
    //size of 1000 occasionally caused a crash on my system, please feel free to grade using lower values
    int arraySize = 1000;
    //Uncomment the test case to run
    //Test case 1 (provided array)
    //int[] a = { 5, 1, 6, 2, 3, 4, 10, 634, 34, 23, 653, 23, 2, 6 };

    //Test case 2 (sorted array)
    //int[] a =   { 1, 2, 2, 3, 4, 5, 6, 6, 10, 23, 23, 34, 634, 653 };

    //Test case 3 - 5 (long random array)
    //int[] a = new int[arraySize];
    //for(int i = 0; i < a.length; i++)  a[i] = (int) (Math.random() * 999);

    //Test case 4 - 5 (long static array)
    int[] a = RandomArray.get(arraySize);

    JSONObject response = NetworkUtils.send(port, init(a));
    
    System.out.println(response);
    response = NetworkUtils.send(port, peek());
    System.out.println(response);

    while (true) {
      response = NetworkUtils.send(port, remove());

      if (response.getBoolean("hasValue")) {
        System.out.println(response);;
 
      } else{
        break;
      }
    }
  }

  public static void main(String[] args) {
    // all the listening ports in the setup
    ArrayList<Integer> ports = new ArrayList<>(Arrays.asList(8000, 8001, 8002, 8003, 8004, 8005, 8006, 8007, 8008, 8009,
            8010, 8011, 8012, 8013, 8014));

    // uncomment the tree setup and test functions wanted
    // use this tree setup for test cases 1 - 3
    //      0
    //   1     2
    // 3   4 5   6
    /*
    new Thread(new Branch(ports.get(0), ports.get(1), ports.get(2))).start();
    
    new Thread(new Branch(ports.get(1), ports.get(3), ports.get(4))).start();
    new Thread(new Sorter(ports.get(3))).start();
    new Thread(new Sorter(ports.get(4))).start();
    
    new Thread(new Branch(ports.get(2), ports.get(5), ports.get(6))).start();
    new Thread(new Sorter(ports.get(5))).start();
    new Thread(new Sorter(ports.get(6))).start();

    // make sure we didn't hang
    System.out.println("started");
    long start = System.currentTimeMillis();
    // One Sorter
    Test(ports.get(3));
    long finish = System.currentTimeMillis();
    System.out.println("Test 1 Time: " + (finish - start));

    start = System.currentTimeMillis();
    // One branch / Two Sorters
    Test(ports.get(2));
    finish = System.currentTimeMillis();
    System.out.println("Test 2 Time: " + (finish - start));

    start = System.currentTimeMillis();
    // Three Branch / Four Sorters
    Test(ports.get(0));
    finish = System.currentTimeMillis();
    System.out.println("Test 3 Time: " + (finish - start));
    */

    // use this node setup for test case 4
    //            0
    //      1           2
    //   3     4     5     6
    // 7   8 9  10 11 12 13 14

    new Thread(new Branch(ports.get(0), ports.get(1), ports.get(2))).start();

    new Thread(new Branch(ports.get(1), ports.get(3), ports.get(4))).start();
    new Thread(new Branch(ports.get(3), ports.get(7), ports.get(8))).start();
    new Thread(new Sorter(ports.get(7))).start();
    new Thread(new Sorter(ports.get(8))).start();

    new Thread(new Branch(ports.get(4), ports.get(9), ports.get(10))).start();
    new Thread(new Sorter(ports.get(9))).start();
    new Thread(new Sorter(ports.get(10))).start();

    new Thread(new Branch(ports.get(2), ports.get(5), ports.get(6))).start();
    new Thread(new Branch(ports.get(5), ports.get(11), ports.get(12))).start();
    new Thread(new Sorter(ports.get(11))).start();
    new Thread(new Sorter(ports.get(12))).start();

    new Thread(new Branch(ports.get(6), ports.get(13), ports.get(14))).start();
    new Thread(new Sorter(ports.get(13))).start();
    new Thread(new Sorter(ports.get(14))).start();

    // make sure we didn't hang
    System.out.println("started");
    long start = System.currentTimeMillis();
    // One Sorter
    Test(ports.get(0));
    long finish = System.currentTimeMillis();
    System.out.println("Test 4 Time: " + (finish - start));

    // use this node setup for test case 5
    //           0
    //          1 2
    //         3 4
    //        5 6
    //       7 8
    //     9 10
    //   11 12
    // 13 14
/*
    new Thread(new Branch(ports.get(0), ports.get(1), ports.get(2))).start();

    new Thread(new Branch(ports.get(1), ports.get(3), ports.get(4))).start();
    new Thread(new Sorter(ports.get(2))).start();

    new Thread(new Branch(ports.get(3), ports.get(5), ports.get(6))).start();
    new Thread(new Sorter(ports.get(4))).start();

    new Thread(new Branch(ports.get(5), ports.get(7), ports.get(8))).start();
    new Thread(new Sorter(ports.get(6))).start();

    new Thread(new Branch(ports.get(7), ports.get(9), ports.get(10))).start();
    new Thread(new Sorter(ports.get(8))).start();

    new Thread(new Branch(ports.get(9), ports.get(11), ports.get(12))).start();
    new Thread(new Sorter(ports.get(10))).start();

    new Thread(new Branch(ports.get(11), ports.get(13), ports.get(14))).start();
    new Thread(new Sorter(ports.get(12))).start();

    new Thread(new Sorter(ports.get(13))).start();
    new Thread(new Sorter(ports.get(14))).start();

    // make sure we didn't hang
    System.out.println("started");
    long start = System.currentTimeMillis();
    // One Sorter
    Test(ports.get(0));
    long finish = System.currentTimeMillis();
    System.out.println("Test 4 Time: " + (finish - start));
*/
  }
}
