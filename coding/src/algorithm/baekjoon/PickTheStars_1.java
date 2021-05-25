/*별 찍기 - 1 [2438]
 
시간 제한	메모리 제한	제출	정답	맞은 사람	정답 비율
1 초	128 MB	135670	82904	71048	62.358%
문제
첫째 줄에는 별 1개, 둘째 줄에는 별 2개, N번째 줄에는 별 N개를 찍는 문제

입력
첫째 줄에 N(1 ≤ N ≤ 100)이 주어진다.

출력
첫째 줄부터 N번째 줄까지 차례대로 별을 출력한다.

예제 입력 1 
5
예제 출력 1 
*
**
***
****
*****

*/
package algorithm.baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PickTheStars_1 {

	public static void main(String[] args) throws IOException  {
		try(
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(isr)
				){
			
			int n = Integer.valueOf(reader.readLine());
			StringBuilder sb = new StringBuilder();
			
			while(n-- > 0) {
				System.out.println(sb.append("*"));
			}
		}
	}
}
