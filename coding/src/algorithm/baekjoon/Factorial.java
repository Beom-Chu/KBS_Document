/**팩토리얼 [10872]
 * 0보다 크거나 같은 정수 N이 주어진다. 이때, N!을 출력하는 프로그램을 작성하시오.

입력
첫째 줄에 정수 N(0 ≤ N ≤ 12)가 주어진다.

출력
첫째 줄에 N!을 출력한다.

예제 입력 1 
10
예제 출력 1 
3628800
예제 입력 2 
0
예제 출력 2 
1*/
package algorithm.baekjoon;

import java.util.*;

public class Factorial {

	public static void main(String[] args) {
		long fr = System.currentTimeMillis();
		
		try(Scanner sc = new Scanner(System.in)){
			
//			int N = sc.nextInt();
//			System.out.println(factorial(N));
			/* 재귀함수가 속도가 더 느림 */
			
			int N = sc.nextInt();
			int rtn = 1;
			for(int i=2; i<=N; i++) rtn *= i;
			
			System.out.println(rtn);
		}
		System.out.println(System.currentTimeMillis()-fr);
	}
	
	static public int factorial(int N) {
		if(N==1) return 1;
		
		return N * factorial(N-1);
	}
}
