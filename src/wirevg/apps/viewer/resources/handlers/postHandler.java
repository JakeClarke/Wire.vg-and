package wirevg.apps.viewer.resources.handlers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import android.net.Uri;
import android.util.Log;
import wirevg.apps.viewer.resources.Post;

public class postHandler extends DefaultHandler{
	
	boolean InShortURL = false;
	boolean InLongURL = false;
	boolean InTitle = false;
	boolean InVote = false;
	boolean InFeedName = false;
	boolean InImage = false;
	boolean InView = false;
	Post currentPost;
	public ArrayList<Post> Posts;
	
	public static final String FILTERTYPE_USER = "user";
	public static final String FILTERTYPE_CHANNEL = "channel";
	public static final String FILTERTYPE_VOTES = "votes";
	public static final String FILTERTYPE_VIEWS = "views";
	
	public void getPostsViaTrend(String trend){
		
		URL apiURL;
		try {
			apiURL = new URL(ApiData.BaseUrl + "resource=posts&filtertype=search&filtervalue=" + trend);
			getPosts(apiURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void getPosts(URL apiURL) throws FactoryConfigurationError {
		try {
			
			SAXParserFactory fac = SAXParserFactory.newInstance();
			SAXParser sp = fac.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(this);
			xr.setErrorHandler(new errorHandler());
			String cleanURL = apiURL.toString();
			cleanURL = cleanURL.replace(" ", "%20");
			apiURL = new URL(cleanURL);
			Log.v("wire.vg.posthandler", "Getting posts from url:" + apiURL);
			
			xr.parse(new InputSource(apiURL.openStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getPostsFilter(String filterType, String filterValue)
	{
		try {
			URL apiURL = new URL(ApiData.BaseUrl + "format=xml&resource=posts&filtertype=" + filterType + "&filtervalue=" + filterValue);
			getPosts(apiURL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getPostsViaChannel(String hashCode) {
		this.getPostsFilter(FILTERTYPE_CHANNEL, hashCode);
	}
	
	@Override
	public void startElement(String uri, String name, String qname, Attributes attri)
	{
		if (name.trim().equalsIgnoreCase("shortlink")) {
			InShortURL = true;
		}
		else if (name.trim().equalsIgnoreCase("post")) {
			currentPost = new Post();
			this.Posts.add(currentPost);
		}
		else if (name.trim().equalsIgnoreCase("title")) {
			InTitle = true;
		}
		else if (name.trim().equalsIgnoreCase("shortlink")) {
			InShortURL = true;
		}
		else if (name.trim().equalsIgnoreCase("name")) {
			InFeedName = true;
		}
		else if (name.trim().equalsIgnoreCase("link")) {
			InLongURL= true;
		}
		else if (name.trim().equalsIgnoreCase("image")) {
			InImage= true;
		}
		else if (name.trim().equalsIgnoreCase("votes")) {
			InVote = true;
		}
		else if (name.trim().equalsIgnoreCase("views")) {
			InView = true;
		}
		
	}
	
	public void endElement(String uri, String name, String qname) {
		if (name.trim().equalsIgnoreCase("shortlink")) {
			InShortURL = false;
		}
		else if (name.trim().equalsIgnoreCase("views")) {
			InView = false;
		}
		else if (name.trim().equalsIgnoreCase("title")) {
			InTitle = false;
		}
		else if (name.trim().equalsIgnoreCase("shortlink")) {
			InShortURL = false;
		}
		else if (name.trim().equalsIgnoreCase("name"))
		{
			InFeedName = false;
		}
		else if (name.trim().equalsIgnoreCase("link")) {
			InLongURL = false;
		}
		else if (name.trim().equalsIgnoreCase("image")) {
			InImage = false;
		}
		else if (name.trim().equalsIgnoreCase("votes")) {
			InVote = false;
		}
	}
	
	public void characters(char[] ch, int start, int length){
				
		

		String data = (new String(ch).substring(start, start + length));
		
		if(this.InTitle) {
			
			
			this.currentPost.Title = this.currentPost.Title + data;
			this.currentPost.Title = this.currentPost.Title.replace("\t", "");
			this.currentPost.Title = this.currentPost.Title.replace("\n", "");
		}
		else if(this.InLongURL) {
			this.currentPost.LongURL = Uri.parse(data);
		}
		else if(this.InShortURL) {
			this.currentPost.ShortURL = Uri.parse(data);
		}
		else if (this.InImage) {
			this.currentPost.Image = Uri.parse(data);
		}
		else if(this.InVote) {
			this.currentPost.Votes = Integer.parseInt(data);
		}
		else if(this.InFeedName) {
			this.currentPost.FeedName = data;
		}
		else if(this.InView) {
			this.currentPost.Views = Integer.parseInt(data);
		}
	}

	public void startDocument() {
		this.Posts = new ArrayList<Post>();
	}
	
	public void endDocument() {}
	

	class errorHandler implements ErrorHandler
	{

		@Override
		public void error(SAXParseException e) throws SAXException {
			// TODO Auto-generated method stub
			//e.printStackTrace();
			Log.e("XmlReader", "Xml error: " + e.toString());
			throw e;
		}

		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			// TODO Auto-generated method stub
			//e.printStackTrace();
			Log.e("XmlReader", "Xml fatal error: " + e.toString());
			throw e;
		}

		@Override
		public void warning(SAXParseException e) throws SAXException {
			// TODO Auto-generated method stub
			//e.printStackTrace();
			Log.e("XmlReader", "Xml warning: " + e.toString());
			throw e;
		}
		
	}
}
