package programers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//K번째 수
public class KthNumber {
	
	public int[] solution2(int[] array, int[][] commands) {
		int[] result = new int[commands.length];
		int ai = 0;
		
		for(int[] c : commands) {
			ArrayList<Integer> list = new ArrayList<>();
			for(int i=c[0]-1; i<c[1]; i++) {
				list.add(array[i]);
			}
			Collections.sort(list);
			result[ai++] = list.get(c[2]-1);
		}
		
		return result;
	}
	
	public int[] solution3(int[] array, int[][] commands) {
		int[] result = new int[commands.length];
		int ai = 0;
		
		for(int[] c : commands) {
			int[] arr = Arrays.copyOfRange(array, c[0]-1, c[1]);
            Arrays.sort(arr);
            result[ai++] = arr[c[2]-1];
		}
		
		return result;
	}
	
	/*최초*/
	public int[] solution1(int[] array, int[][] commands) {
        int[] answer = new int[commands.length];
	        
        for(int i=0; i<commands.length; i++) {
            int[] arr = Arrays.copyOfRange(array, commands[i][0]-1, commands[i][1]);
            Arrays.sort(arr);
            answer[i] = arr[commands[i][2]-1];
        }

        return answer;
    }
	
	public static void main(String[] args) {
		
		int[] array = {1, 5, 2, 6, 3, 7, 4};	
		int[][] commands = {{2, 5, 3}, {4, 4, 1}, {1, 7, 3}};
		/*[5, 6, 3]*/
		
		KthNumber kthNumber = new KthNumber();
		int[] result = kthNumber.solution1(array, commands);
		
		for(int i : result) System.out.print(i+" ");
		
	}

}
