package com.marknguyen.babygenderpredictor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import materialdesign.views.ButtonRectangle;
import shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity {

    private ButtonRectangle btn_PickDay;
    private ButtonRectangle btn_PickGender;
    private int mCurrentPreset = -1;
    private Toast mPresetToast;

    private ShimmerFrameLayout mShimmerViewContainer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Send feedback to our via Email xxx.com", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"xxx@gmail.com"});
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "We welcome any suggestions and feedback you have that will help us improve the products and services we provide to you. Please note that your feedback via this email! Thanks you :v");
                startActivity(Intent.createChooser(i, "Send email"));
            }
        });

        btn_PickDay = (ButtonRectangle) findViewById(R.id.btn_pick_day);
        btn_PickDay.setClickable(true);
        btn_PickDay.getBackground().setAlpha(200);
        btn_PickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PickDayActivity.class));
            }
        });
        btn_PickGender = (ButtonRectangle) findViewById(R.id.btn_pick_gender);
        btn_PickGender.getBackground().setAlpha(200);
        btn_PickGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TimePredictorActivity.class));
            }
        });

        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

       // selectPreset(0, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Select one of the shimmer animation presets.
     *
     * @param preset    index of the shimmer animation preset
     * @param showToast whether to show a toast describing the preset, or not
     */
    private void selectPreset(int preset, boolean showToast) {
        if (mCurrentPreset == preset) {
            return;
        }
        mCurrentPreset = preset;

        // Save the state of the animation
        boolean isPlaying = mShimmerViewContainer.isAnimationStarted();

        // Reset all parameters of the shimmer animation
        mShimmerViewContainer.useDefaults();

        // If a toast is already showing, hide it
        if (mPresetToast != null) {
            mPresetToast.cancel();
        }
    }
}
