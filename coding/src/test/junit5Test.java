package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class junit5Test {

	@BeforeAll
	static void setup() {
		System.out.println("@BeforeAll");
	}
	
	@BeforeEach
	void init() {
	    System.out.println("@BeforeEach");
	}
	
	
	@Test
	@DisplayName("첫번째 테스트")
	void test() {
		System.out.println("@Test");
	}
	
	@Test
	@DisplayName("두번째 테스트")
	@Disabled("잠시 비활성")
	void test2() {
		System.out.println("@Test2");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5, Integer.MAX_VALUE}) // 5개 인수
	void parameterizedTest(int number) {
		System.out.println(number);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "  "})
	void isBlank_ShouldReturnTrueForNullOrBlankStrings(String input) {
	    System.out.println(input);
	}
	
	@TestFactory
	@RepeatedTest(3)
	void repeatedTest() {
		System.out.println("@RepeatedTest");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("@AfterAll");
	}
	
	@AfterEach
	void afterEach() {
		System.out.println("@AfterEach");
	}
}
