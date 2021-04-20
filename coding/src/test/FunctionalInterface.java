package test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FunctionalInterface {

	public static void main(String[] args) {
		
		FunctionalInterface fi = new FunctionalInterface();
//		fi.function();
//		fi.predicate();
//		fi.consumer();
		fi.supplier();
	}
	
	/*Function<T, R> 형태로 T는 파라미터 타입, R은 리턴타입이다. 추상메소드 apply() 를 가진다.*/
	public void function() {
		
		Function<String, String> f1 = (s)-> "func : "+s;
		String rtnS = f1.apply("ABC");
		System.out.println(rtnS);
		
		
		Function<String, Integer> f2 = (s)-> {
			Integer i = 0;
			try {
				 i = Integer.valueOf(s);
			}catch(NumberFormatException e) {
				i = 0;
			}catch(Exception e) {
				e.printStackTrace();
			}
			return i;
		};
		int rtnI = f2.apply("A");
		System.out.println(rtnI);
	}
	
	/*Predicate<T, Boolean> 형태로, boolean을 리턴하는 함수형 인터페이스다. 추상메소드 test() 를 가진다.*/
	public void predicate() {
		Predicate<Integer> p1 = (i) -> i > 3;
		System.out.println(p1.test(2));
		System.out.println(p1.test(4));
	}
	
	/*Consumer는 < T > 형태로 리턴이 없는 형태이다. 추상메소드 accept() 를 가진다.*/
	public void consumer() {
		Consumer<String> c = (s) -> System.out.println(s);
		c.accept("abc");
		c.accept("hello");
	}
	
	/*Supplier는 < R > 형태로 파라미터가 없는 형태이다. 추상메소드 get() 를 가진다.이를 이용해 Lazy Evalutioan이 가능하다.*/
	public void supplier() {
		Supplier<String> sup = () -> "supplier";
		String str = sup.get();
		System.out.println(str);
		
		Supplier<String> sup2 = () -> {
			try {
				Thread.sleep(3000);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return "delaySupplier";
		};
		String str2 = sup2.get();
		System.out.println(str2);
		
	}
}
