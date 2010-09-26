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
        
        i = new Intent(this, ListPosts.class);
		i.putExtra(ListPosts.MODE, ListPosts.MODE_HASHCODE);
		i.putExtra(ListPosts.KEY_PARAMETER, "frontpage");
        
        TabHost.TabSpec spec;
        spec = tabHost.newTabSpec("frontpage").setIndicator("Frontpage");
        spec.setContent(i);
        this.tabHost.addTab(spec);
        
        i = new Intent(this, trendViewActivity.class);
        
        spec = tabHost.newTabSpec("trend").setIndicator("Current trends");
        spec.setContent(i);
        this.tabHost.addTab(spec);
        
	
	}
	
}
