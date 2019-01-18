package next.com;

public class MainQueue {

	public static void main(String[] args) {

		Queue newQ = new Queue(5);
		newQ.enqueue(1);
		newQ.enqueue(2);
		newQ.enqueue(3);
		newQ.enqueue(4);
		System.out.println("--Back--");
		newQ.peek();
		System.out.println("------------");
		newQ.enqueue(5);
		newQ.enqueue(6);
		
		// stack 생성 확인
		System.out.println("==arryQueue==");
		newQ.print();
		System.out.println("=============");
		
		newQ.dequeue();
		
		// stack 생성 확인
		System.out.println("==arryQueue==");
		newQ.print();
		System.out.println("=============");
		
	}

}
