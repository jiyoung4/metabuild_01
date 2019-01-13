package next.com;

/*
 * 단어 개수를 Console을 통해 출력하는 기능 
 */
public class ToConsole extends Output {

	@Override
	public void write(int cntLetters) {

		System.out.println("단어 개수: "+cntLetters);
		
	}

}
