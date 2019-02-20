package meta.com;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
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

public class MainXML {

	public static void main(String[] args) {
		
		long preUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		long start = System.currentTimeMillis();
		
		MainXML mainXML = new MainXML();
		String fileIdsArr[];
		
		try {
			
			// FILE ID를 찾은 뒤 조건에 맞는 결과 파일 생성하는 메소드 실행
			fileIdsArr = mainXML.selectFileId();
			mainXML.makeResult(fileIdsArr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		long aftUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		
		long useMemory = (aftUseMemory - preUseMemory);
		
		System.out.println("Time: "+String.format("%,d",(end-start)) +"ms");
		System.out.println("Used Memory: "+String.format("%,d", useMemory/1000)+" kbyte");
		
	}
	
	
	/**
	 * 파일의 위치인 'pathname'을 받아서 
	 * Document로 생성해서 반환해주는 메소드
	 * @param pathname
	 * @return Document
	 */
	private Document mkDocument(String pathname) {
		
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
	
	
	/**
	 * T_BASEFILE_TB.xml에서 모든 FILE_ID를 추출하여
	 * 배열로 반환하는 메소드
	 * @return String[]
	 * @throws Exception
	 */
	private String[] selectFileId() throws Exception {
		
		String basePathname = "D:\\A_XMLFILES\\T_BASEFILE_TB.xml";
		Document documentBase = mkDocument(basePathname);
		XPath xpath = XPathFactory.newInstance().newXPath();
		
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
	
	
	/**
	 * 조건에 맞는 경우 결과 파일 생성
	 * @param fileIds
	 */
	private void makeResult(String[] fileIds) {
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		for (int i = 0; i < fileIds.length; i++) {
		
			String fPathname = "D:\\A_XMLFILES\\F_"+fileIds[i]+"_TB.xml";
			String pPathname = "D:\\A_XMLFILES\\P_"+fileIds[i]+"_TB.xml";
			
			//XML_F
			Document documentF = mkDocument(fPathname);
			//XML_P
			Document documentP = mkDocument(pPathname);
			
			try {
				
				Transformer xformer = TransformerFactory.newInstance().newTransformer();
				
				// 각각의 FILE의 모든 ROW 추출
				NodeList fRowList = (NodeList) xpath.evaluate("TABLE/ROWS/ROW", documentF, XPathConstants.NODESET);
				NodeList pRowList = (NodeList) xpath.evaluate("TABLE/ROWS/ROW", documentP, XPathConstants.NODESET);
				
				for (int j = 0; j < fRowList.getLength(); j++) {
					
					// NodeList의 item 중 'SIMIRA_RATE' 추출
					Node similarRateN = getNodeByTageName(fRowList.item(j), "SIMILAR_RATE");
					
					if(similarRateN!=null){
						chkCondition(similarRateN, fRowList, pRowList, j);
					}
					
				}
				
				// 결과 파일 생성
				xformer.transform(new DOMSource(documentF), new StreamResult(new File("D:\\A_XMLFILES\\result\\T_"+fileIds[i]+"_TB.xml")));
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		
		}
		
	} // changeInfo() :: END
	
	
	/**
	 * '(SIMILAR_RATE/100) > 15'의 조건을 만족하는 경우 
	 * P_ID를 이용하여 LICENSE_ID를 찾은 뒤 COMMENT 변경
	 * @param similarRateN
	 * @param fRowList
	 * @param pRowList
	 * @param j
	 */
	private void chkCondition(Node similarRateN, NodeList fRowList, NodeList pRowList, int j) {
		
		String pid = "";//F_(FILE_ID)_TB.xml 에서의 P_ID
		int similarRate = Integer.parseInt(similarRateN.getTextContent());
		
		// (SIMILAR_RATE/100)>50 조건  -> 15이상으로 변경
		if((similarRate/100) > 15) {
			
			// 조건에 맞는 경우 P_ID 추출
			Node fpidN = getNodeByTageName(fRowList.item(j), "P_ID");
			
			if(fpidN!=null) {// P_ID가 없는 경우 제외
				pid = fpidN.getTextContent();
				
				// COMMENT에 입력할 LICENSE_ID 찾기
				String commentLid = chkPPID(pid, pRowList);
				
				// LICENSE_ID가 없는 경우 제외
				if((commentLid != null)&&(commentLid.compareTo("") != 0)) {
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
	
	
	
	
	/**
	 * 비교해야 하는 F파일의 P_ID값을 P파일의 NodeList(pRowList)
	 * 에서 찾은 뒤 null이 아닌 경우 LICENSEID를 반환하는 메소드
	 * @param fPid
	 * @param pRowList
	 * @return
	 */
	private String chkPPID(String fPid, NodeList pRowList) {
		// LICENSEID가 없는 경우 빈값으로 반환
		String licenseID = "";
		String pPid = "";
		
		for (int i = 0; i < pRowList.getLength(); i++) {
			
			Node pPidN = getNodeByTageName(pRowList.item(i), "P_ID");
			
			if(pPidN != null)
			{
				pPid = pPidN.getTextContent();
				
				if(fPid.compareTo(pPid) == 0)
				{
					licenseID = findLicenseID(pRowList, i);
					break;
				}
			}
			
		}

		return licenseID;
		
	}// findLicenseID() :: END
	
	
	/**
	 * NodeList에서 LICENSE_ID를 찾은뒤 
	 * null이 아닌 경우에만 그 값을 반환하는 메소드
	 * @param pRowList
	 * @param i
	 * @return String
	 */
	private String findLicenseID(NodeList pRowList, int i) {
		
		Node licenseIDN = getNodeByTageName(pRowList.item(i), "LICENSE_ID");
		
		if(licenseIDN != null);
			String licenseID = licenseIDN.getTextContent();
		
		return licenseID;
	}
	
	
	
	/**
	 * Node의 ChildNodes에서 찾고자 하는 Node들을 tagName으로 찾는 메소드
	 * (xpath를 이용하여 Node를 찾는 기능과 동일) 
	 * @param parent
	 * @param tagName
	 * @return Node
	 */
	private Node getNodeByTageName(Node parent, String tagName) {
		
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
