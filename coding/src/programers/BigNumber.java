package programers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

/* 가장 큰 수*/
public class BigNumber {
	
	
    public String solution(int[] numbers) {
    	
    	String[] arrS = new String[numbers.length];
    	StringBuffer sb = new StringBuffer();
    	
    	/* 스트림 사용한 경우보다 성능이 조금 더 높음 */
    	for(int i=0;i<numbers.length;i++) arrS[i] = String.valueOf(numbers[i]);
		
    	Arrays.sort(arrS, new Comparator<String>() {
    		public int compare(String o1, String o2) {
    			return (o2+o1).compareTo(o1+o2);
    		}
		});
    	
    	for(String s : arrS) sb.append(s);
    	
    	return String.valueOf(new BigDecimal(sb.toString()));
    }
    
    /* 최초 */
    public String solution1(int[] numbers) {
		StringBuffer result = new StringBuffer();
		String[] arrS = Arrays.stream(numbers).mapToObj(i->String.valueOf(i)).toArray(String[]::new); 
		
		Arrays.sort(arrS, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return (s2+s1).compareTo(s1+s2);
			}
		});
		
		for(String s : arrS) result.append(s);
		
		return String.valueOf(new BigDecimal(result.toString()));
    }
	
	public static void main(String[] args) {
		
//		int[] numbers = {6, 10, 2}; /* 6210 */
//		int[] numbers = {3, 30, 34, 5, 9};	/* 9534330 */
		int[] numbers = {3, 30, 34, 5, 9,300};	/* 9534330300 */
//		int[] numbers = {3, 30, 34, 5, 9,340};	/* 9534330300 */

		BigNumber bigNumber = new BigNumber();
		String rtn = bigNumber.solution(numbers);
		
		System.out.println(rtn);
	}

}
