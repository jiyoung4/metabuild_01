package next.com;


public class Queue {
	
	Object[] queueArray;
	int size;
	int back;
	int front = 0;

	
	public Queue(int number) {
		this.back = -1;
		this.size=number;
		this.queueArray = new Object[size];

	}
	
	// test_생성확인
	public void print() {
		for (int i = size-1 ; i >= 0 ; i--) {
			System.out.println(queueArray[i]);
			//System.out.println(i);
		}
	}

	
	public void enqueue(Object inData) {
		
		try {
			queueArray[back+1] = inData;
			back++;
		} catch (ArrayIndexOutOfBoundsException e) {
//			e.printStackTrace();
			System.out.println("가득차있습니다.");
		}
	
	}
	
	public void dequeue() {
		
		try {
			Object out = queueArray[front];
			System.out.println("dequeue값: "+out);
			
			for (int i = 1; i < queueArray.length; i++) {
				Object temp = queueArray[i];
				queueArray[i-1] = temp;
			}
			queueArray[size-1]=null;
			
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("비어있습니다.");
		}
		
	}
	
	public void peek() {
		
		Object peekData;
		
		try {
			peekData = queueArray[back];
			System.out.println(peekData);
		} catch (Exception e) {
		//	e.printStackTrace();
			System.out.println("비어있습니다.");
		}
		

	}

		
	} 
	


