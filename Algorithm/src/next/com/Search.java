package next.com;

public class Search {
	
	//조건: 데이터가 정렬되어 있어야 함
	
	public void binarySearch(int arr[], int target) {
		
		// 초기 setting
		int start = 0;// arr[]의 시작
		int end = arr.length-1;// arr[]의 끝
		int mid = end/2; // arr[]중간값
		
		while(start <= end) {
			//중간값 찾기
			mid = (start+end)/2;
			
			//중간값과 찾는값 비교
			if(target > arr[mid]) {
				start = mid+1;
			}else {
				end = mid-1;
			}
			
		}
		
		System.out.printf("찾는 값(%d)은 %d번 째에 있습니다.",target,(mid+1));
		
	}
	
	

}
