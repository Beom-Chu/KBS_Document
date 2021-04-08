package programers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.sun.javafx.binding.SelectBinding.AsInteger;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
//베스트 앨범
public class BestAlbum {
	
	 /* 최후 */
	public int[] solution(String[] genres, int[] plays) {
		
		ArrayList<HashMap<String,Object>> music = new ArrayList<>();
		HashMap<String,Integer> total = new HashMap<>();
		TreeMap<Integer,String> top = new TreeMap<>((o1,o2)->o2.compareTo(o1));
		ArrayList<Integer> listRtn = new ArrayList<>();
		
		//음악별 컬렉션 설정, 장르별 재생수 합 구하기
		for(int i=0; i<genres.length; i++) {
			HashMap<String,Object> map = new HashMap<>();
			map.put("no", i);
			map.put("genre", genres[i]);
			map.put("play", plays[i]);
			music.add(map);
			total.put(genres[i], total.getOrDefault(genres[i], 0)+plays[i]);
		}
		
		//장르별 재생수 정렬
		for(String gen : total.keySet()) {
			top.put(total.get(gen), gen);
		}
		
		//음악별 재생수 역순, 번호순 으로 정렬
		Collections.sort(music, new Comparator<HashMap<String, Object>>() {
            public int compare(HashMap<String, Object> m1, HashMap<String, Object> m2) {
            	int i = ((Integer)m2.get("play")).compareTo((Integer)m1.get("play"));
            	int j = ((Integer)m1.get("no")).compareTo((Integer)m2.get("no"));
            	return i==0?j:i;
            }
		});
		
		//앨범 수록곡 선별
		for(Integer key : top.keySet()) {
			int mCnt = 0; //음악 count
			for(HashMap m : music) {
				if(top.get(key).equals(m.get("genre"))) {
					listRtn.add((int)m.get("no"));
					mCnt++;
				}
				if(mCnt>1) break; //음악수 2건 이상일때 break;
			}
		}
		
		//컬렉션=>배열 변환
		int[] result = new int[listRtn.size()];
		for(int i=0; i<listRtn.size(); i++) result[i] = listRtn.get(i);
		
		return result;
	}
	
	
	
	
	
	/* 최초 */
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
    
    
    
    
    
    
    
    /* Music Class 활용 */
    public int[] solution3(String[] genres, int[] plays) {
    	
    	ArrayList<Music> mList = new ArrayList<>();
    	HashMap<String,Integer> total = new HashMap<>();
    	ArrayList<Integer> result = new ArrayList<>();
    	
    	for(int i=0; i<genres.length; i++) {
    		Music music = new Music(i,plays[i],genres[i]);
    		mList.add(music);
    		total.put(genres[i], total.getOrDefault(genres[i],0)+plays[i]);
    	}
    	
    	for(Music m : mList) m.total = total.get(m.genre);
    	
    	Collections.sort(mList,new comparePlay());
    	
    	int mCnt = 0;
    	String bfGenre = "";
		for(Music m : mList) {
			
			if(!bfGenre.equals(m.genre)) {
				bfGenre = m.genre;
				mCnt = 0;
			}
			
			if(mCnt < 2) {
				result.add(m.no);
				mCnt++;
			}
		}

//    	for(int i: result) System.out.print(i+" ");
		
    	return result.stream().mapToInt(i->i).toArray();
    }
    
    class comparePlay implements Comparator<Music>{
    	public int compare(Music m1, Music m2) {
    		int i = m2.total - m1.total;
    		int j = m2.play - m1.play;
    		int k = m1.no - m2.no;
    		return i==0 ? (j==0 ? k : j ) : i;
    	}
    }
    
    public class Music {
    	int no;
    	int play;
    	String genre;
    	int total;
    	
    	Music(int no, int play, String genre) {
    		this.no = no;
    		this.play = play;
    		this.genre = genre;
    	}
    }
	
	public static void main(String[] args) {
		
		String[] genres = {"classic", "pop", "classic", "classic", "pop"};
		int[] plays = {500, 600, 150, 800, 2500};
		/*[4, 1, 3, 0]*/
		
//		String[] genres = {"classic", "classic", "classic", "classic", "pop"};
//		int[] plays = {500, 150, 800, 800, 2500};
//		/*[4, 2, 3]*/
				
//		String[] genres = {"pop", "pop", "pop", "rap", "rap"};
//		int[] plays = {45, 50, 40, 60, 70};
//		/*[1, 0, 4, 3]*/
		
//		String[] genres = {"pop", "pop", "pop"};
//		int[] plays = {40, 50, 40};
//		/*[1, 0]*/
		
//		String[] genres = {"A", "A", "B", "A", "B", "B", "A", "A", "A", "A"};
//		int[] plays = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
//		/*0, 1, 2, 4*/
		
		BestAlbum bestAlbum = new BestAlbum();
		int[] rtn = bestAlbum.solution2(genres,plays);
		
		for(int i : rtn) System.out.print(i+" ");
		
		
	}

}
