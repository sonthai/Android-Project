package calpoly.csc.idgoback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

public class DataHandler implements ContentHandler {
	private ArrayList<ItemInfo> list = new ArrayList<ItemInfo>();
	private ArrayList<String> checkList = new ArrayList<String>();
	private boolean isLocation = false;
	private boolean isRegion = false;
	private String currLon, currLat;
	String currText="";

	public ItemInfo item;

	public void loadData(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(url);

		try {

			HttpResponse httpResponse = client.execute(httpRequest);
			final int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(),
						"Error => " + statusCode + " => for URL " + url);
				return;
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			try {
				//create a parser
				SAXParserFactory parseFactory = SAXParserFactory.newInstance();
				SAXParser xmlParser = parseFactory.newSAXParser();   

				//get an XML reader
				XMLReader xmlIn = xmlParser.getXMLReader();

				//instruct the app to use this object as the handler
				xmlIn.setContentHandler(this);

				InputStreamReader xmlStream = new InputStreamReader(httpEntity.getContent());

				//build a buffered reader
				BufferedReader xmlBuff = new BufferedReader(xmlStream);

				//parse the data
				xmlIn.parse(new InputSource(xmlBuff));
			}
			catch(SAXException se) { 
				Log.e("SAX Error", se.getMessage()); 
			}
			catch(IOException ie) { 
				Log.e("Input Error",ie.getMessage());
			}
			catch(Exception oe) { 
				Log.e("Unspecified Error",oe.getMessage()); 
			}

		}
		catch (IOException e) {
			httpRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL =>" + url, e);
		}

		return;
	}

	public void startDocument() {
		//Log.i("DataHandler","Start of XML document");
	}

	public void endDocument() {
		//Log.i("DataHandler","End od XML document");
	}

	public void startElement(String uri, String name, String qName, Attributes atts) {
		if(qName.equals("location")) {
			item = new ItemInfo();
			item.id = atts.getValue("id");
			isLocation = true;
		}
		
		if (qName.equals("region"))
			isRegion = true;
	}

	public void endElement (String uri, String name, String qName)
	{
		if (isLocation) {
			if (qName.equals("latitude")) {
				item.lat = currText;
			}
			
			if (qName.equals("rating")) {
				/* Divide current rating from city grid by 2 */
				if (!currText.equals(""))
					item.rate = Float.parseFloat(currText)/2;
				isLocation = false;
			}

			if (qName.equals("longitude")) {
				item.lon = currText;			
			}
			
			if (qName.equals("name")) {
				if (!checkList.contains(currText.toLowerCase(Locale.US))) {
					item.name =  currText.toUpperCase(Locale.US);
					list.add(item);
					checkList.add(item.name.toLowerCase(Locale.US));
				}
			}
		}
		
		if (isRegion) {
			if (qName.equals("latitude")) {
				currLat = currText;
			}
			
			if (qName.equals("longitude")) {
				currLon = currText;
				isRegion = false;
			}
		}

		currText= "";
	}

	public void characters (char ch[], int start, int length)
	{
		for(int i = start; i<start+length; i++) {
			currText += ch[i];
		}
	}

	public String formatString(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		String words[] = s.toLowerCase(Locale.US).split("\\ ");
		for (String st: words) {
			sb.append(Character.toUpperCase(st.charAt(0)))
			.append(st.substring(1));
			sb.append(" ");
		}

		return sb.toString();
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {

	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {

	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {

	}

	@Override
	public void setDocumentLocator(Locator arg0) {

	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {

	}


	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {

	}

	public ArrayList<ItemInfo> getList() {
		return list;
	}
	
	public String getCurrLat () {
		return currLat;
	}
	
	public String getCurrLong () {
		return currLon;
	}
	
	public void setDistance(int index, String value) {
		list.get(index).distance = value;
	}


	public class ItemInfo{
		public String name = "";
		public String id = "";
		public String distance = "";
		public float rate = 0;
		public String lon="";
		public String lat="";
	}

}
