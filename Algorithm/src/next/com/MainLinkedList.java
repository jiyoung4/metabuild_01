package next.com;

public class MainLinkedList {

	public static void main(String[] args) {
		
		LinkedList nodeA = new LinkedList();
		
		nodeA.FirstAdd("a");
		nodeA.LastAdd("b");
		nodeA.LastAdd("c");
		nodeA.LastAdd("d");
		nodeA.LastAdd("e");
		nodeA.LastAdd("f");
		
		System.out.println(nodeA);
		
		// 리스트의 size 확인
		
		// 넣을 위치 선정 
		
		nodeA.MiddelAdd("AB");
		System.out.println(nodeA);
		
	}
	
}
