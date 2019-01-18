package next.com;

public class Sort {
	
	/*
	 * 선택정렬(selection sort) - 최소값/최대값
	 * 최소값: 제일 작은 수를 찾아서 맨앞에 위치 시키면서 정렬
	 * 최대값: 제일 큰 수를 찾아서 맨 뒤에 위치 시키면서 정렬
	 */
	public void selectionSort(int[] arrS) {
		
		// 1. 최소값 찾기
		// 2. 맨앞의 값과 교환
		// 3. 앞쪽의 배열 제외
		
		int size = arrS.length;
		int min=0; // 최소값의 index
		int temp=0; // 값 변경시 담아놓을 곳
		int n=0; // 정렬하는데 걸린 횟수
		
		for (int i = 0; i < size-1; i++) {

			for (int j = i+1; j < size; j++) {// 맨앞부터 하나씩 제외
			
				if(arrS[min]>arrS[j]) {// 최소값
					min=j;
				}
			}
			temp = arrS[min];
			arrS[min] = arrS[i];
			arrS[i] = temp;
			n++;
		}
		
		for (int i = 0; i < arrS.length; i++) {
			System.out.printf("%d ",arrS[i]);
		}
		
		System.out.printf("\n %d 회전 \n", n);


	}// selectionSort()
	
	/*
	 * 삽입정렬(insertion sort)
	 * 현재 위치에서(두 번째 자료부터 시작),
	 * 그 이하의 배열들을 비교하면서 자신이 들어갈 위치를 찾아 그 위치에 삽입
	 */
	public void insertionSort(int[] arrS) {
		
		int size = arrS.length;
		int temp = 0;
		int move = 0;// 범위 설정해 주는 index
		int count = 0;
		
		for (int i = 1; i < size; i++) { // 시작점(두번째 요소) 설정, 차례대로 비교
			
			// 선정된 값 temp에 담기
			temp = arrS[i];
			// 선정된 값 앞부분까지의 index 설정
			move = i-1;
			
			//범위 안에서 비교(arrS[0]~arrS[i-1])
			// 1) 범위의 기준이 되는 move index는 0보다 크거나 같아야함
			// 2) temp의 위치를 찾기 위해 arrS[move]와 비교
			
			while ((move >= 0)&&(arrS[move]>temp)) {
				// temp에 temp보다 큰값 넣어주기 
				arrS[move+1]= arrS[move];
				// while문 빠져 나가기
				move--;
				count++;
			}
			
			// temp값을 해당 위치에 배치
			arrS[move+1] = temp;
			
		}
		
		for (int i = 0; i < arrS.length; i++) {
			System.out.printf("%d ",arrS[i]);
		}
		
		System.out.printf("\n %d 회전 \n", count);
					
		
		
		
	}
	
	/*
	 * 버블정렬(bubble sort)
	 * 인접한 두 요소를 비교하여 정렬
	 */
	public void bubbleSort(int[] arrS) {
		
		int size = arrS.length;
		int temp; // 값 변경시 담아놓을 곳
		int count=0; // 정렬하는데 걸린 횟수
		
		for(int i = 0; i < size-1 ; i++) {// 앞쪽 배열값
		
			for (int j = i+1; j < size ; j++) {// 뒤쪽 배열값
				
				if(arrS[i]>arrS[j]) { // 두개의 값을 비교해서 정열
					
					temp = arrS[j];
					arrS[j] = arrS[i];
					arrS[i] = temp;
					count++;
				}
				
			}
		}
		
		
		for (int i = 0; i < arrS.length; i++) {
			System.out.printf("%d ",arrS[i]);
		}
		
		System.out.printf("\n %d 회전 \n", count);
	}
	
	

}
