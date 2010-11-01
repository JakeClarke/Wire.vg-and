package wirevg.apps.viewer;

import java.util.ArrayList;

import wirevg.apps.viewer.resources.Post;
import wirevg.apps.viewer.resources.handlers.postHandler;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
		
		aa = new ArrayAdapter<Post>(this, R.layout.postlist_item, this.posts);
		setListAdapter(aa);
		
		refresh();
		
		
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
	}

	private void refresh() {
		progress = ProgressDialog.show(ListPosts.this, "", "Loading please wait...",true, true, new DialogInterface.OnCancelListener(){
			@Override
			public void onCancel(DialogInterface arg0) {
				ListPosts.this.finish();
			}
		});
		XmlThread = new getXmlThead();
		tout = new timeOutThread(mHandler);
		tout.start();
		XmlThread.start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.mainmenu, menu);
		return true;
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
		aa.clear();
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
	
	void showSearchDialog(String title, String message, final String mode)
	{
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.searchdialog);
		dialog.setTitle(title);
		TextView dialogHint = (TextView)dialog.findViewById(R.id.dialoghint);
		dialogHint.setText(message);
		final EditText searchInput = (EditText)dialog.findViewById(R.id.useredittext);
		Button searchGoB = (Button)dialog.findViewById(R.id.searchGo);
		searchGoB.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ListPosts.this, ListPosts.class);
				i.putExtra(ListPosts.MODE, mode);
				i.putExtra(ListPosts.KEY_PARAMETER, searchInput.getText().toString());
				ListPosts.this.startActivity(i);
			}
		});
		dialog.show();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menurefresh:
			refresh();
			return true;
		case R.id.menusearch:
			showSearchDialog("Keyword Search", "Enter search terms: ", ListPosts.MODE_SEARCH);
			return true;
		case R.id.menuhashtag:
			showSearchDialog("Hashtag Search", "Enter hashtag: ", ListPosts.MODE_HASHCODE);
			return true;
		default:
	        return super.onOptionsItemSelected(item);
		}
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
