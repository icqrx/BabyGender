package com.marknguyen.babygenderpredictor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import materialdesign.views.ButtonRectangle;

public class MainActivity extends AppCompatActivity {

    private ButtonRectangle btn_PickDay;
    private ButtonRectangle btn_PickGender;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                shareToGMail(new String[]{"huuquoc09@gmail.com","ntiendung@gmail.com"},"Feedback Baby Gender Predictor","We welcome any suggestions and feedback you have that will help us improve the products and services we provide to you. Please note that your feedback via this email! Thanks you :v");
//            }
//        });

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     *
     * @param email
     * @param subject
     * @param content
     */
    public void shareToGMail(String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = getApplication().getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        getApplication().startActivity(emailIntent);
    }

    /**
     * share button
     * @param view
     */
    public void shareApp(View view){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"huuquoc09@gmail.com","ntiendung@gmail.com"});
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback GenderPredictor App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.marknguyen.babygenderpredictor" + "\n" + "We welcome any suggestions and feedback you have that will help us improve the products and services we provide to you. Please note that your feedback via huuquoc09@gmailcom.com | ntiendung@gmail.com! Thanks you!");

        startActivity(Intent.createChooser(shareIntent, "Share this app thoughts"));

    }
}
