package meta.com;

import java.io.File;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLFiles {
	
	// XPath 생성
	XPath xpath = XPathFactory.newInstance().newXPath();
	// Utils 생성
	Utils util = new Utils();
	
	/*FILE_ID 가져오기*/
	public String[] selectFileId() throws Exception {
		
		String basePathname = "C:\\A_XMLFILES\\T_BASEFILE_TB.xml";
		Document documentBase = util.mkDocument(basePathname);
		
		// FILE_ID 가져오기 (NodeList)
		NodeList fileIds = (NodeList) xpath.evaluate("//ROW/FILE_ID", documentBase, XPathConstants.NODESET);
		
		// FILE_ID 값 배열
		String[] fileIdsArr = new String[fileIds.getLength()];
		
			for (int i = 0; i < fileIds.getLength(); i++) {
				//System.out.println(fileIds.item(i).getTextContent());
				fileIdsArr[i] = fileIds.item(i).getTextContent();
			}
			return fileIdsArr;
	}
	
	
	
	/*정보 수정 후  파일 생성하기*/
	public void changeInfo(String[] fileIds) {
		
		String pid = "";//F_(FILE_ID)_TB.xml 에서의 P_ID
		
		for (int i = 0; i < fileIds.length; i++) {
		
			String fPathname = "C:\\A_XMLFILES\\F_"+fileIds[i]+"_TB.xml";
			String pPathname = "C:\\A_XMLFILES\\P_"+fileIds[i]+"_TB.xml";
			
			//XML_F
			Document documentF = util.mkDocument(fPathname);
			//XML_P
			Document documentP = util.mkDocument(pPathname);
			
			try {
				
				Transformer xformer = TransformerFactory.newInstance().newTransformer();
				
				// 각각의 FILE의 모든 ROW 추출
				NodeList fRowList = (NodeList) xpath.evaluate("TABLE/ROWS/ROW", documentF, XPathConstants.NODESET);
				NodeList pRowList = (NodeList) xpath.evaluate("TABLE/ROWS/ROW", documentP, XPathConstants.NODESET);
				
				for (int j = 0; j < fRowList.getLength(); j++) {
					
					// NodeList의 item 중 'SIMIRA_RATE' 추출
					//Node similarRateN = (Node)xpath.compile("SIMILAR_RATE").evaluate(fRowList.item(j), XPathConstants.NODE);
					Node similarRateN = getNodeByTageName(fRowList.item(j), "SIMILAR_RATE");
					
					if(similarRateN!=null)
					{
						int similarRate = Integer.parseInt(similarRateN.getTextContent());
						
						// (SIMILAR_RATE/100)>50 조건  -> 15이상으로 변경
						if((similarRate/100) > 15) {
							
							// 조건에 맞는 경우 P_ID 추출
							//Node fpidN = (Node) xpath.evaluate("//P_ID", fRowList.item(j), XPathConstants.NODE);
							Node fpidN = getNodeByTageName(fRowList.item(j), "P_ID");
							
							if(fpidN!=null) {// P_ID가 없는 경우 제외
								pid = fpidN.getTextContent();
								
								// COMMENT에 입력할 LICENSE_ID 찾기
								String commentLid = findLicenseID(pid, pRowList);
								//System.out.println(commentLid);
								
								if((commentLid != null)&&(commentLid.compareTo("") != 0)) {// LICENSE_ID가 없는 경우 제외 
									//Node commentN = (Node) xpath.evaluate("//COMMENT", fRowList.item(j), XPathConstants.NODE);
									Node commentN = getNodeByTageName(fRowList.item(j), "COMMENT");
									
									// COMMENT 수정
									commentN.setTextContent(commentLid);
								
								}else {
									//System.out.println("LicenseId 없습니다."+"**"+commentLid+"**");
								}
							}else {
								//System.out.println("changeInfo: 일치하는 P_ID가 없습니다.");
							}
						}
					}
				} // rowsRID for문 : END
				
				// 결과 파일 생성
				xformer.transform(new DOMSource(documentF), new StreamResult(new File("C:\\A_XMLFILES\\result\\T_"+fileIds[i]+"_TB.xml")));
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		
		} // FILE_ID for문 :: END
		
	} // changeInfo() :: END
	
	
	/*LICENSE_ID 찾기*/
	public String findLicenseID(String fPid, NodeList pRowList) throws XPathExpressionException {
		String licenseID = "";
		String pPid = "";
		
		for (int i = 0; i < pRowList.getLength(); i++) {
			
			//Node pPidN = (Node) xpath.evaluate("//P_ID", pRowList.item(i), XPathConstants.NODE);
			Node pPidN = getNodeByTageName(pRowList.item(i), "P_ID");
			
			if(pPidN != null) // P_ID 없는 경우 제외
			{
				pPid = pPidN.getTextContent();
				if(fPid.compareTo(pPid) == 0)
				{
					//System.out.println(pPid+"::"+fPid);
					//Node licenseIDN = (Node) xpath.evaluate("//LICENSE_ID", pRowList.item(i), XPathConstants.NODE);
					Node licenseIDN = getNodeByTageName(pRowList.item(i), "LICENSE_ID");
					
					if(licenseIDN != null)
						licenseID = licenseIDN.getTextContent();
					break;
				}
			}
			
		}

		return licenseID;
		
	}// findLicenseID() :: END
	
	
	/*Node에서 해당 tagName에 해당하는 Node를 추출 : xpath 일부 대체*/
	public Node getNodeByTageName(Node parent, String tagName) {
		
		Node child = null;
		NodeList children = parent.getChildNodes();
		
		for(int i = 0; i < children.getLength(); i++) {
			
			if(tagName.compareTo(children.item(i).getNodeName()) == 0)
			{
				child = children.item(i);
				break;
			}
		}
		
		return child;
	}// getNodeByTageName() :: END

}
