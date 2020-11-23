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
  public static long start;
  public static long middle;
  public static long middle2;
  public static long finish;
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
  
  public static void Test(int port, String host, int size, int test) {
    int[] a = null, b = null;
    //Test case 1 (provided array)
    if (test == 1) a = new int[]{5, 1, 6, 2, 3, 4, 10, 634, 34, 23, 653, 23, 2, 6};

    //Test case 2 (sorted array)
    if (test == 2) a = new int[]{1, 2, 2, 3, 4, 5, 6, 6, 10, 23, 23, 34, 634, 653};

    //Test case 3 - 6 (long random array)
    if (test >= 3 && test < 7) {
      a = new int[size];
      for (int i = 0; i < a.length; i++) a[i] = (int) (Math.random() * 999);
    }

    //Test case 4 - 6 (long static array)
    if (test >= 4 && test < 7) b = RandomArray.get(size);

    start = System.currentTimeMillis();
    assert a != null;
    JSONObject response = NetworkUtils.send(port, init(a), host);

    System.out.println(response);
    response = NetworkUtils.send(port, peek(), host);
    System.out.println(response);

    while (true) {
      response = NetworkUtils.send(port, remove(), host);

      assert response != null;
      if (response.getBoolean("hasValue")) {
        System.out.println(response);
      } else {
        middle = System.currentTimeMillis();
        break;
      }
    }

    if (test >= 4) {
      middle2 = System.currentTimeMillis();
      assert b != null;
      JSONObject response2 = NetworkUtils.send(port, init(b), host);

      System.out.println(response2);
      response2 = NetworkUtils.send(port, peek(), host);
      System.out.println(response2);

      while (true) {
        response2 = NetworkUtils.send(port, remove(), host);

        assert response2 != null;
        if (response2.getBoolean("hasValue")) {
          System.out.println(response2);
        } else {
          finish = System.currentTimeMillis();
          break;
        }
      }
    }
  }

  public static void main(String[] args) {
    String host = args[0];
    int size = Integer.parseInt(args[1]);
    int test = Integer.parseInt(args[2]);

    // all the listening ports in the setup
    ArrayList<Integer> ports = new ArrayList<>(Arrays.asList(8000, 8001, 8002, 8003, 8004, 8005, 8006, 8007, 8008, 8009,
            8010, 8011, 8012, 8013, 8014));

    // use this tree setup for test cases 1 - 3
    //      0
    //   1     2
    // 3   4 5   6
    if(test >= 1 && test < 4) {
      new Thread(new Branch(ports.get(0), ports.get(1), ports.get(2), host)).start();

      new Thread(new Branch(ports.get(1), ports.get(3), ports.get(4), host)).start();
      new Thread(new Sorter(ports.get(3))).start();
      new Thread(new Sorter(ports.get(4))).start();

      new Thread(new Branch(ports.get(2), ports.get(5), ports.get(6), host)).start();
      new Thread(new Sorter(ports.get(5))).start();
      new Thread(new Sorter(ports.get(6))).start();

      // make sure we didn't hang
      System.out.println("started");

      // One Sorter
      Test(ports.get(3), host, size, test);
      long time1 = middle - start;

      // One branch / Two Sorters
      Test(ports.get(2), host, size, test);
      long time2 = middle - start;

      // Three Branch / Four Sorters
      Test(ports.get(0), host, size, test);
      long time3 = middle - start;

      System.out.println("One Sorter Time:                  " + time1);
      System.out.println("One branch / Two Sorters Time:    " + time2);
      System.out.println("Three Branch / Four Sorters Time: " + time3);
      System.out.println("Host: " + args[0]);
    }
    if(test == 4) {
      // use this node setup for test case 4
      //            0
      //      1           2
      //   3     4     5     6
      // 7   8 9  10 11 12 13 14
      new Thread(new Branch(ports.get(0), ports.get(1), ports.get(2), host)).start();

      new Thread(new Branch(ports.get(1), ports.get(3), ports.get(4), host)).start();
      new Thread(new Branch(ports.get(3), ports.get(7), ports.get(8), host)).start();
      new Thread(new Sorter(ports.get(7))).start();
      new Thread(new Sorter(ports.get(8))).start();

      new Thread(new Branch(ports.get(4), ports.get(9), ports.get(10), host)).start();
      new Thread(new Sorter(ports.get(9))).start();
      new Thread(new Sorter(ports.get(10))).start();

      new Thread(new Branch(ports.get(2), ports.get(5), ports.get(6), host)).start();
      new Thread(new Branch(ports.get(5), ports.get(11), ports.get(12), host)).start();
      new Thread(new Sorter(ports.get(11))).start();
      new Thread(new Sorter(ports.get(12))).start();

      new Thread(new Branch(ports.get(6), ports.get(13), ports.get(14), host)).start();
      new Thread(new Sorter(ports.get(13))).start();
      new Thread(new Sorter(ports.get(14))).start();

      // make sure we didn't hang
      System.out.println("started");
      // One Sorter
      Test(ports.get(0), host, size, test);
      System.out.println("Test 4 Random Array Time: " + (middle - start));
      System.out.println("Test 4 Static Array Time: " + (finish - middle2));
      System.out.println("Host: " + args[0]);
    }
    if(test == 5) {
      // use this node setup for test case 5
      //           0
      //          1 2
      //         3 4
      //        5 6
      //       7 8
      //     9 10
      //   11 12
      // 13 14
      new Thread(new Branch(ports.get(0), ports.get(1), ports.get(2), host)).start();

      new Thread(new Branch(ports.get(1), ports.get(3), ports.get(4), host)).start();
      new Thread(new Sorter(ports.get(2))).start();

      new Thread(new Branch(ports.get(3), ports.get(5), ports.get(6), host)).start();
      new Thread(new Sorter(ports.get(4))).start();

      new Thread(new Branch(ports.get(5), ports.get(7), ports.get(8), host)).start();
      new Thread(new Sorter(ports.get(6))).start();

      new Thread(new Branch(ports.get(7), ports.get(9), ports.get(10), host)).start();
      new Thread(new Sorter(ports.get(8))).start();

      new Thread(new Branch(ports.get(9), ports.get(11), ports.get(12), host)).start();
      new Thread(new Sorter(ports.get(10))).start();

      new Thread(new Branch(ports.get(11), ports.get(13), ports.get(14), host)).start();
      new Thread(new Sorter(ports.get(12))).start();

      new Thread(new Sorter(ports.get(13))).start();
      new Thread(new Sorter(ports.get(14))).start();

      // make sure we didn't hang
      System.out.println("started");
      // One Sorter
      Test(ports.get(0), host, size, test);
      System.out.println("Test 5 Random Array Time: " + (middle - start));
      System.out.println("Test 5 Static Array Time: " + (finish - middle2));
      System.out.println("Host: " + args[0]);
    }
    if(test == 6) {
      // single node setup for task 2
      // 0
      new Thread(new Sorter(ports.get(0))).start();

      // make sure we didn't hang
      System.out.println("started");
      // One Sorter
      Test(ports.get(0), host, size, test);
      System.out.println("Host: " + args[0]);
    }
  }
}
