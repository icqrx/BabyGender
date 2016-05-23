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

    private ImageView iv_boy;
    private ImageView iv_girl;
    private ImageView question_mark;
    private ImageView ivBoyOrGirl;
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


        tv_solar_birthday = (TextView) findViewById(R.id.tv_solar_birthday);
        tv_lunar_birthday = (TextView) findViewById(R.id.tv_lunar_birthday);
        tv_solar_timebaby = (TextView) findViewById(R.id.tv_solar_timebaby);
        tv_lunar_timebaby = (TextView) findViewById(R.id.tv_lunar_timebaby);
        tvGenderCheckResults = (TextView) findViewById(R.id.text_check_gender_results);

        iv_boy = (ImageView) findViewById(R.id.iv_boy);
        iv_girl = (ImageView) findViewById(R.id.iv_girl);
        question_mark = (ImageView) findViewById(R.id.question_mark);
        ivBoyOrGirl = (ImageView)findViewById(R.id.iv_boyOrgirl);

        // Button to predict the gender of baby
        btn_predictor = (ButtonRectangle) findViewById(R.id.btn_gender_predictor);
        btn_predictor.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                try {
                    if (checkBoyorGirl(lunarBirthday.lunarYear, lunarPregnat.lunarYear, lunarPregnat.lunarMonth) == 0) {
                        iv_girl.setVisibility(View.GONE);
                        iv_boy.setVisibility(View.GONE);
                        question_mark.setVisibility(View.GONE);
                        tvGenderCheckResults.setVisibility(View.GONE);
                        ivBoyOrGirl.setImageResource(R.drawable.its_girl);
                        ivBoyOrGirl.setVisibility(View.VISIBLE);
//                        mShimmerViewContainer.startShimmerAnimation();
                    } else {
                        iv_boy.setVisibility(View.GONE);
                        iv_girl.setVisibility(View.GONE);
                        question_mark.setVisibility(View.GONE);
                        tvGenderCheckResults.setVisibility(View.GONE);
                        ivBoyOrGirl.setImageResource(R.drawable.its_boy);
                        ivBoyOrGirl.setVisibility(View.VISIBLE);
//                        mShimmerViewContainer.startShimmerAnimation();

                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Please enter input exactly!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
//        myCalendar.set(Calendar.YEAR, 1991);
//        myCalendar.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH));
//        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.get(Calendar.DAY_OF_MONTH));

//        final Calendar myCalendar_pregnant = Calendar.getInstance();
//        myCalendar_pregnant.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR));
//        myCalendar_pregnant.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH));
//        myCalendar_pregnant.set(Calendar.DAY_OF_MONTH, myCalendar.get(Calendar.DAY_OF_MONTH));

//        updateLabel(0, myCalendar);
//        updateLabel(1, myCalendar_pregnant);
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
                DatePickerDialog date_picker = new DatePickerDialog(PickDayActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth, chooseBirthday, 1991, myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                date_picker.getDatePicker().setCalendarViewShown(false);
                date_picker.show();
            }
        });


        btnTimeBaby = (EditText) findViewById(R.id.btn_choose_timebaby);
        btnTimeBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date_picker = new DatePickerDialog(PickDayActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth, choosePregnant, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                date_picker.getDatePicker().setCalendarViewShown(false);
                date_picker.show();
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
