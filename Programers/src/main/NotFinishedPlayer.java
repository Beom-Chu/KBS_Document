package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

//완주하지 못한 선수

public class NotFinishedPlayer {

	public static String solution(String[] participant, String[] completion) {
		  
		Arrays.sort(participant);
		Arrays.sort(completion);
        
		int i = 0;
		while(participant[i].equals(completion[i])) {
			i++;
			if(i == completion.length) break;
		}
        
        return participant[i];
    }
	
    public static String solution2(String[] participant, String[] completion) {
    	
        Arrays.sort(participant);
        Arrays.sort(completion);

        for(int i=0; i<completion.length; i++) {
        	if(!participant[i].equals(completion[i])) return participant[i];
        }

        return participant[participant.length-1];
    }
    
    //최고성능
    public static String solution3(String[] participant, String[] completion) {

    	HashMap<String, Integer> hm = new HashMap<>();
	    for(String p : participant) hm.put(p, hm.getOrDefault(p, 0) + 1);
	    for(String c : completion)  hm.put(c, hm.get(c) - 1);
	    
	    for(String key : hm.keySet()) {
	        if (hm.get(key) != 0) return key;
	    }
	    return "";
    }

	public static void main(String[] args) {
		
//		String[] participant = {"leo", "kiki", "eden"};
//		String[] completion = {"eden", "kiki"};
		
//		String[] participant = {"mislav", "stanko", "mislav", "ana"};
//		String[] completion = {"stanko", "ana", "mislav"};
		
		String[] participant = {"marina", "josipa", "nikola", "vinko", "filipa"};
		String[] completion = {"josipa", "filipa", "marina", "nikola"};
		
		String rtn = solution3(participant, completion);
		
		System.out.println(rtn);
		
	}

}
