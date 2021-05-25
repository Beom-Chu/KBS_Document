package algorithm.programers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/*모의고사*/
/*수포자는 수학을 포기한 사람의 준말입니다. 수포자 삼인방은 모의고사에 수학 문제를 전부 찍으려 합니다. 수포자는 1번 문제부터 마지막 문제까지 다음과 같이 찍습니다.

1번 수포자가 찍는 방식: 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, ...
2번 수포자가 찍는 방식: 2, 1, 2, 3, 2, 4, 2, 5, 2, 1, 2, 3, 2, 4, 2, 5, ...
3번 수포자가 찍는 방식: 3, 3, 1, 1, 2, 2, 4, 4, 5, 5, 3, 3, 1, 1, 2, 2, 4, 4, 5, 5, ...

1번 문제부터 마지막 문제까지의 정답이 순서대로 들은 배열 answers가 주어졌을 때, 가장 많은 문제를 맞힌 사람이 누구인지 배열에 담아 return 하도록 solution 함수를 작성해주세요.

제한 조건
시험은 최대 10,000 문제로 구성되어있습니다.
문제의 정답은 1, 2, 3, 4, 5중 하나입니다.
가장 높은 점수를 받은 사람이 여럿일 경우, return하는 값을 오름차순 정렬해주세요.*/
public class PracticeTest {
	
	public int[] solution(int[] answers) {
		
		int[][] supo = {	//찍는 방식
				{1, 2, 3, 4, 5},
				{2, 1, 2, 3, 2, 4, 2, 5},
				{3, 3, 1, 1, 2, 2, 4, 4, 5, 5},
		};
		int[] score = new int[3];	//점수
		int topScore = 0; //최고점수
		ArrayList<Integer> rtn = new ArrayList<Integer>();
		
		//수포자별 정답수
		for(int i=0; i<answers.length; i++) {
			if(answers[i] == supo[0][i%5])  score[0]++;
			if(answers[i] == supo[1][i%8])  score[1]++;
			if(answers[i] == supo[2][i%10]) score[2]++;
		}
		
		//최고 점수
		topScore = Math.max(Math.max(score[0], score[1]),score[2]);
		
		//최고 점수 해당 수포자
		for(int i=0;i<score.length;i++) {
			if(score[i]==topScore) rtn.add(i+1);
		}
		
		//배열 변환
		return rtn.stream().mapToInt(i->i).toArray();
	}
	
	/*최초*/
	public int[] solution0(int[] answers) {
        int[][] pattern = {{1, 2, 3, 4, 5}
        				  ,{2, 1, 2, 3, 2, 4, 2, 5}
        				  ,{3, 3, 1, 1, 2, 2, 4, 4, 5, 5}};
        int[] cnt = new int[3];
        List<Integer> l = new ArrayList<>();
        
        for(int i=0;i<answers.length;i++) {
        	if(answers[i]==pattern[0][i%5]) cnt[0]++;
        	if(answers[i]==pattern[1][i%8]) cnt[1]++;
        	if(answers[i]==pattern[2][i%10]) cnt[2]++;
        }
        
        if(cnt[0]>=cnt[1]&&cnt[0]>=cnt[2]) l.add(1);
        if(cnt[1]>=cnt[0]&&cnt[1]>=cnt[2]) l.add(2);
        if(cnt[2]>=cnt[0]&&cnt[2]>=cnt[1]) l.add(3);
        
        int size = l.size();
        int[] answer = new int[size];
        for(int i=0; i<size;i++) answer[i]=l.get(i);
        return answer;
        
//        return answer.stream().mapToInt(i -> i).toArray();
    }
	
	public static void main(String[] args) {
		
//		int[] answers = {1,2,3,4,5};	/*[1]*/
		int[] answers = {1,3,2,4,2};	/*[1,2,3]*/
		
		PracticeTest practiceTest = new PracticeTest();
		int[] rtn = practiceTest.solution(answers);
		
		for(int i : rtn) System.out.print(i+" ");
	}

}
