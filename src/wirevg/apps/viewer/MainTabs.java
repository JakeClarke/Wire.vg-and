package wirevg.apps.viewer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TextView;

public class MainTabs extends TabActivity {

	TabHost tabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.maintabs);
        tabHost = this.getTabHost();
        tabHost.setBackgroundResource(R.drawable.icon);
        Intent i;
        TabHost.TabSpec spec;
        
        // begin front page definition
        i = new Intent(this, ListPosts.class);
		i.putExtra(ListPosts.MODE, ListPosts.MODE_HASHCODE);
		i.putExtra(ListPosts.KEY_PARAMETER, "frontpage");
		i.putExtra(ListPosts.NO_SET_TITLE, true);
		
		LayoutInflater inflater = this.getLayoutInflater();
		
		
		
		TextView tabIndicator = (TextView) inflater.inflate(R.layout.tabselectorlayout, null);
		tabIndicator.setText("Wire.vg");
        
        spec = tabHost.newTabSpec("frontpage");
        spec.setIndicator(tabIndicator);
        spec.setContent(i);
        this.tabHost.addTab(spec);
        
        // begin recent posts definition
        
        tabIndicator = (TextView) inflater.inflate(R.layout.tabselectorlayout, null);
		tabIndicator.setText("Recent");
        
        i = new Intent(this, ListPosts.class);
        i.putExtra(ListPosts.KEY_ORDER, ListPosts.ORDER_TIME);
        i.putExtra(ListPosts.NO_SET_TITLE, true);
        
        spec = tabHost.newTabSpec("recent").setIndicator(tabIndicator);
        spec.setContent(i);
        this.tabHost.addTab(spec);
        
        // begin current trends tab definition
        tabIndicator = (TextView) inflater.inflate(R.layout.tabselectorlayout, null);
		tabIndicator.setText("Current trends");
        
        i = new Intent(this, trendViewActivity.class);
        i.putExtra(ListPosts.NO_SET_TITLE, true);
        
        spec = tabHost.newTabSpec("trends").setIndicator(tabIndicator);
        spec.setContent(i);
        this.tabHost.addTab(spec);
        
        this.tabHost.setCurrentTab(1);

	
	}
	
}
