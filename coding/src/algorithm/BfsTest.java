/*너비우선탐색 [Breadth-first search]
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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BfsTest {

	@Test
	void testBfs() {
		Bfs bfs = new Bfs(9);
		bfs.connectNode(0, 1);
		bfs.connectNode(1, 2);
		bfs.connectNode(1, 3);
		bfs.connectNode(2, 4);
		bfs.connectNode(2, 3);
		bfs.connectNode(3, 4);
		bfs.connectNode(3, 5);
		bfs.connectNode(5, 6);
		bfs.connectNode(5, 7);
		bfs.connectNode(6, 8);
		
		bfs.bfs();
	}



}
