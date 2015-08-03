package calpoly.csc.idgoback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

public class FindDistance implements ContentHandler {
	private boolean isDistance = false;
	private String currText = "";
	private ArrayList<String> distance = new ArrayList<String>();
	public void loadDistance(String url) {
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

	@Override
	public void startDocument() throws SAXException {
		//Log.i("Start","Find distance");

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if(qName.equals("distance"))
			isDistance = true;

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		for(int i = start; i<start+length; i++) {
			currText += ch[i];
		}



	}

	@Override
	public void endDocument() throws SAXException {
		//Log.i("End","Find distance");

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(isDistance) {
			if(qName.equals("text")) {
				distance.add(currText.replace("\n", ""));
				isDistance = false;
			}
			currText ="";
		}
	}
	
	public ArrayList<String> getDistList() {
		return distance;
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {

	}

	@Override
	public void setDocumentLocator(Locator locator) {

	}

	@Override
	public void skippedEntity(String name) throws SAXException {

	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {

	}


}
