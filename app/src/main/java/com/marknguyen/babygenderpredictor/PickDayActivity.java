package com.marknguyen.babygenderpredictor;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

import blurdialogfragment.SampleDialogFragment;
import date.DatePickerDialog;
import datepicker.DatePicker;
import materialdesign.views.ButtonRectangle;
import sola2lunar.Lunar;
import sola2lunar.LunarSolarConverter;
import sola2lunar.Solar;

public class PickDayActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    private DatePicker btnChooseBirthday;
    private DatePicker btnTimeBaby;
    private TextView tv_solar_birthday;
    private TextView tv_lunar_birthday;
    private TextView tv_solar_timebaby;
    private TextView tv_lunar_timebaby;
    private ButtonRectangle btn_predictor;
    private Timer timer;

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

        tv_solar_birthday = (TextView) findViewById(R.id.tv_solar_birthday);
        tv_lunar_birthday = (TextView) findViewById(R.id.tv_lunar_birthday);
        tv_solar_timebaby = (TextView) findViewById(R.id.tv_solar_timebaby);
        tv_lunar_timebaby = (TextView) findViewById(R.id.tv_lunar_timebaby);

        iv_boy = (ImageView) findViewById(R.id.iv_boy);
        iv_girl = (ImageView) findViewById(R.id.iv_girl);
        question_mark = (ImageView) findViewById(R.id.question_mark);

        btn_predictor = (ButtonRectangle) findViewById(R.id.btn_gender_predictor);
        btn_predictor.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                try {
                    if (checkBoyorGirl(lunarBirthday.lunarYear, lunarPregnat.lunarYear, lunarPregnat.lunarMonth) == 0) {
                        //iv_boy.setImageResource(R.drawable.girl1);
                        iv_boy.setImageAlpha(30);
                        iv_girl.setImageAlpha(255);
                        question_mark.setImageAlpha(30);
                    } else {
                        //iv_boy.setImageResource(R.drawable.boy1);
                        iv_girl.setImageAlpha(30);
                        iv_boy.setImageAlpha(255);
                        question_mark.setImageAlpha(30);
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Please enter input exactly!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final int initYear = 1991;
        btnChooseBirthday = (DatePicker) findViewById(R.id.btn_choose_birthday);
        btnChooseBirthday.setDateFormat(DateFormat.getDateFormat(PickDayActivity.this));
//        btnChooseBirthday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        PickDayActivity.this,
//                        initYear,
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                        //now.get(Calendar.YEAR)
//                );
//                dpd.showYearPickerFirst(true);
//                dpd.show(getFragmentManager(), "Datepickerdialog");
//            }
//        });
        btnTimeBaby = (DatePicker) findViewById(R.id.btn_choose_timebaby);
        btnTimeBaby.setDateFormat(DateFormat.getDateFormat(PickDayActivity.this));
//        btnTimeBaby.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd1 = DatePickerDialog.newInstance(
//                        PickDayActivity.this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd1.showYearPickerFirst(true);
//                dpd1.show(getFragmentManager(), "Datepickerdialog1");
//            }
//        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (dpd != null) dpd.setOnDateSetListener(this);

        DatePickerDialog dpd1 = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog1");

        if (dpd1 != null) dpd1.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (view.getFragmentManager().findFragmentByTag("Datepickerdialog") != null) {
            String date = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            tv_solar_birthday.setText(date);
            Solar solar = new Solar();
            solar.solarYear = year;
            solar.solarMonth = monthOfYear;
            solar.solarDay = dayOfMonth;
            lunarBirthday = LunarSolarConverter.SolarToLunar(solar);
            tv_lunar_birthday.setText("Lunar birthday: " + lunarBirthday.lunarDay + "/" + lunarBirthday.lunarMonth + "/" + lunarBirthday.lunarYear);
        }
        if (view.getFragmentManager().findFragmentByTag("Datepickerdialog1") != null) {
            String date = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            tv_solar_timebaby.setText(date);
            Solar solar = new Solar();
            solar.solarYear = year;
            solar.solarMonth = monthOfYear;
            solar.solarDay = dayOfMonth;
            lunarPregnat = LunarSolarConverter.SolarToLunar(solar);
            tv_lunar_timebaby.setText("Lunar Pregnat Time: " + lunarPregnat.lunarDay + "/" + lunarPregnat.lunarMonth + "/" + lunarPregnat.lunarYear);
        }
    }

    public static int checkBoyorGirl(int lunarBirthdayYear, int lunarPregnatYear, int lunarPregnatMonth) {
        int ageMom = lunarPregnatYear - lunarBirthdayYear;

        return magic_table[ageMom - 18][lunarPregnatMonth - 1];
    }


}
