package wirevg.apps.viewer;

import wirevg.apps.viewer.resources.Post;
import wirevg.apps.viewer.resources.handlers.postHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostDetailsActivity extends Activity {

	static public final String KEY_POSTID = "postID";
	
	int postID = 0;
	
	Post currentPost;
	
	postHandler ph = new postHandler();;
	
	Button viewPostButton;
	TextView postTitle;
	
	TextView commentsView;
	
	
	
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.postdetails);
        
        this.viewPostButton = (Button)this.findViewById(R.id.viewpostbutton);
        this.postTitle = (TextView)this.findViewById(R.id.posttitle);
        
        
        this.commentsView = (TextView)this.findViewById(R.id.commenttext);
        
        this.postID = this.getIntent().getExtras().getInt(KEY_POSTID);
        this.setTitle("Showing post id:" +  this.postID);
        
        if (this.postID == 0)
        {
        	showErrorAndFinish("Error!","No post has been selected.");
        	return;
        }
        
        ph.getPost(postID);
        this.currentPost = ph.Posts.get(0);
        this.postTitle.setText(this.currentPost.Title);
        this.commentsView.setText("Number of comments: " + this.currentPost.Comments.size());
        this.viewPostButton.setOnClickListener(viewPostL);
        
	}

	private void showErrorAndFinish(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("Ok", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PostDetailsActivity.this.finish();
					}
				});
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.create().show();
		
	}
	
	View.OnClickListener viewPostL = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Intent i = new Intent(Intent.ACTION_VIEW, PostDetailsActivity.this.currentPost.ShortURL);
			startActivity(i);
		}
	};
	
}