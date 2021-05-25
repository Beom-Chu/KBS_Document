package algorithm.programers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FarthestNodeTest {

	@DisplayName("가장 먼 노드 테스트")
	@Test
	void testSolution() {
		
		FarthestNode farthestNode = new FarthestNode();
		
		farthestNode.solution(6, new int[][]{{3, 6}, {4, 3}, {3, 2}, {1, 3}, {1, 2}, {2, 4}, {5, 2}});
	}

}
