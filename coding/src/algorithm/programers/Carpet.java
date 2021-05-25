package algorithm.programers;

/*카펫*/
/*Leo는 카펫을 사러 갔다가 아래 그림과 같이 중앙에는 노란색으로 칠해져 있고 테두리 1줄은 갈색으로 칠해져 있는 격자 모양 카펫을 봤습니다.
□□□□
□■■□
□□□□
Leo는 집으로 돌아와서 아까 본 카펫의 노란색과 갈색으로 색칠된 격자의 개수는 기억했지만, 전체 카펫의 크기는 기억하지 못했습니다.

Leo가 본 카펫에서 갈색 격자의 수 brown, 노란색 격자의 수 yellow가 매개변수로 주어질 때 카펫의 가로, 세로 크기를 순서대로 배열에 담아 return 하도록 solution 함수를 작성해주세요.

제한사항
갈색 격자의 수 brown은 8 이상 5,000 이하인 자연수입니다.
노란색 격자의 수 yellow는 1 이상 2,000,000 이하인 자연수입니다.
카펫의 가로 길이는 세로 길이와 같거나, 세로 길이보다 깁니다.*/
public class Carpet {
	
	public int[] solution(int brown, int yellow) {
		
		int[] rtn = new int[2];
		
		int yellHV = (brown/2)-2;	//노란 부분 가로세로 합
		
		for(int H=yellHV-1 , V=1; H>0; H--, V++) {
			if(H*V == yellow) {	//노란 가로*세로 == 노랑 넓이 체크
				rtn[0]=H+2;		//가로(노란가로+갈색가로)
				rtn[1]=V+2;		//세로(노란세로+갈색세로)
				break;
			}
		}
		return rtn;
	}
	
	public static void main(String[] args) {
		
		int[] colors = {10,	2};		/*[4, 3]*/
//		int[] colors = {8,	1 };	/*[3, 3]*/
//		int[] colors = {24,	24};	/*[8, 6]*/
		
		Carpet carpet = new Carpet();
		int[] rtn = carpet.solution(colors[0], colors[1]);
		
		System.out.printf("[%s , %s]", rtn[0],rtn[1]);
	}

}
