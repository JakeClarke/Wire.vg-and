package wirevg.apps.viewer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabs extends TabActivity {

	TabHost tabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.maintabs);
        tabHost = this.getTabHost();
        Intent i;
        TabHost.TabSpec spec;
        
        // begin front page definition
        i = new Intent(this, ListPosts.class);
		i.putExtra(ListPosts.MODE, ListPosts.MODE_HASHCODE);
		i.putExtra(ListPosts.KEY_PARAMETER, "frontpage");
        
        spec = tabHost.newTabSpec("frontpage").setIndicator("Frontpage");
        spec.setContent(i);
        this.tabHost.addTab(spec);
        
        // begin recent posts definition
        i = new Intent(this, ListPosts.class);
        i.putExtra(ListPosts.KEY_ORDER, ListPosts.ORDER_TIME);
        
        spec = tabHost.newTabSpec("recent").setIndicator("Recent");
        spec.setContent(i);
        this.tabHost.addTab(spec);
        
        // begin current trends tab definition
        i = new Intent(this, trendViewActivity.class);
        
        spec = tabHost.newTabSpec("trends").setIndicator("Current trends");
        spec.setContent(i);
        this.tabHost.addTab(spec);
        

	
	}
	
}
