package slidelauch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.marknguyen.babygenderpredictor.MainActivity;
import com.marknguyen.babygenderpredictor.R;

import java.util.List;
import java.util.Vector;

import adapter.InitPagerAdapter;

public class SlideLauch extends FragmentActivity {
	private PagerAdapter mPagerAdapter;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Intent i = new Intent(SlideLauch.this, MainActivity.class);
            SlideLauch.this.startActivity(i);
            finish();
        }
    };
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("first_time", false))
        {
            setContentView(R.layout.activity_launch);

    		List fragments = new Vector();

    		fragments.add(Fragment.instantiate(this, SpanOne.class.getName()));

    		fragments.add(Fragment.instantiate(this, SpanFive.class.getName()));

    		// Fragments
    		this.mPagerAdapter = new InitPagerAdapter(super.getSupportFragmentManager(), fragments);

    		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
    		pager.setAdapter(this.mPagerAdapter);
    		

        }
        
        else
        {
            handler.sendEmptyMessage(0);
        }
	}
    
}
