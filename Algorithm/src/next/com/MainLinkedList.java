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
		
		nodeA.MiddelAdd("AB");
		System.out.println(nodeA);
		
		nodeA.NodeDelete();
		System.out.println(nodeA);
		
		nodeA.DataChk();
		
	}
	
}
