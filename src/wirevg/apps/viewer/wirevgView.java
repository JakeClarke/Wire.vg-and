package wirevg.apps.viewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class wirevgView extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	Button trendB;
	Button searchB;
	Button visitB;
	Button votedB;
	Button viewB;
	EditText termsBox;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        
        
        trendB = (Button)findViewById(R.id.trendbutton);
        trendB.setOnClickListener(this);
        searchB = (Button)findViewById(R.id.SearchButton);
        searchB.setOnClickListener(this);
        termsBox = (EditText)findViewById(R.id.searchbox);
        visitB = (Button)findViewById(R.id.VisitSiteButton);
        visitB.setOnClickListener(this);
        votedB = (Button)findViewById(R.id.TopVotedButton);
        votedB.setOnClickListener(this);
        
        
        
        
    }
    
    public void onClick(View view) {
    	if(view.equals(trendB)) {
    		Intent i = new Intent(this, trendViewActivity.class);
    		startActivity(i);
    	} else if (view.equals(searchB)){
    		Intent i = new Intent(this, ListPosts.class);
    		i.putExtra(ListPosts.MODE, ListPosts.MODE_SEARCH);
    		i.putExtra(ListPosts.KEY_PARAMETER, termsBox.getText().toString());
    		startActivity(i);
    	} else if (view.equals(visitB)){
    		// load wire.vg in the default browser
    		Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("http://wire.vg/"));
    		startActivity(i);
    	} else if (view.equals(votedB)){
    		Intent i = new Intent(this, ListPosts.class);
    		i.putExtra(ListPosts.MODE, ListPosts.MODE_HASHCODE);
    		i.putExtra(ListPosts.KEY_PARAMETER, "frontpage");
    		startActivity(i);
    	}
    }
}