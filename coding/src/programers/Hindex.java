package programers;

import java.util.Arrays;

/*H-Index*/
public class Hindex {
	
	public int solution(int[] citations) {
		int rtn = 0;
		
		Arrays.sort(citations);
		
		for(int i=citations.length-1;i>=0;i--) {
			if(rtn < citations[i] && citations[i] != 0) rtn++;
			else break;
		}
		
		return rtn;
	}
	
	/* 최초 */
    public int solution0(int[] citations) {
        int answer = 0;
        Arrays.sort(citations);
        
        for(int i=citations.length; i>=1; i--) {
        	int cnt = 0;
        	for(int j=citations.length-1; j>=0; j--) {
        		if(i<=citations[j]) cnt++;
        	}
        	if(cnt >= i) {
        		answer = i;
        		break;
        	}
        }
        return answer;
    }
	
	public static void main(String[] args) {
		
		int[] citations = {3, 0, 6, 1, 5};  /*3*/
//		int[] citations = {0,0,0};	/*0*/
//		int[] citations = {0,1,1};  /*1*/
		
		Hindex hindex = new Hindex();
		int rtn = hindex.solution(citations);
		System.out.println(rtn);

	}

}
