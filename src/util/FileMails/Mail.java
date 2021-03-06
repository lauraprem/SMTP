package util.FileMails;

import java.io.Serializable;
import java.util.ArrayList;

public class Mail implements Serializable{
	
	// ATTRIBUTS
	private ArrayList<String> listHeader;
	private String endLine = "\r\n";
	private ArrayList<String> listLine;
	private String endMsg = ".\r\n";// caractaire 10 et 13
	// message lu verif
	// message a supprimer
	
	// CONSTRUCTEURS
	public Mail() {
		super();
		
		this.listHeader = new ArrayList<String>();
		this.listLine = new ArrayList<String>();
	}
	
	public Mail(ArrayList<String> listHeader, ArrayList<String> listLine) {
		super();
		
		this.listHeader = listHeader;
		this.listLine = listLine;
	}
	
	// METHODES
	public ArrayList<String> getListHeader() {
		return listHeader;
	}



	public void setListHeader(ArrayList<String> listHeader) {
		this.listHeader = listHeader;
	}

	public void addHeader(String header) {
		this.listHeader.add(header);
	}

	public String getEndMsg() {
		return endMsg;
	}



	public void setEndMsg(String endMsg) {
		this.endMsg = endMsg;
	}



	public ArrayList<String> getListLine() {
		return listLine;
	}



	public void setListLine(ArrayList<String> listLine) {
		this.listLine = listLine;
	}

	public void addLine(String line) {
		this.listLine.add(line);
	}

	public String getEndLine() {
		return endLine;
	}



	public void setEndLine(String endLine) {
		this.endLine = endLine;
	}
	
	@Override
	public String toString() {
		String stringMail="";
		
		for (int i = 0; i < listHeader.size(); i++) {
			stringMail = stringMail + listHeader.get(i)+endLine;
		}
		stringMail = stringMail + endLine;
		for (int i = 0; i < listLine.size(); i++) {
			stringMail = stringMail + listLine.get(i)+endLine;
		}
		
		stringMail = stringMail.replaceAll(endLine + endMsg, "\n" + endMsg);
		String endOfString = (stringMail.length() > 1 ? stringMail.substring(stringMail.length() - 2) : "");
		stringMail = stringMail + (endLine.equals(endOfString) ? endMsg : endLine + endMsg);
		
		return stringMail;
	}
	
	public int CalculationSizeMail(){
		int nbOctetEndLine = endLine.getBytes().length;
		int nbOctet = nbOctetEndLine;
		
		for(int i = 0; i < listHeader.size(); i++)
	    {
			nbOctet = nbOctet + listHeader.get(i).length()+nbOctetEndLine;
		}
		nbOctet = nbOctet + nbOctetEndLine;
		for(int i = 0; i < listLine.size(); i++)
	    {
			nbOctet = nbOctet + listLine.get(i).length()+nbOctetEndLine;
		}
		
		return nbOctet;
	}
}
