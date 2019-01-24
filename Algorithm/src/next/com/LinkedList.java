package next.com;

import java.util.Scanner;

public class LinkedList {
	
	// 구성요소: Node(Data+Pointer) (Head, Tail, Node)
	// 기능: 삽입(앞,뒤,중간), 삭제(앞,뒤,중간), 조회
	
	// Node란 정보와 다음 노드를 가르키는 포인터로 구성, 접근되지 않도록 innerClass로 작성
	private class Node {
		
		// Node의 Data
		private Object data;
		// Node의 다음 노드 
		private Node next;
		
		public Node(Object inData) {
			this.data = inData;
			this.next = null;
		}
		
		
	}
	
	
	
	@Override
	public String toString() {
		
		Node temp = head;
		String allD = "[";
		
		while(temp.next !=null) {
			allD += temp.data +",";
			temp = temp.next;
		}
		
		allD += temp.data;
		
		return allD+"]";
	}


	// Node의 Head와 Tail 
	private Node head;
	private Node tail;
	// LinkedList의 길이
	private int size = 0;
	
	
	// FirstAdd: Head만 생성
	public void FirstAdd(Object insertD) {
		// Node 생성
		Node newNode = new Node(insertD);
		// 첫번째 노드 이므로 다음 노드는 head로 설정
		newNode.next = head;
		// Head로 설정
		head = newNode;
		// 길이증가
		size++;
		
		if(head.next == null) {
			tail = head;
		}
		
	}
	
	// EndAdd
	public void LastAdd(Object insertD) {
		
		if(size==0) {// NullpointException
			FirstAdd(insertD);
		}else {
		
		// Node 생성
		Node newNode = new Node(insertD);
		// 마지막 노드와 연결 
		tail.next = newNode;
		// 마지막 노드 등록
		tail = newNode;
		
		// 길이증가
		size++;
		}
		
	}
	
	// 중간 삽입
	public void MiddelAdd(Object insertD) {
		
		int num = 0;
		Node temp1;
		Node temp2;
		
		// 값을 몇번째에 넣은건지 입력받기
		System.out.println("몇번째에 넣겠습니까?");
		Scanner scan = new Scanner(System.in);
		num = scan.nextInt();
		
		temp1 = head;
		
		// 입력한 값까지 head부터 찾아감
		while (--num != 1) {
			//System.out.println("while: "+num);
			
			// 선택한 값의 직전 Node
			temp1 = temp1.next;
			//System.out.println("temp1: "+ temp1.data);
		}
			// 선택한 값의 Node
			temp2 = temp1.next;
			//System.out.println("temp2: "+ temp2.data);
			
			// 새로 입력할 Node 생성
			Node newNode = new Node(insertD);
			// 직전 Node에서 새로운 Node로 연결
			temp1.next = newNode;
			// 새로운 Node의 next는 선택한 값의 Node
			newNode.next = temp2;
			
		
	}
	
	
	//---------- 삭제
	
	public void NodeDelete() {
		
		int num = 0;
		Node temp1;
		Node deleteN;
		
		// 몇번째를 삭제할 것인지 입력
		System.out.println("몇번째 노드를 삭제하겠습니까?");
		Scanner scan = new Scanner(System.in);
		num = scan.nextInt();
		
		if(num==1) {// 첫번째 노드 삭제시 
			// head의 다음값 담아두기
			temp1 = head.next;
			// head 연결 끊기
			head.next = null;
			// head 새로 등록
			head = temp1;
			
		}else {// 두번째 노드 이후
			temp1 = head;
			// 입력한 값까지 head부터 찾아감
			while (--num != 1){
				System.out.println("while: "+num);
				
				// 선택한 값의 직전 Node
				temp1 = temp1.next;
				System.out.println("temp1: "+ temp1.data);
			}
				// 삭제할 Node
				deleteN = temp1.next;
				System.out.println("삭제 노드 :"+deleteN.data);
				// 삭제할 Node 이전의 링크를 삭제할 Node다음으로 연결
				temp1.next = deleteN.next;
				
				// Node삭제
				deleteN.next = null;
		}
	
			
	}
	
	// -------- 조회
	
	public void DataChk() {
		
		int num = 0;
		Node temp1;
		
		// 조회할 노드 입력
		System.out.println("몇번째 노드를 조회하시겠습니까?");
		Scanner scan = new Scanner(System.in);
		num = scan.nextInt();
		
		temp1 = head;
		// 입력한 값까지 head부터 찾아감
		while (--num != 0){
			//System.out.println("while: "+num);
			
			// 선택한 값의 직전 Node
			temp1 = temp1.next;
		}
		System.out.println("Node data: "+ temp1.data);
		
		
	}
	


	
	

}
