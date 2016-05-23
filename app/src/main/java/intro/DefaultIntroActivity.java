package intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.marknguyen.babygenderpredictor.MainActivity;
import com.marknguyen.babygenderpredictor.R;

public final class DefaultIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("first_time", false)) {
            addSlide(SampleSlide.newInstance(R.layout.intro1));
            addSlide(SampleSlide.newInstance(R.layout.intro2));
            addSlide(new EndIntroSlide());
//        setColorDoneText(Color.parseColor("#ff66cc"));
//        setNextArrowColor(Color.parseColor("#ff66cc"));
//        setIndicatorColor(Color.parseColor("#ff66cc"), Color.GRAY);
//        setColorSkipButton(Color.parseColor("#ff66cc"));
            setDoneText("TRY IT NOW");
        } else {
            loadMainActivity();
        }
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("first_time", true);
        editor.commit();

        loadMainActivity();

        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
        Toast.makeText(getApplicationContext(), "Skip", Toast.LENGTH_SHORT).show();
        finish();
    }
}
