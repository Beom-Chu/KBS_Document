package algorithm.programers;

import java.util.LinkedList;
import java.util.Queue;
/*단어변환*/
/* 두 개의 단어 begin, target과 단어의 집합 words가 있습니다. 아래와 같은 규칙을 이용하여 begin에서 target으로 변환하는 가장 짧은 변환 과정을 찾으려고 합니다.

1. 한 번에 한 개의 알파벳만 바꿀 수 있습니다.
2. words에 있는 단어로만 변환할 수 있습니다.
예를 들어 begin이 "hit", target가 "cog", words가 ["hot","dot","dog","lot","log","cog"]라면 "hit" -> "hot" -> "dot" -> "dog" -> "cog"와 같이 4단계를 거쳐 변환할 수 있습니다.

두 개의 단어 begin, target과 단어의 집합 words가 매개변수로 주어질 때, 최소 몇 단계의 과정을 거쳐 begin을 target으로 변환할 수 있는지 return 하도록 solution 함수를 작성해주세요.

제한사항
각 단어는 알파벳 소문자로만 이루어져 있습니다.
각 단어의 길이는 3 이상 10 이하이며 모든 단어의 길이는 같습니다.
words에는 3개 이상 50개 이하의 단어가 있으며 중복되는 단어는 없습니다.
begin과 target은 같지 않습니다.
변환할 수 없는 경우에는 0를 return 합니다.
 */
public class ChangeWords {
	
	 public int solution(String begin, String target, String[] words) {
        int answer = 0;
        
        Queue<Word> que = new LinkedList<>();
        boolean[] chk = new boolean[words.length];
        que.add(new Word(begin,0));
        
        while(!que.isEmpty()) {
        	Word w = que.poll();
        	System.out.println(w.depth +","+w.word);
        	if(w.word.equals(target)) {
        		answer = w.depth;
        		break;
        	}
        	
        	for(int i=0; i<words.length; i++) {
        		if(!chk[i] && compareWord(w.word,words[i])) {
        			que.add(new Word(words[i],w.depth+1));
        			chk[i] = true;
        		}
        	}
        }
        return answer;
	}
	 
	class Word {
		String word;
		int depth;
		public Word(String word, int depth) {
			this.word = word;
			this.depth = depth;
		}
	}
	 
	/* 단어 비교 [한글자만 다른지 체크] */
	public boolean compareWord(String s1, String s2) {
		int cnt = 0;
		for(int i=0; i<s1.length(); i++) {
			if(s1.charAt(i)==s2.charAt(i)) cnt++;
		}
		return cnt == s1.length()-1;
	}
	 
	public static void main(String[] args) {
		ChangeWords cw = new ChangeWords();
		
		String begin = "hit", target="cog";
//		String[] words = {"hot", "dot", "dog", "lot", "log", "cog"}; /*4*/
		String[] words = {"hot", "dot", "dog", "lot", "log"}; /*0*/
		
		System.out.println(cw.solution(begin, target, words));
	}

}
