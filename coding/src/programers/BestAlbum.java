package programers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestAlbum {
	
	public static int[] solution(String[] genres, int[] plays) {
		
		ArrayList<HashMap<String,Object>> music = new ArrayList<>();
		ArrayList<HashMap<Integer,String>> liTot = new ArrayList<>();
		HashMap<String,Integer> total = new HashMap<>();
		
		
		for(int i=0; i<genres.length; i++) {
			HashMap<String,Object> map = new HashMap<>();
			map.put("no", i);
			map.put("genre", genres[i]);
			map.put("play", plays[i]);
			music.add(map);
			total.put(genres[i], total.getOrDefault(genres[i], 0)+plays[i]);
		}
		
		for(String gen : total.keySet()) {
			HashMap<Integer,String> map = new HashMap<>();
			map.put(total.get(gen), gen);
			liTot.add(map);
		}
		
		Collections.sort(music, new Comparator<HashMap<String, Object>>() {
            public int compare(HashMap<String, Object> m1, HashMap<String, Object> m2) {
            	int i = ((Integer)m2.get("play")).compareTo((Integer)m1.get("play"));
            	int j = ((Integer)m2.get("no")).compareTo((Integer)m1.get("no"));
            	return i==0?j:i;
            }
		});
		
		for(HashMap hm : music) System.out.println(hm.get("no")+","+hm.get("genre")+","+hm.get("play")+","+hm.get("total"));
		
		ArrayList<Integer> list = new ArrayList<>();
		int cnt = 0;
		String bfGen = "";
		for(HashMap hm : music) {

		}

		
		return new int[]{1,2};
	}
	
    public int[] solution2(String[] genres, int[] plays) {
        List<Map<String,Object>> lmGenres = new ArrayList<Map<String,Object>>();
    	List<Integer> result = new ArrayList<>();
    	
    	for(int i=0; i<genres.length; i++) {
    		int chkIdx = -1;
    		for(int j=0; j<lmGenres.size(); j++) {
    			if(lmGenres.get(j).containsValue(genres[i])) {
    				chkIdx = j;
    				break;
    			}
    		}
    		if(chkIdx > -1) {
    			lmGenres.get(chkIdx).put("plays", (int)lmGenres.get(chkIdx).get("plays")+plays[i]);
    		}else {
    			Map<String, Object> tempM = new HashMap<>();
    			tempM.put("genres", genres[i]);
        		tempM.put("plays", plays[i]);
    			lmGenres.add(tempM);
    		}
    	}
    	
		Collections.sort(lmGenres,new playCompare());
		
		for(Map<String,Object> m :lmGenres) {
			List<Map<String,Object>> lmMusic = new ArrayList<Map<String,Object>>();
			for(int i=0; i<genres.length; i++) {
				Map<String, Object> tempM = new HashMap<>();
				if(m.get("genres").equals(genres[i])) {
					tempM.put("plays", plays[i]);
					tempM.put("no", i);
					lmMusic.add(tempM);
				}
			}
			Collections.sort(lmMusic,new playCompare());
			
			m.put("music", lmMusic);
		}
    	
		for(Map<String,Object> m :lmGenres) {
			List<Map<String,Object>> lmMusic = (List<Map<String, Object>>) m.get("music");
    		for(int i=0; i<lmMusic.size(); i++) {
    			if(i>1) break;
    			result.add((Integer) lmMusic.get(i).get("no"));
    		}
		}
    	
    	int[] answer = new int[result.size()];
    	for(int i=0; i<result.size();i++) answer[i]=result.get(i);
    	
        return answer;
    }


    class playCompare implements Comparator<Map<String, Object>>{
    	public int compare(Map<String, Object> m1, Map<String, Object> m2) {
			return ((Integer) m2.get("plays")).compareTo((Integer) m1.get("plays"));
		}
    }
	
	public static void main(String[] args) {
		
		String[] genres = {"classic", "pop", "classic", "classic", "pop"};
		int[] plays = {500, 600, 150, 800, 2500};
		//[4, 1, 3, 0]
		
//		String[] genres = {"classic", "classic", "classic", "classic", "pop"};
//		int[] plays = {500, 150, 800, 800, 2500};
//		//[4, 2, 3];
				
//		String[] genres = {"pop", "pop", "pop", "rap", "rap"};
//		int[] plays = {45, 50, 40, 60, 70};
//		//[1, 0, 4, 3];
		
//		String[] genres = {"pop", "pop", "pop"};
//		int[] plays = {40, 50, 40};
//		//[1, 0];
		
		int[] rtn = solution(genres,plays);

		System.out.println(rtn.toString());
	}

}
