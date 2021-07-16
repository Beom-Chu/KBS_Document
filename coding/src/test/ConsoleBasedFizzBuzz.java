package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class ConsoleBasedFizzBuzz implements FizzBuzz {

  @Override
  public void print(int from, int to) {
    
    for (int i = from; i <= to; i++) {
      if (i % 3 == 0 && i % 5 == 0) {
        System.out.println("FizzBuzz");
      } else if (i % 3 == 0) {
        System.out.println("Fizz");
      } else if (i % 5 == 0) {
        System.out.println("Buzz");
      } else {
        System.out.println(i);
      }
    }
  }

  public void print2(int from, int to) {
    
    for (int i = from; i <= to; i++) {
      StringBuilder sb = new StringBuilder();
      if (i % 3 == 0) {
        sb.append("Fizz");
      }
      if (i % 5 == 0) {
        sb.append("Buzz");
      }
      System.out.println(sb.isEmpty() ? i : sb);
    }
  }


  static public void main(String[] args) throws Exception {

    ConsoleBasedFizzBuzz console = new ConsoleBasedFizzBuzz();

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    int max = Integer.valueOf(reader.readLine());

    console.print2(1, max);

  }
}
