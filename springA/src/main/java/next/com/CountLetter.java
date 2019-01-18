package next.com;

import java.util.StringTokenizer;

public class CountLetter implements Count {

	/*
	 * 문장의 단어 개수를 반환하는 메소드
	 */
	public int count(String letters) {
		int cntLetters=0;
		
		StringTokenizer st = new StringTokenizer(letters);
		cntLetters = st.countTokens();
		
		return cntLetters;
	}
	
}
