package programers;

import java.util.HashMap;

public class Camouflage {
	
	public static int solution(String[][] clothes) {
		
		int result = 1;
		HashMap<String,Integer> hm = new HashMap<>();
		
		for(String[] cloth : clothes) {
			hm.put(cloth[1], hm.getOrDefault(cloth[1], 0)+1);
		}
		
		for(String key :hm.keySet()) {
			result *= hm.get(key)+1;
		}
        
        return result-1;
    }
	
	public static void main(String[] args) {
		
//		String[][] clothes = {{"yellowhat", "headgear"}, {"bluesunglasses", "eyewear"}, {"green_turban", "headgear"}};	//5
		String[][] clothes = {{"crowmask", "face"}, {"bluesunglasses", "face"}, {"smoky_makeup", "face"}};	//3

		int rtn = solution(clothes);
		
		System.out.println(rtn);
	}

}
