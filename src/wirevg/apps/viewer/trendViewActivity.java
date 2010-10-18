package wirevg.apps.viewer;

import java.util.ArrayList;

import wirevg.apps.viewer.resources.handlers.trendHandler;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class trendViewActivity extends ListActivity implements OnItemClickListener {

	trendHandler th;
	ArrayList<String> phrases;
	ProgressDialog progress;
	Handler mHandler = new Handler();
	
	Runnable completedGet = new Runnable()
	{
		@Override
		public void run() {
			setListAdapter(new ArrayAdapter<String>(trendViewActivity.this, R.layout.postlist_item, th.phrases));
			progress.dismiss();
		}
	};
	
	@Override
	public void onCreate (Bundle savedInstanceData){
		super.onCreate(savedInstanceData);
		this.setTitle("Current trends: ");

		refresh();
		getListView().setOnItemClickListener(this);
	}

	private void refresh() {
		
		progress = ProgressDialog.show(this, "", "Loading please wait...",true, true, new DialogInterface.OnCancelListener(){
			@Override
			public void onCancel(DialogInterface arg0) {
				trendViewActivity.this.finish();
			}
		});
		
		th = new trendHandler();
		
		GetThread gt = new GetThread(mHandler);
		gt.start();
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menurefresh:
			refresh();
			return true;
		default:
	        return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.refreshmenu, menu);
		return true;
	}
	
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(this, ListPosts.class);
		i.putExtra(ListPosts.MODE, ListPosts.MODE_SEARCH);
		i.putExtra(ListPosts.KEY_PARAMETER, th.phrases.get(arg2));
		startActivity(i);
		
	}
	
	class GetThread extends Thread{
		Handler mHandler;
		@Override
		public void run()
		{
			try {
				trendViewActivity.this.phrases = th.getTrends();
				mHandler.post(completedGet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public GetThread(Handler mHandler)
		{
			this.mHandler = mHandler;
		}
	}
}
