package next.com;

import java.util.Arrays;

public class Stack {
	
	// Stack: 선형자료구조로 제한된 데이터 구조를 가지고 있다.(LIFO)
	
	// 크기
	private int size;
	// stack(배열 사용)
	Object[] stackArray;
	// 최상위 값
	private int top;
	
	// 생성자를 이용하여 초기 배열 생성
	public Stack(int number) {
		this.top = -1;
		this.size=number;
		this.stackArray = new Object[size];

	}
	
	// test_생성확인
	public void print() {
		for (int i = size-1 ; i >= 0 ; i--) {
			System.out.println(stackArray[i]);
			//System.out.println(i);
		}
	}
	
	// stack 기능: push(), pop()
	
	// push(): 값 넣기, 예외처리: overflow
	public void push(Object inData) {
	
			try {
				stackArray[top+1] = inData;
				top++;
			} catch (ArrayIndexOutOfBoundsException e) {
//				e.printStackTrace();
				System.out.println("가득차있습니다.");
			}
		
	}
	
	//pop(): 값 뺴기, 예외처리: underflow
	public void pop() {
		
		try {
			stackArray[top]=0;
			top--;
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("비어있습니다.");
		}

	}
	
	public void peek() {
		
		Object peekData;
		
		try {
			peekData = stackArray[top];
			System.out.println(peekData);
		} catch (Exception e) {
		//	e.printStackTrace();
			System.out.println("비어있습니다.");
		}
		

	}
	

}
