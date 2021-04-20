package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: Auto-generated Javadoc
public class StreamTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		int[] arrInt = {3,2,5,1,4};
		String[] arrStr = {"B","A","D","C"};
		List<Integer> liInt = Arrays.asList(6,8,5,7,4);
		List<String> liStr = Arrays.asList("Java", "Scala", "Groovy", "Python", "Go", "Swift");
		
		StreamTest streamTest = new StreamTest();
		
		List<Integer> rtnLiInt = streamTest.convArrIntToLiInteger(arrInt);	
		List<String> rtnLiStr = streamTest.convArrStrToLiStr(arrStr);
		int[] rtnArrInt = streamTest.convLiIntegerToArrInt(liInt);
		String[] rtnArrStr = streamTest.convLiStrToArrStr(liStr);
		
		List<Integer> rtnDistLi = new ArrayList<>(rtnLiInt);
		rtnDistLi.addAll(liInt);
		rtnDistLi = streamTest.distList(rtnDistLi);
				
		List<Integer> rtnPlusAll = streamTest.plusAll(liInt,3);
		
//		System.out.println(streamTest.sum(arrInt));
		
		int[] rtnSortInt = streamTest.sortInt(liInt);
		List<Integer> rtnFiltUpInt = streamTest.filterUpLiInt(liInt, 5);
		
		// 필터링
		String[] sFiltStWith = liStr.stream().filter((s) -> {
			return s.startsWith("S");
		}).toArray(String[]::new);
		
		
		
		//출력
//		streamTest.out(sFiltStWith);
	}
	
	

	/**
	 * int 배열 -> Integer List.
	 * @param arrInt 　
	 * @return List<Integer>
	 */
	public List<Integer> convArrIntToLiInteger(int[] arrInt){
		
		return Arrays.stream(arrInt).boxed().collect(Collectors.toList());
	}
	
	/**
	 * String 배열 -> String List (정렬).
	 * @param arrStr 　
	 * @return List<String>
	 */
	public List<String> convArrStrToLiStr(String[] arrStr){
		
		return Arrays.stream(arrStr).sorted().collect(Collectors.toList());
	}
	
	/**
	 *  Integer List -> int 배열.
	 * @param liInt 　
	 * @return int[]
	 */
	public int[] convLiIntegerToArrInt(List<Integer> liInt) {
		
		return liInt.stream().mapToInt(i->i).toArray();
	}
	
	/**
	 *  String List -> String 배열.
	 * @param liStr 　
	 * @return String[]
	 */
	public String[] convLiStrToArrStr(List<String> liStr) {
		
		return liStr.stream().toArray(String[]::new);
	}
	
	/**
	 *  중복값 제거.
	 *
	 * @param <T> 　
	 * @param li 　
	 * @return List<T>
	 */
	public <T> List<T> distList(List<T> li) {
		
		return li.stream().distinct().collect(Collectors.toList());
	}
	
	/**
	 *  List 전체에 숫자 더하기.
	 *
	 * @param li void
	 * @param sumI 　
	 * @return List<Integer> 　
	 */
	public List<Integer> plusAll(List<Integer> li, int sumI) {
		
		return li.stream().map(i->i+sumI).collect(Collectors.toList());
	}
	
	/**
	 * Integer List 정렬 후 int 배열로 변환.
	 * @param li 　
	 * @return int[]
	 */
	public int[] sortInt(List<Integer> li){
		return li.stream().sorted().mapToInt(i->i).toArray();
	}
	
	/**
	 * 배열 전체 합.
	 * @param arrInt 　
	 * @return int 　
	 */
	public int sum(int[] arrInt) {
		return IntStream.of(arrInt).sum();
	}
	
	
	/**
	 * 배열값 up 이상 필터링
	 * @param liInt 　
	 * @param up 　
	 * @return List<Integer> 　
	 */
	public List<Integer> filterUpLiInt(List<Integer> liInt, int up){
		return liInt.stream().filter(i->i>up).collect(Collectors.toList());
	}
	
	
	
	/**
	 * Out.
	 *
	 * @param <T> 　
	 * @param li 　
	 */
	//List 값 출력
	public <T> void out(List<T> li) {
		
		for(T e : li) System.out.print(e +" ");
		System.out.println();
	}
	
	/**
	 * Out.
	 *
	 * @param <T> 　
	 * @param arr 　
	 */
	//배열 값 출력
	public <T> void out(T[] arr){
		for(T e : arr) System.out.print(e +" ");
		System.out.println();
	}
	
	/**
	 * Out.
	 *
	 * @param arrI 　
	 */
	//배열 int 값 출력
	private void out(int[] arrI) {
		for(int e : arrI) System.out.print(e +" ");
		System.out.println();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
