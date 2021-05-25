package algorithm.programers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CrainGame {
	
	public int solution(int[][] board, int[] moves) {
       int answer = 0;
		ArrayList<Queue<Integer>> aq = new ArrayList<>();
		Stack<Integer> stk = new Stack<>();
        
		for(int i=0; i<board.length; i++) {
			Queue<Integer> q = new LinkedList<>();
			for(int j=0; j<board.length; j++) {
				int n = board[j][i];
				if(n!=0) q.add(Integer.valueOf(n));
			}
			aq.add(q);
		}
		
		for(int i : moves) {
			if(!aq.get(i-1).isEmpty()) {
				int n = aq.get(i-1).poll();
				if(!stk.isEmpty() && n == stk.peek()) {
					stk.pop();
					answer+=2;
				}else stk.add(n);
			}
		}
		return answer;
	}
	
	public static void main(String[] args) {

		CrainGame cg = new CrainGame();
		int rtn = cg.solution(new int[][]{{0,0,0,0,0},{0,0,1,0,3},{0,2,5,0,1},{4,2,4,4,2},{3,5,1,3,1}}, new int[]{1,5,3,5,1,2,1,4});
		System.out.println(rtn);
	}

}
