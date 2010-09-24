package wirevg.apps.viewer;

import android.net.Uri;

public class Post {
	public int Votes = 0;
	public int Views = 0;
	public Uri ShortURL;
	public String Title = "";
	public Uri LongURL;
	public Uri Image;
	public String FeedName = "";

	@Override
	public String toString()
	{
		return this.Title + "\n- [" + this.FeedName + "] \n- Votes: " + this.Votes +  " | Views: " + this.Views ;
	}
}