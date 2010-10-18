package wirevg.apps.viewer.resources.handlers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class trendHandler extends DefaultHandler {
	
	boolean inPhrase = false;
	public ArrayList<String> phrases = new ArrayList<String>();
	
	public ArrayList<String> getTrends() throws Exception
	{
		URL url = new URL(ApiData.BaseUrl + "resource=trends");
		SAXParserFactory fac = SAXParserFactory.newInstance();
		SAXParser sp = fac.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		xr.setContentHandler(this);
		xr.parse(new InputSource(url.openStream()));
		return this.phrases;
		
	}
	
	@Override
	public void startElement(String uri, String localname, String qName , Attributes attributes)
	{
		if (localname.trim().equalsIgnoreCase("phrase")) {
			inPhrase = true;
		}
		
	}
	
	public void startDocument(){
		phrases = new ArrayList<String>();
	}
	
	public void endElement(String uri, String localName, String qName)
	{
		if (localName.trim().equalsIgnoreCase("phrase")) {
			inPhrase = false;
		}
	}
	
	public void characters(char chars[], int start, int length)
	{
		String outPh = (new String(chars).substring(start, start + length));
		if(inPhrase)
		{
			outPh = outPh.trim();
			phrases.add(outPh);
		}
	}
	

}
