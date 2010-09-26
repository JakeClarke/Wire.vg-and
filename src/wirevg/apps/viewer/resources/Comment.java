package wirevg.apps.viewer.resources;

public class Comment {
	
	public int ID = 0;
	public String Author = "";
	public String AuthorLink = "";
	public int TimeStamp = 0;
	public String Text = "";
	public int Votes = 0;
	
	@Override
	public String toString()
	{
		return this.Author + ":\n " + this.Text + "\n" + new java.util.Date(TimeStamp * 1000).toGMTString();
	}

}
