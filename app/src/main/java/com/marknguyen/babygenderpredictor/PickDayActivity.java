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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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
    private TextView tv_lunar_birthday;
    private TextView tv_lunar_timebaby;
    private TextView tvGenderCheckResults;
    private ButtonRectangle btn_predictor;
    private Timer timer;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
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


        tv_lunar_birthday = (TextView) findViewById(R.id.tv_lunar_birthday);
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
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.iv_boyOrgirl));

                    } else {
                        iv_boy.setVisibility(View.GONE);
                        iv_girl.setVisibility(View.GONE);
                        question_mark.setVisibility(View.GONE);
                        tvGenderCheckResults.setVisibility(View.GONE);
                        ivBoyOrGirl.setImageResource(R.drawable.its_boy);
                        ivBoyOrGirl.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.iv_boyOrgirl));

                    }
                } catch (Exception e) {
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.btn_choose_birthday));
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.btn_choose_timebaby));

                    Toast.makeText(getBaseContext(), "Please enter input exactly! Maybe you are not over the age of 18?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Calendar myCalendar1 = Calendar.getInstance();

        btnChooseBirthday = (EditText) findViewById(R.id.btn_choose_birthday);
        btnChooseBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnTimeBaby = (EditText) findViewById(R.id.btn_choose_timebaby);
        btnTimeBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog1();
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

    private void showDialog() {
        if (mYear == 0 || mMonth == 0 || mDay == 0) {
            final Calendar c = Calendar.getInstance();
            mYear = 1991;
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog.OnDateSetListener chooseBirthday = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker myCalendar, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                mYear = selectedYear;
                mMonth = selectedMonth;
                mDay = selectedDay;
                updateLabel(0, mYear, mMonth, mDay );
                updateResult(0, selectedYear, selectedMonth, selectedDay);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PickDayActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth, chooseBirthday, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.show();
    }

    private void showDialog1() {

        if (mYear1 == 0 || mMonth1 == 0 || mDay1 == 0) {
            final Calendar c1 = Calendar.getInstance();
            mYear1 = c1.get(Calendar.YEAR);
            mMonth1 = c1.get(Calendar.MONTH);
            mDay1 = c1.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog.OnDateSetListener choosePregnant = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker myCalendar, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                mYear1 = selectedYear;
                mMonth1 = selectedMonth;
                mDay1= selectedDay;
                updateLabel(1, mYear1, mMonth1, mDay1 );
                updateResult(1, selectedYear, selectedMonth, selectedDay);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PickDayActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth, choosePregnant, mYear1, mMonth1, mDay1);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.show();
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
//                tv_solar_birthday.setText(date);
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
//                tv_solar_timebaby.setText(date);
                Solar solar = new Solar();
                solar.solarYear = year;
                solar.solarMonth = monthOfYear;
                solar.solarDay = dayOfMonth;
                lunarPregnat = LunarSolarConverter.SolarToLunar(solar);
                tv_lunar_timebaby.setText("Lunar Conception Date: " + lunarPregnat.lunarDay + "/" + lunarPregnat.lunarMonth + "/" + lunarPregnat.lunarYear);
                break;
            }
        }

    }

    /**
     *
     * @param flag
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    public void updateLabel(int flag, int year, int monthOfYear, int dayOfMonth) {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        myCalendar.set(year, monthOfYear, dayOfMonth);
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
