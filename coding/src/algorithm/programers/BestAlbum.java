package algorithm.programers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BestAlbum {
	//베스트 앨범
	/*
	스트리밍 사이트에서 장르 별로 가장 많이 재생된 노래를 두 개씩 모아 베스트 앨범을 출시하려 합니다. 노래는 고유 번호로 구분하며, 노래를 수록하는 기준은 다음과 같습니다.

	속한 노래가 많이 재생된 장르를 먼저 수록합니다.
	장르 내에서 많이 재생된 노래를 먼저 수록합니다.
	장르 내에서 재생 횟수가 같은 노래 중에서는 고유 번호가 낮은 노래를 먼저 수록합니다.
	노래의 장르를 나타내는 문자열 배열 genres와 노래별 재생 횟수를 나타내는 정수 배열 plays가 주어질 때, 베스트 앨범에 들어갈 노래의 고유 번호를 순서대로 return 하도록 solution 함수를 완성하세요.

	제한사항
	genres[i]는 고유번호가 i인 노래의 장르입니다.
	plays[i]는 고유번호가 i인 노래가 재생된 횟수입니다.
	genres와 plays의 길이는 같으며, 이는 1 이상 10,000 이하입니다.
	장르 종류는 100개 미만입니다.
	장르에 속한 곡이 하나라면, 하나의 곡만 선택합니다.
	모든 장르는 재생된 횟수가 다릅니다.

	*/
	
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
