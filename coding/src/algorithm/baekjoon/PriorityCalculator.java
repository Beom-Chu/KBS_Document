/*
 * 우선순위 계산기 [20936]
 * 
시간 제한	메모리 제한	제출	정답	맞은 사람	정답 비율
1.5 초 (추가 시간 없음)	1024 MB	158	22	20	35.714%
문제
국렬이는 두 번씩이나 계산기 문제를 내놓고 또 계산기 문제를 냈다. 이대로라면 죽을 때까지 계산기를 우려먹을 생각이고, 당신은 귀찮지만 상금을 얻기 위해서 주어진 수식을 규칙에 맞게 계산해야 한다.

입력으로 주어지는 수식은 띄어쓰기 없이 수와 연산자가 번갈아 가면서 나온다. 수식의 번째 수를 
, 번째 연산자를 
로 표시하면 수가 개인 식을 
 
 
 
 ...  
로 표기할 수 있다. 연산자의 종류는 +, -, *, /가 있다. 마지막에 연산자가 있는 경우와 
가 음수인 경우는 입력으로 주어지지 않는다. 즉, ,  같은 경우는 입력으로 주어지지 않는다. 그리고 불필요한 이 수의 앞에 있을 수 있다. 즉,  같은 수식이 입력으로 주어질 수 있다.

주어진 수식을 다음 규칙에 맞게 계산할 것이다.

 
  중 가장 큰 값을 갖는 를 선택한다. ()
 
 가 가장 큰 값을 갖는 가 개 이상인 경우, 연산자 우선순위가 높은 
인 를 먼저 선택한다. 연산자의 우선순위는 곱셈과 나눗셈이 덧셈과 뺄셈보다 앞선다.
 
 가 가장 큰 값을 갖고 연산자 
의 우선순위가 같은 가 개 이상인 경우, 가 가장 작은 것을 선택한다.
 
 를 먼저 계산하고, 위의 과정을 연산자의 개수만큼 반복한다.
예를 들어서 수식이 로 주어진다고 하면 다음과 같이 계산된다.

가 가장 크기에 먼저 계산한다. 이후 계산식은 이다.
그다음으로 를 계산한다. 이후 계산식은 이다.
그 후 를 계산한다. 이후 계산식은 이다.
마지막에 남은 를 계산하면 최종 결과 값은 가 된다.
이 문제에서의 나눗셈은 C++에서 정수 간에 정의된 나눗셈으로 생각한다. 즉, 나누어지는 수가 양수면 나머지가  이상, 음수면 나머지가  이하로 처리가 되는 식으로 진행했을 때 나오는 몫을 계산하는 방식으로 이루어진다. 예를 들어, , , , 로 계산된다.

이와 같은 계산 과정에 따라 주어진 식을 계산하시오.

입력
다음과 같이 입력이 주어진다.

출력
첫 번째 줄에 주어진 식을 계산한 결과 값을 출력한다. 불필요한 은 제거해야 한다.

제한
는 계산하고자 하는 수식으로 지문에서 언급된 수와 연산자가 다른 문자 없이 교대로 나오며, 길이는  이하이다.
계산 과정 중의 모든 수는  이상  미만이며, 으로 나누는 경우는 없다. 수 앞에 불필요한 이 있을 수 있다.
주어지는 연산자는 +, -, *, /다.
예제 입력 1 
3*2+5-5+7
예제 출력 1 
9
예제 입력 2 
8/4/2
예제 출력 2 
1
예제 입력 3 
000009223372036854775807
예제 출력 3 
9223372036854775807
 */
package algorithm.baekjoon;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PriorityCalculator {

	public static void main(String[] args) {
		
		try(Scanner sc = new Scanner(System.in)){
			
			String str = sc.next();
			
			List<BigDecimal> numbers = new LinkedList<>();
			List<String> operator = new LinkedList<>();
			
			/*숫자 분리*/
			for(String s : str.split("[^0-9]")) numbers.add(new BigDecimal(s));
			
			/* 숫자만 있는경우 바로 출력 */
			if(numbers.size()==1) {
				System.out.println(numbers.get(0));
				return;
			}
			
			/*연산자 분리*/
			for(String s : str.split("[0-9]")) operator.add(s);
			operator.remove(0); /*첫번째 빈값 제거*/
			
			
			while(!operator.isEmpty()) {
				
				/* 최초 우선순위 설정 */
				Priority priority = new Priority(operator.get(0), numbers.get(0), numbers.get(1));
				
				for(int i=1; i<operator.size(); i++) {
					
					String oper = operator.get(i);
					BigDecimal calcNum = calc(oper,numbers.get(i),numbers.get(i+1));
					
					/* 우선순위 설정 */
					if(priority.number.compareTo(calcNum) < 0) {
						
						priority.setPriority(i, oper, calcNum);
					
					}else if(priority.number.compareTo(calcNum) == 0) {
						
						if(oper.equals("*") && oper.equals("/")) {
							
							priority.setPriority(i, oper, calcNum);
							
						}
					}
				}
				
				/* 우선순위에 해당하는 숫자, 연산자 컬렉션 제거 */
				operator.remove(priority.idx);
				numbers.remove(priority.idx);
				numbers.remove(priority.idx);
				
				/* 숫자 컬렉션에 우선순위 계산값 입력 */
				numbers.add(priority.idx, priority.number);
			}
			
			System.out.println(numbers.get(0));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 우선순위 Class */
	public static class Priority{
		int idx;
		String operator;
		BigDecimal number;
		
		public Priority(String operator, BigDecimal b1, BigDecimal b2) {
			this.idx = 0;
			this.operator = operator;
			this.number = calc(operator, b1, b2);
		}
		
		public void setPriority(int idx, String operator, BigDecimal number) {
			this.idx = idx;
			this.operator = operator;
			this.number = number;
		}
	}
	
	/* 계산 */
	public static BigDecimal calc(String operator, BigDecimal b1, BigDecimal b2) {
		
		if(operator.equals("+")) return b1.add(b2);
		else if(operator.equals("-")) return b1.subtract(b2);
		else if(operator.equals("*")) return b1.multiply(b2);
		else return b1.divide(b2);
		
	}

}
