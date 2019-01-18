package next.com;

public class MainStack {

	public static void main(String[] args) {

		Stack stack = new Stack(5);
		
		stack.push(2);
		stack.push(3);
		stack.push(4);
		System.out.println("--top Data--");
		stack.peek();
		System.out.println("------------");
		
		stack.push(5);
		stack.push(6); // ArrayIndexOutOfBoundsException
		stack.push(99);
		
		// stack 생성 확인
		System.out.println("==arryStack==");
		stack.print();
		System.out.println("=============");
		
		for (int i = 0; i < 6; i++) {
			stack.pop();
		}
		
	}

}
