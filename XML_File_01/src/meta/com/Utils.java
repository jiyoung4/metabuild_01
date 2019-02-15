package meta.com;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class Utils {
	
	/*Document 파일 생성*/
	public Document mkDocument(String pathname) {
		
		File baseXML = new File(pathname);
		Document baseDocument=null;
		
		try {
			
			baseDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(baseXML);
			baseDocument.getDocumentElement().normalize();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return baseDocument;
	}

}
