package wirevg.apps.viewer;

import wirevg.apps.viewer.handlers.trendHandler;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class trendViewActivity extends ListActivity implements OnItemClickListener {

	trendHandler th;
	@Override
	public void onCreate (Bundle savedInstanceData){
		super.onCreate(savedInstanceData);

		th = new trendHandler();
		th.getTrends("public");
		String output = "Current trends: ";
		this.setTitle(output);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.postlist_item, th.phrases));
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(this, ListPosts.class);
		i.putExtra(ListPosts.MODE, ListPosts.MODE_SEARCH);
		i.putExtra(ListPosts.KEY_PARAMETER, th.phrases.get(arg2));
		startActivity(i);
		
	}
}
