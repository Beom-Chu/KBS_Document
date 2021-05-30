/*깊이우선탐색 [Depth-first search]
 출발노드에서 목표노드까지의 최단 길이 경로를 보장한다.
 
  0
 /
1---3   7
|  /|\ /
| / | 5
|/  |  \
2---4   6--8

 */
package algorithm;

import org.junit.jupiter.api.Test;

class DfsTest {

	@Test
	void testDfs() {

		Dfs dfs = new Dfs(9);
		dfs.connectNode(0, 1);
		dfs.connectNode(1, 2);
		dfs.connectNode(1, 3);
		dfs.connectNode(2, 4);
		dfs.connectNode(2, 3);
		dfs.connectNode(3, 4);
		dfs.connectNode(3, 5);
		dfs.connectNode(5, 7);
		dfs.connectNode(5, 6);
		dfs.connectNode(6, 8);
		
//		dfs.dfs();
		dfs.recursive();
	}

}
