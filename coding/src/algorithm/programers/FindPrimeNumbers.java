package algorithm.programers;

import java.util.HashSet;
import java.util.Set;

/*소수찾기*/
/*한자리 숫자가 적힌 종이 조각이 흩어져있습니다. 흩어진 종이 조각을 붙여 소수를 몇 개 만들 수 있는지 알아내려 합니다.

각 종이 조각에 적힌 숫자가 적힌 문자열 numbers가 주어졌을 때, 종이 조각으로 만들 수 있는 소수가 몇 개인지 return 하도록 solution 함수를 완성해주세요.

제한사항
numbers는 길이 1 이상 7 이하인 문자열입니다.
numbers는 0~9까지 숫자만으로 이루어져 있습니다.
"013"은 0, 1, 3 숫자가 적힌 종이 조각이 흩어져있다는 의미입니다.*/
public class FindPrimeNumbers {
	
	public int solution(String numbers) {
		Set<Integer> set = new HashSet<>();
		int rtn = 0;
		
		//숫자 조합 생성
		makeNum("",numbers,set);
		
		for(Integer i : set) {
			//소수 체크
			if(chkPrimeNumber(i)) rtn++;
		}
		
		return rtn;
	}
	
	//숫자 조합 생성
	public void makeNum(String Prefix, String Pieces, Set<Integer> set) {
		
		for(char Piece : Pieces.toCharArray()) {
			String CombPiece = Prefix + Piece;	//숫자 조합
			String RestPiece = Pieces.replaceFirst(String.valueOf(Piece), "");	//남은 조각중 사용 조각 제거
			set.add(Integer.valueOf(CombPiece));//조합 숫자 등록
			makeNum(CombPiece,RestPiece,set); //재귀 호출
		}
	}
	
	//소수 체크
	public boolean chkPrimeNumber(Integer number) {
		
		if(number < 2) return false;
		
		//1과 본인 외의 숫자로 나눠지는 경우 
		for(int i=2; i<number; i++) {
			if(number%i==0) return false;
		}
		
		return true;
	}
	
	
	
	/*최초*/
    public int solution0(String numbers) {
        int answer = 0;
        HashSet<Integer> set = new HashSet<>();
        
        permutation("",numbers,set);
        
        for(int i:set) if(check(i)) answer++;
        
        return answer;
    }
    
    //순열
	public static void permutation(String prefix, String str, HashSet<Integer> set) {
		int n = str.length();
		if(prefix.length()>0) set.add(Integer.valueOf(prefix));
		
		for(int i=0; i<n; i++) {
			permutation(prefix+str.charAt(i)
						,str.substring(0,i) + str.substring(i+1, n)
						,set);
		}
	}
	
	//소수 체크
	static Boolean check(int num) {
		if(num<2) return false;
		
		for(int i=2;i<num;i++) {
			if(num%i==0 && num!=i) return false;
		}
		return true;
	}
	
	
	
	public static void main(String[] args) {
	
//		String numbers = "17";	/*3*/
		String numbers = "011";	/*2*/
		
		FindPrimeNumbers findPrimeNumbers = new FindPrimeNumbers();
		int rtn = findPrimeNumbers.solution(numbers);
		
		System.out.println(rtn);
	}

}
