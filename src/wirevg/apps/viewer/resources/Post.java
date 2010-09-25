package wirevg.apps.viewer.resources;

import java.util.ArrayList;

import android.net.Uri;

public class Post {
	public int Votes = 0;
	public int Views = 0;
	public Uri ShortURL;
	public String Title = "";
	public Uri LongURL;
	public Uri Image;
	public String FeedName = "";
	public String FeedSlug = "";
	public int CommentCount = 0;

	@Override
	public String toString()
	{
		return this.Title + "\n- [" + this.FeedName + "] \n- Votes: " + this.Votes + "| Comments: " + this.CommentCount;
	}
	
	public ArrayList<Post> Related = new ArrayList<Post>();
	
	public ArrayList<Comment> Comments = new ArrayList<Comment>();
	
	
}