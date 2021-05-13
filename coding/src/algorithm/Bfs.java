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

import java.util.LinkedList;
import java.util.Queue;

class Bfs {
	Node[] nodes;
	
	class Node {
		int no;
		boolean chk;
		LinkedList<Node> connNode;
		
		public Node(int no) {
			this.no = no;
			chk = false;
			connNode = new LinkedList<>();
		}

		@Override
		public String toString() {
			return "Node [no=" + no + ", chk=" + chk + "]";
		}
		
	}
	
	Bfs(int size){
		nodes = new Node[size];
		
		for(int i=0; i<size; i++) {
			nodes[i] = new Node(i);
		}
	}
	
	void connectNode(int i1, int i2) {
		Node n1 = nodes[i1];
		Node n2 = nodes[i2];
		if(!n1.connNode.contains(n2)) n1.connNode.add(n2);
		if(!n2.connNode.contains(n1)) n2.connNode.add(n1);
	}
	
	void bfs(int start) {
		System.out.println("bfs");
		Queue<Node> que = new LinkedList<Node>();
		
		Node root = nodes[start];
		root.chk = true;
		que.add(root);
		
		while(!que.isEmpty()) {
			Node node = que.poll();
			
			for(Node cNode : node.connNode) {
				if(!cNode.chk) {
					cNode.chk = true;
					que.add(cNode);
				}
			}
			
			System.out.println(node.toString());
		}
	}
	
	void bfs() {
		bfs(0);
	}
}
