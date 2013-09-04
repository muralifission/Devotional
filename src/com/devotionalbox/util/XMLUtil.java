package com.devotionalbox.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class XMLUtil {
	static final double SECOND = 1;
	static final double MINUTE = 60*SECOND;
	static final double HOUR = 60*MINUTE;
	static final double DAY = 24*HOUR;
	static final double MONTH = 30*DAY;
	static final double YEAR = 12*MONTH;
	
	long timeOnServer;
	long timeOnClient;
	
	public final static Document XMLfromString(String xml){

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {        	
			DocumentBuilder db = dbf.newDocumentBuilder();			
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is); 	        
		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());			
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}		       
		return doc;

	}

	/** Returns element value
	 * @param elem element (it is XML tag)
	 * @return Element value otherwise empty String
	 */
	

	public static String getXML(String url){	 
		String line = null;
		try {				
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpPost = new HttpGet(url);	
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity);				
		} catch (UnsupportedEncodingException e) {
			line = "<results status=\"error\"><msg>Can't connect to server - UnsupportedEncodingException</msg></results>";
		} catch (MalformedURLException e) {
			line = "<results status=\"error\"><msg>Can't connect to server - MalformedURLException</msg></results>";
		} catch (IOException e) {
			e.printStackTrace();
			line = "<results status=\"error\"><msg>Can't connect to server - IOException</msg></results>";
		}
		return line;
	}

	public static String getJSON(){	 
		String line = null;
		try {				
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpPost = new HttpGet("http://dl.dropbox.com/u/51542931/books.txt");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity);				
		} catch (UnsupportedEncodingException e) {
			line = "<results status=\"error\"><msg>Can't connect to server - UnsupportedEncodingException</msg></results>";
		} catch (MalformedURLException e) {
			line = "<results status=\"error\"><msg>Can't connect to server - MalformedURLException</msg></results>";
		} catch (IOException e) {
			e.printStackTrace();
			line = "<results status=\"error\"><msg>Can't connect to server - IOException</msg></results>";
		}
		return line;
	}

	public static int numResults(Document doc){		
		Node results = doc.getDocumentElement();
		int res = -1;

		try{
			res = Integer.valueOf(results.getAttributes().getNamedItem("count").getNodeValue());
		}catch(Exception e ){
			res = -1;
		}

		return res;
	}

	public static String getValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);		
		return XMLUtil.getElementValue(n.item(0));
	}
	public final static String getElementValue( Node elem ) {
		Node kid;
		if( elem != null){
			if (elem.hasChildNodes()){
				for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
					if( kid.getNodeType() == Node.TEXT_NODE  ){
						return kid.getNodeValue();
					}
				}
			}
		}
		return "";
	}
	
	public static String getTimeString(long timeOnServer){
		double timeOnClient = System.currentTimeMillis()/1000;
		double diff = timeOnClient-timeOnServer;
		Log.i("Current Time is", String.valueOf(timeOnClient));
		Log.i("Server Time is ", String.valueOf(timeOnServer));
		/*Date currentdate  = new Date();
		long endTime = System.currentTimeMillis();
		long differenceTime = endTime - timeOnServer;
		long diff = (int) ((currentdate.getTime() - timeOnServer)/(1000*60*60*24*60*60*60));*/
		if (diff < 1 * MINUTE) {     
		      return String.format("seconds ago");
		   } else if(diff > 1 * MINUTE && diff < 2 * MINUTE){
		        return String.format(" a minute ago");
		    } else if(diff > 2 * MINUTE && diff < 59 * MINUTE){
		    	int minutes = (int)((int)diff/(int)MINUTE);
		        return String.format("%d minutes ago",minutes);
		    } else if(diff > 59 * MINUTE && diff < 2 * HOUR){
		        return String.format("an hour ago");
		    } else if(diff > 2 * HOUR && diff < 23 * HOUR){
		    	int hours = (int)((int)diff/(int)HOUR);
		        return String.format("%d hours ago",hours);
		    } else if(diff > 23 * HOUR && diff < 2 * DAY){
		        return String.format("a day ago");
		    } else if(diff > 2 * DAY && diff < 29 * DAY){
		    	int days = (int)((int)diff/(int)DAY);
		        return String.format("%d days ago",days);
		    } else if(diff > 29 * DAY && diff < 2 * MONTH){
		        return String.format("a month ago");
		    } else if(diff > 2 * MONTH && diff < 29 * MONTH){
		    	int days = (int)((int)diff/(int)MONTH);
		        return String.format("%d months ago",days);
		    } else if(diff > 29 * MONTH && diff < 2 * YEAR){
		        return String.format("a year ago");
		    } else if(diff > 2 * YEAR && diff < 29 * YEAR){
		    	int days = (int)((int)diff/(int)YEAR);
		        return String.format("%d years ago",days);
		    }	
		return String.valueOf(diff);
	}
	
	
	public static boolean isNetworkAvailable(Activity context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    Log.d("Network Info: ", ""+activeNetworkInfo);
	    if(activeNetworkInfo != null)
	    	return true;
	    else
	    	return false;
	}
	
}
