package wirevg.apps.viewer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import wirevg.apps.viewer.resources.Post;
import wirevg.apps.viewer.resources.handlers.postHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetailsActivity extends Activity {

	static public final String KEY_POSTID = "postID";
	
	int postID = 0;
	
	Post currentPost;
	
	postHandler ph = new postHandler();;
	
	Button viewPostButton;
	TextView postTitle;
	
	TextView commentsView;
	Handler handler = new Handler();
	Bitmap postBm;
	
	
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
	
	// method of getting image from web
	//http://groups.google.com/group/android-developers/browse_thread/thread/8eea6c95a78012ea/2e1e7d581405d422?#2e1e7d581405d422
	private Bitmap getImageBitmap(String url) { 
        Bitmap bm = null; 
        try { 
            URL aURL = new URL(url); 
            URLConnection conn = aURL.openConnection(); 
            conn.connect(); 
            InputStream is = conn.getInputStream(); 
            BufferedInputStream bis = new BufferedInputStream(is); 
            bm = BitmapFactory.decodeStream(bis); 
            bis.close(); 
            is.close(); 
       } catch (IOException e) { 
           Log.e("PostDetails", "Error getting bitmap", e); 
       } 
       return bm; 
    } 
	
	View.OnClickListener viewPostL = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Intent i = new Intent(Intent.ACTION_VIEW, PostDetailsActivity.this.currentPost.ShortURL);
			startActivity(i);
		}
	};
	
	
}
