package programers;

import java.util.Arrays;

public class TelList {
	
	public static boolean solution(String[] phone_book) {
        
		Arrays.sort(phone_book);
		
		for(int i=0; i<phone_book.length; i++) {
			for(int j=i+1; j<phone_book.length; j++) {
				if(phone_book[j].startsWith(phone_book[i])) {
					return false;
				}
			}
		}
		
        return true;
    }

	public static void main(String[] args) {
		
		String[] param = {"119", "97674223", "1195524421"}; //	false
//		String[] param = {"123","456","789"}; //	true
//		String[] param = {"12","123","1235","567","88"}; //	false
		
		long start = System.currentTimeMillis();
		boolean rtn = solution(param);
		System.out.println(rtn);
		System.out.println(System.currentTimeMillis()-start);
	}

}
