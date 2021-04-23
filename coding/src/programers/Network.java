package programers;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.text.html.HTMLDocument.Iterator;

/*네트워크*/
/*네트워크란 컴퓨터 상호 간에 정보를 교환할 수 있도록 연결된 형태를 의미합니다. 예를 들어, 컴퓨터 A와 컴퓨터 B가 직접적으로 연결되어있고, 
 컴퓨터 B와 컴퓨터 C가 직접적으로 연결되어 있을 때 컴퓨터 A와 컴퓨터 C도 간접적으로 연결되어 정보를 교환할 수 있습니다. 따라서 컴퓨터 A, B, C는 모두 같은 네트워크 상에 있다고 할 수 있습니다.

컴퓨터의 개수 n, 연결에 대한 정보가 담긴 2차원 배열 computers가 매개변수로 주어질 때, 네트워크의 개수를 return 하도록 solution 함수를 작성하시오.

제한사항
컴퓨터의 개수 n은 1 이상 200 이하인 자연수입니다.
각 컴퓨터는 0부터 n-1인 정수로 표현합니다.
i번 컴퓨터와 j번 컴퓨터가 연결되어 있으면 computers[i][j]를 1로 표현합니다.
computer[i][i]는 항상 1입니다.*/
public class Network {
	
//	public int dfs(int i, int[][] computers, boolean[] visited) {
//		if (visited[i]) {
//			return 0;
//		}
//		visited[i] = true;
//		for (int j = 0; j < computers[i].length; j++) {
//			if (computers[i][j] == 1) {
//				dfs(j, computers, visited);
//			}
//		}
//		return 1;
//	}
//
//	public int solution(int n, int[][] computers) {
//		int answer = 0;
//		boolean[] visited = new boolean[n];
//		for (int i = 0; i < n; i++) {
//			if (!visited[i]) {
//				answer += dfs(i, computers, visited);
//			}
//		}
//		return answer;
//	}
	
	public int solution1(int n, int[][] computers) {
        
		int result = 0;
		boolean[] chk = new boolean[n];
		
		for(int i=0; i<n; i++) {
			if(!chk[i]) {
				result += connCheck(i,computers,chk);
			}
		}
        
        return result;
    }
	
	public int connCheck(int i, int[][] computers, boolean[] chk) {
		
		if(chk[i]) return 0;
		chk[i] = true;
		
		for(int j=0; j<computers[i].length; j++) {
			if(computers[i][j]==1) {
				connCheck(j,computers,chk);
			}
		}
		return 1;
	}

	class Solution {
	public int solution(int n, int[][] computers) {
	        int answer = 0;
	        boolean[] chk = new boolean[n];
	        
	        for(int i=0; i<n; i++){
	            answer += compCom(i, computers, chk);
	        }
	        
	        return answer;
	    }
	    
	    public int compCom(int n, int[][] computers, boolean[] chk){
	        
	        if(chk[n]== true) return 0;
	        chk[n] = true;
	        for(int j=0; j<computers[n].length; j++){
	            if(computers[n][j]==1) {
	                compCom(j,computers, chk);
	            }
	        }
	        return 1;
	    }
	}
	
	
	public static void main(String[] args) {

//		int n=3;	
//		int[][] computers =	{{1, 1, 0}
//							,{1, 1, 0}
//							,{0, 0, 1}};	/*2*/
		int n=3;	
//		int[][] computers =	{{1, 1, 0}, {1, 1, 1}, {0, 1, 1}};	/*1*/
		int[][] computers =	{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};	/*3*/

		
		
		Network network = new Network();
		int rtn = network.solution1(n, computers);
		
		System.out.println("결과:"+rtn);
	}

}
