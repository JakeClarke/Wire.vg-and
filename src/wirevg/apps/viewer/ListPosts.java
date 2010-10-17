package wirevg.apps.viewer;

import java.util.ArrayList;

import wirevg.apps.viewer.resources.Post;
import wirevg.apps.viewer.resources.handlers.postHandler;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListPosts extends ListActivity  implements OnItemClickListener{
	public static final String MODE = "MODE";
	public static final String MODE_SEARCH = "search";
	public static final String MODE_HASHCODE = "hash";
	public static final String KEY_PARAMETER = "parameter";
	static final String STATUS_RETRIEVING = "getting";
	static final String STATUS_COMPLETE = "complete";
	public static final String KEY_ORDER = "order";
	public static final String ORDER_TIME = "time";
	
	
	ProgressDialog progress;
	
	postHandler ph;
	ArrayList<Post> posts = new ArrayList<Post>();
	getXmlThead XmlThread;
	ArrayAdapter<Post> aa;
	Handler mHandler = new Handler();
	String newTitle = "Post view";
	String Status = STATUS_RETRIEVING;
	timeOutThread tout;
	
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			addListItems();
		}
	};
	
	final Runnable getPostsTimeout = new Runnable() {
		public void run() {
			timeOutDialog();
		}
	};
	
	final Runnable getPostsFail = new Runnable() {
		public void run() {
			getFailedDialog("Failed to get posts");
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		progress = ProgressDialog.show(ListPosts.this, "", "Loading please wait...",true, true, new DialogInterface.OnCancelListener(){
			@Override
			public void onCancel(DialogInterface arg0) {
				ListPosts.this.finish();
			}
		});
		aa = new ArrayAdapter<Post>(this, R.layout.postlist_item, this.posts);
		setListAdapter(aa);
		
		XmlThread = new getXmlThead();
		tout = new timeOutThread(mHandler);
		tout.start();
		XmlThread.start();
		
		
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
	}

	protected void getPosts() throws Exception {
		this.posts.clear();
		
		
		Bundle extras = getIntent().getExtras();
		String mode = extras.getString(MODE) != null ? extras.getString(MODE) : "";
		
		// this is not a proper implementation of order.
		// that would require a number of changes to the post handler
		String order = extras.getString(KEY_ORDER) != null ?  extras.getString(KEY_ORDER) : "";
		
		
		ph = new postHandler();
		if (mode.equals(MODE_SEARCH)) {
			
			this.posts = ph.getPostsViaSearch(extras.getString(KEY_PARAMETER));
			this.newTitle = "Search results: " + extras.getString(KEY_PARAMETER);
		} else if (mode.equals(MODE_HASHCODE)) {
			
			this.posts = ph.getPostsViaChannel(extras.getString(KEY_PARAMETER));
			this.newTitle = "Get posts by hash: " + extras.getString(KEY_PARAMETER);
			
		} else if (order.equals(ORDER_TIME)) {
			this.posts = ph.getRecentPosts();
			this.newTitle = "Recent posts";
			
		} else {
			// nothing will happen
			this.getFailedDialog("Nothing to get");
		}
		
		
		
		
		

	}
	
	protected void onStop()
	{
		super.onStop();
		XmlThread.stop();
		
	}

	protected void addListItems() {
		
		progress.dismiss();
		
		if (this.posts.size() == 0)
		{
			
			noPostsDialog();
		}
		
		this.setTitle(newTitle);
		
		for(Post p : this.posts){
			aa.add(p);
		}
	}

	private void noPostsDialog() {
		this.getFailedDialog("No posts match the search.");
	}
	
	private void timeOutDialog() {
		this.getFailedDialog("Get posts timeout.");
	}
	
	private void getFailedDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	               ListPosts.this.finish();
	           }});
		AlertDialog d = builder.create();
		d.show();
	}


	@Override
	public void onItemClick(AdapterView<?> adpview, View view, int position, long id) {
		Intent i = new Intent(this, PostDetailsActivity.class);
		i.putExtra(PostDetailsActivity.KEY_POSTID, this.posts.get(position).ID);
		startActivity(i);
	}
	
	private class getXmlThead extends Thread {
		
		public void run()
		{
			try {
				ListPosts.this.getPosts();
				ListPosts.this.Status = STATUS_COMPLETE;
				mHandler.post(mUpdateResults);
			} catch (Exception e) {
				mHandler.post(getPostsFail);
				ListPosts.this.Status = STATUS_COMPLETE;
				e.printStackTrace();
			}
			
			
			
		}
	}
	
	private class timeOutThread extends Thread {
		
		int currentMilliSeconds = 0;
		int maxSeconds = 60;
		int threadWaitMS = 100;
		boolean timeoutShown = false;
		Handler h;
		
		public timeOutThread(Handler h)
		{
			this.h = h;
		}
		
		public void run()
		{
			while (ListPosts.this.Status == ListPosts.STATUS_RETRIEVING && !timeoutShown)
			{
				if(currentMilliSeconds > maxSeconds * 1000)
				{
					h.post(getPostsTimeout);
					timeoutShown = true;
				}
				try {
					Thread.sleep(threadWaitMS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.currentMilliSeconds = this.currentMilliSeconds + threadWaitMS;
			}
		}
	}
}
