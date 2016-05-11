package com.marknguyen.babygenderpredictor;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

import blurdialogfragment.SampleDialogFragment;
import materialdesign.views.ButtonRectangle;
import shimmer.ShimmerFrameLayout;
import sola2lunar.Lunar;
import sola2lunar.LunarSolarConverter;
import sola2lunar.Solar;

public class PickDayActivity extends AppCompatActivity {

    private int mCurrentPreset = -1;
    private Toast mPresetToast;

    private EditText btnChooseBirthday;
    private EditText btnTimeBaby;
    private TextView tv_solar_birthday;
    private TextView tv_lunar_birthday;
    private TextView tv_solar_timebaby;
    private TextView tv_lunar_timebaby;
    private TextView tvGenderCheckResults;
    private ButtonRectangle btn_predictor;
    private Timer timer;
    private ShimmerFrameLayout mShimmerViewContainer;

    private ImageView iv_boy;
    private ImageView iv_girl;
    private ImageView question_mark;
    private Lunar lunarBirthday;
    private Lunar lunarPregnat;

    //Girl = 0 , Boy = 1
    // cols = month 1 -> 12
    // rows = age 18 -> 45
    public static int magic_table[][] = new int[][]{
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0},
            {0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1},
            {0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1},
            {1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
            {0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0},
            {0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pick_day);

        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

        tv_solar_birthday = (TextView) findViewById(R.id.tv_solar_birthday);
        tv_lunar_birthday = (TextView) findViewById(R.id.tv_lunar_birthday);
        tv_solar_timebaby = (TextView) findViewById(R.id.tv_solar_timebaby);
        tv_lunar_timebaby = (TextView) findViewById(R.id.tv_lunar_timebaby);
        tvGenderCheckResults = (TextView)findViewById(R.id.text_check_gender_results);

        iv_boy = (ImageView) findViewById(R.id.iv_boy);
        iv_girl = (ImageView) findViewById(R.id.iv_girl);
        question_mark = (ImageView) findViewById(R.id.question_mark);

        // Button to predict the gender of baby
        btn_predictor = (ButtonRectangle) findViewById(R.id.btn_gender_predictor);
        btn_predictor.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                try {
                    if (checkBoyorGirl(lunarBirthday.lunarYear, lunarPregnat.lunarYear, lunarPregnat.lunarMonth) == 0) {
                        iv_girl.setVisibility(View.VISIBLE);
                        iv_boy.setVisibility(View.GONE);
                        question_mark.setVisibility(View.GONE);
                        tvGenderCheckResults.setText("Congratulations! You might be got a little girl!");
                        mShimmerViewContainer.startShimmerAnimation();
                    } else {
                        iv_boy.setVisibility(View.VISIBLE);
                        iv_girl.setVisibility(View.GONE);
                        question_mark.setVisibility(View.GONE);
                        tvGenderCheckResults.setText("Congratulations! You might be got a little boy!");
                        mShimmerViewContainer.startShimmerAnimation();


                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Please enter input exactly!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        // Listener of choose mom birthday button
        final DatePickerDialog.OnDateSetListener chooseBirthday = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(0, myCalendar);
                updateResult(0, year, monthOfYear, dayOfMonth);
            }

        };
        // Listener of choose pregnant time
        final DatePickerDialog.OnDateSetListener choosePregnant = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(1, myCalendar);
                updateResult(1, year, monthOfYear, dayOfMonth);
            }

        };

        btnChooseBirthday = (EditText) findViewById(R.id.btn_choose_birthday);
        btnChooseBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PickDayActivity.this, chooseBirthday, 1991, myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnTimeBaby = (EditText) findViewById(R.id.btn_choose_timebaby);
        btnTimeBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PickDayActivity.this, choosePregnant, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        FloatingActionButton fab_info = (FloatingActionButton) findViewById(R.id.fab_info);
        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SampleDialogFragment fragment
                        = SampleDialogFragment.newInstance();
                fragment.show(getFragmentManager(), "blur_sample");
            }
        });
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

    /**
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    public void updateResult(int flag, int year, int monthOfYear, int dayOfMonth) {
        switch (flag) {
            case 0: {
                String date = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                tv_solar_birthday.setText(date);
                Solar solar = new Solar();
                solar.solarYear = year;
                solar.solarMonth = monthOfYear;
                solar.solarDay = dayOfMonth;
                lunarBirthday = LunarSolarConverter.SolarToLunar(solar);
                tv_lunar_birthday.setText("Lunar birthday: " + lunarBirthday.lunarDay + "/" + lunarBirthday.lunarMonth + "/" + lunarBirthday.lunarYear);
                break;
            }
            case 1: {
                String date = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                tv_solar_timebaby.setText(date);
                Solar solar = new Solar();
                solar.solarYear = year;
                solar.solarMonth = monthOfYear;
                solar.solarDay = dayOfMonth;
                lunarPregnat = LunarSolarConverter.SolarToLunar(solar);
                tv_lunar_timebaby.setText("Lunar Pregnat Time: " + lunarPregnat.lunarDay + "/" + lunarPregnat.lunarMonth + "/" + lunarPregnat.lunarYear);
                break;
            }
        }

    }

    /**
     * Update the label
     *
     * @param myCalendar
     */
    public void updateLabel(int flag, Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        switch (flag) {
            case 0:
                btnChooseBirthday.setText(sdf.format(myCalendar.getTime()));
                break;
            case 1:
                btnTimeBaby.setText(sdf.format(myCalendar.getTime()));
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

         selectPreset(0, false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Check Baby's Gender
     *
     * @param lunarBirthdayYear
     * @param lunarPregnatYear
     * @param lunarPregnatMonth
     * @return
     */
    public static int checkBoyorGirl(int lunarBirthdayYear, int lunarPregnatYear, int lunarPregnatMonth) {
        int ageMom = lunarPregnatYear - lunarBirthdayYear;
        return magic_table[ageMom - 18][lunarPregnatMonth - 1];
    }

}
