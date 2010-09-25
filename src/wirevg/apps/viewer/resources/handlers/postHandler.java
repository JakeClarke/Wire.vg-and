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
import wirevg.apps.viewer.resources.Comment;
import wirevg.apps.viewer.resources.Post;

public class postHandler extends DefaultHandler{
	
	boolean inShortURL = false;
	boolean inLongURL = false;
	boolean inTitle = false;
	boolean inVote = false;
	boolean inFeedName = false;
	boolean inImage = false;
	boolean inView = false;
	boolean inCommentCount = false;
	
	
	// comment flags
	boolean inComment = false;
	boolean inCommentID = false;
	boolean inCommentAuthor = false;
	boolean inCommentAuthorLink = false;
	boolean inCommentTimestamp = false;
	boolean inCommentText = false;
	boolean inCommentVotes = false;
	
	Comment currentComment;
	
	
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
	
	
	public void getPost(int postID)
	{
		try {
			URL apiURL = new URL(ApiData.BaseUrl + "format=xml&resource=post&filtertype=id&filtervalue=" + postID);
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
			inShortURL = true;
		}
		else if (name.trim().equalsIgnoreCase("post")) {
			currentPost = new Post();
			this.Posts.add(currentPost);
		}
		else if (name.trim().equalsIgnoreCase("title")) {
			inTitle = true;
		}
		else if (name.trim().equalsIgnoreCase("shortlink")) {
			inShortURL = true;
		}
		else if (name.trim().equalsIgnoreCase("name")) {
			inFeedName = true;
		}
		else if (name.trim().equalsIgnoreCase("link")) {
			inLongURL= true;
		}
		else if (name.trim().equalsIgnoreCase("image")) {
			inImage= true;
		}
		else if (name.trim().equalsIgnoreCase("votes")) {
			inVote = true;
		}
		else if (name.trim().equalsIgnoreCase("views")) {
			inView = true;
		}
		else if (name.trim().equalsIgnoreCase("commentcount")) {
			inCommentCount = true;
		}
		// begin comment clauses
		else if (name.trim().equalsIgnoreCase("comment")) {
			// prepare for a new comment
			this.currentComment = new Comment();
			this.currentPost.Comments.add(currentComment);
			// allow comment data to be captured
			this.inComment = true;
			
		} 
		else if ((name.trim().equalsIgnoreCase("id")) && inComment) 
		{
			this.inCommentID = true;
		}
		else if ((name.trim().equalsIgnoreCase("author")) && inComment)
		{
			this.inCommentAuthor = true;
		}
		else if ((name.trim().equalsIgnoreCase("authorlink")) && inComment)
		{
			this.inCommentAuthorLink = true;
		}
		else if ((name.trim().equalsIgnoreCase("timestamp")) && inComment)
		{
			this.inCommentTimestamp = true;
		}
		else if ((name.trim().equalsIgnoreCase("text")) && inComment)
		{
			this.inCommentText = true;
		}
		else if ((name.trim().equalsIgnoreCase("vote")) && inComment)
		{
			this.inCommentVotes = true;
		}
		
		
	}
	
	public void endElement(String uri, String name, String qname) {
		if (name.trim().equalsIgnoreCase("shortlink")) {
			inShortURL = false;
		}
		else if (name.trim().equalsIgnoreCase("views")) {
			inView = false;
		}
		else if (name.trim().equalsIgnoreCase("title")) {
			inTitle = false;
		}
		else if (name.trim().equalsIgnoreCase("shortlink")) {
			inShortURL = false;
		}
		else if (name.trim().equalsIgnoreCase("name"))
		{
			inFeedName = false;
		}
		else if (name.trim().equalsIgnoreCase("link")) {
			inLongURL = false;
		}
		else if (name.trim().equalsIgnoreCase("image")) {
			inImage = false;
		}
		else if (name.trim().equalsIgnoreCase("votes")) {
			inVote = false;
		}
		else if (name.trim().equalsIgnoreCase("commentcount")) {
			inCommentCount = false;
		}
		
		// begin comment clauses
		else if (name.trim().equalsIgnoreCase("comment")) {
			this.inComment = false;
			
		} 
		else if ((name.trim().equalsIgnoreCase("id")) && inComment) 
		{
			this.inCommentID = false;
		}
		else if ((name.trim().equalsIgnoreCase("author")) && inComment)
		{
			this.inCommentAuthor = false;
		}
		else if ((name.trim().equalsIgnoreCase("authorlink")) && inComment)
		{
			this.inCommentAuthorLink = false;
		}
		else if ((name.trim().equalsIgnoreCase("timestamp")) && inComment)
		{
			this.inCommentTimestamp = false;
		}
		else if ((name.trim().equalsIgnoreCase("text")) && inComment)
		{
			this.inCommentText = false;
		}
		else if ((name.trim().equalsIgnoreCase("vote")) && inComment)
		{
			this.inCommentVotes = false;
		}
	}
	
	public void characters(char[] ch, int start, int length){
				
		

		String data = (new String(ch).substring(start, start + length));
		
		if(this.inTitle) {
			
			
			this.currentPost.Title = this.currentPost.Title + data;
			this.currentPost.Title = this.currentPost.Title.replace("\t", "");
			this.currentPost.Title = this.currentPost.Title.replace("\n", "");
		}
		else if(this.inLongURL) {
			this.currentPost.LongURL = Uri.parse(data);
		}
		else if(this.inShortURL) {
			this.currentPost.ShortURL = Uri.parse(data);
		}
		else if (this.inImage) {
			this.currentPost.Image = Uri.parse(data);
		}
		else if(this.inVote) {
			this.currentPost.Votes = Integer.parseInt(data);
		}
		else if(this.inCommentCount) {
			this.currentPost.CommentCount = Integer.parseInt(data);
		}
		else if(this.inFeedName) {
			this.currentPost.FeedName = data;
		}
		else if(this.inView) {
			this.currentPost.Views = Integer.parseInt(data);
		}
		else if(this.inCommentID) {
			this.currentComment.ID = Integer.parseInt(data);
		}
		else if (this.inCommentAuthor) {
			this.currentComment.Author = data;
		}
		else if (this.inCommentAuthorLink) {
			this.currentComment.AuthorLink = data;
		}
		else if (this.inCommentText) {
			this.currentComment.Text = data;
		}
		else if (this.inCommentTimestamp)
		{
			this.currentComment.TimeStamp = Integer.parseInt(data);
		}
		else if (this.inCommentVotes)
		{
			this.currentComment.Votes = Integer.parseInt(data);
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
