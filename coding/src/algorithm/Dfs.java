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

import java.util.LinkedList;
import java.util.Stack;


public class Dfs {
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
	
	Dfs(int size) {
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
	
	void dfs(int start) {
		Stack<Node> stk = new Stack<Node>();
		
		Node root = nodes[start];
		root.chk = true;
		stk.add(root);
		
		while(!stk.isEmpty()) {
			Node node = stk.pop();
			
			for(Node cNode : node.connNode) {
				if(!cNode.chk) {
					cNode.chk = true;
					stk.add(cNode);
				}
			}
			
			System.out.println(node);
		}
	}
	
	void dfs() {
		dfs(0);
	}
	
	/*재귀*/
	void recursive(Node root) {
		
		root.chk = true;

		System.out.println(root.toString());
		
		for(Node cNode : root.connNode) {
			if(!cNode.chk) {
				recursive(cNode);
			}
		}
	}

	public void recursive() {
		recursive(nodes[0]);
	}
	
	
}
