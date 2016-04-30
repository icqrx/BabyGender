package com.marknguyen.babygenderpredictor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;

import blurdialogfragment.SampleDialogFragment;
import date.DatePickerDialog;
import materialdesign.views.ButtonRectangle;
import materialdesign.views.DonutProgress;
import sola2lunar.Lunar;
import sola2lunar.LunarSolarConverter;
import sola2lunar.Solar;

public class PickDayActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    private ImageButton btnChooseBirthday;
    private TextView tv_solar_birthday;
    private TextView tv_lunar_birthday;
    private TextView tv_solar_timebaby;
    private TextView tv_lunar_timebaby;
//    private EditText edt_xxx_month;
    private ButtonRectangle btn_predictor;
    private Timer timer;
    private ImageButton btnTimeBaby;
    private ImageView iv_boyorgirl;
    private Lunar lunarBirthday;
    private Lunar lunarPregnat;
    //Girl = 0 , Boy = 1
// cols = month 1 -> 12
// rows = age 18 -> 45
    public static int magic_table[][] = new int[][] {
        {	0	,	1	,	0	,	1	,	1	,	1	,	1	,	1	,	1	,	1	,	1	,	1   }	,
        {	1	,	0	,	1	,	0	,	0	,	1	,	1	,	1	,	1	,	1	,	0	,	0	}	,
        {	0	,	1	,	0	,	1	,	1	,	1	,	1	,	1	,	1	,	0	,	1	,	1	}	,
        {	1	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	}	,
        {	0	,	1	,	1	,	0	,	1	,	0	,	0	,	1	,	0	,	0	,	0	,	0	}	,
        {	1	,	1	,	0	,	1	,	1	,	0	,	1	,	0	,	1	,	1	,	1	,	0	}	,
        {	1	,	0	,	1	,	1	,	0	,	1	,	1	,	0	,	0	,	0	,	0	,	0	}	,
        {	0	,	1	,	1	,	0	,	0	,	1	,	0	,	1	,	1	,	1	,	1	,	1	}	,
        {	1	,	0	,	1	,	0	,	0	,	1	,	0	,	1	,	0	,	0	,	0	,	0	}	,
        {	0	,	1	,	0	,	1	,	0	,	0	,	1	,	1	,	1	,	1	,	0	,	1	}	,
        {	1	,	0	,	1	,	0	,	0	,	0	,	1	,	1	,	1	,	1	,	0	,	0	}	,
        {	0	,	1	,	0	,	0	,	1	,	1	,	1	,	1	,	1	,	0	,	0	,	0	}	,
        {	1	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	1	,	1	}	,
        {	1	,	0	,	1	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	1	}	,
        {	1	,	0	,	1	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	1	}	,
        {	0	,	1	,	1	,	1	,	0	,	0	,	0	,	1	,	0	,	0	,	0	,	1	}	,
        {	1	,	0	,	1	,	0	,	0	,	0	,	0	,	0	,	0	,	0	,	1	,	1	}	,
        {	1	,	1	,	0	,	1	,	0	,	0	,	0	,	1	,	0	,	0	,	1	,	1	}	,
        {	0	,	1	,	1	,	0	,	1	,	0	,	0	,	0	,	1	,	1	,	1	,	1	}	,
        {	1	,	0	,	1	,	1	,	0	,	1	,	0	,	1	,	0	,	1	,	0	,	1	}	,
        {	0	,	1	,	0	,	1	,	1	,	0	,	1	,	0	,	1	,	0	,	1	,	0	}	,
        {	1	,	0	,	1	,	1	,	1	,	0	,	0	,	1	,	0	,	1	,	0	,	0	}	,
        {	0	,	1	,	0	,	1	,	0	,	1	,	1	,	0	,	1	,	0	,	1	,	0	}	,
        {	1	,	0	,	1	,	0	,	1	,	0	,	1	,	1	,	0	,	1	,	0	,	1	}	,
        {	0	,	1	,	0	,	1	,	0	,	1	,	0	,	1	,	1	,	0	,	1	,	0	}	,
        {	1	,	0	,	1	,	0	,	1	,	0	,	1	,	0	,	1	,	1	,	1	,	1	}	,
        {	1	,	1	,	0	,	1	,	1	,	1	,	0	,	1	,	0	,	1	,	0	,	0	}	,
        {	0	,	1	,	1	,	0	,	0	,	0	,	1	,	0	,	1	,	0	,	1	,	1	}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_day);

        tv_solar_birthday = (TextView)findViewById(R.id.tv_solar_birthday);
        tv_lunar_birthday = (TextView)findViewById(R.id.tv_lunar_birthday);
        tv_solar_timebaby = (TextView)findViewById(R.id.tv_solar_timebaby);
        tv_lunar_timebaby = (TextView)findViewById(R.id.tv_lunar_timebaby);

        iv_boyorgirl = (ImageView)findViewById(R.id.iv_boyorgirl);



        btn_predictor = (ButtonRectangle)findViewById(R.id.btn_gender_predictor);
        btn_predictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new AsyncTaskRunner().execute();
            }
        });

//        edt_xxx_month = (EditText)findViewById(R.id.edt_enter_xxxmonth);
//        edt_xxx_month.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});

        btnChooseBirthday = (ImageButton)findViewById(R.id.btn_choose_birthday);
        btnChooseBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PickDayActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.showYearPickerFirst(true);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        btnTimeBaby = (ImageButton)findViewById(R.id.btn_choose_timebaby);
        btnTimeBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd1 = DatePickerDialog.newInstance(
                        PickDayActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd1.showYearPickerFirst(true);
                dpd1.show(getFragmentManager(), "Datepickerdialog1");
            }
        });

        FloatingActionButton fab_info = (FloatingActionButton) findViewById(R.id.fab_info);
        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SampleDialogFragment fragment
                        = SampleDialogFragment.newInstance();
                fragment.show(getFragmentManager(), "blur_sample");
                // startActivity(new Intent(MainActivity.this, PickDayActivity.class));
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

        if(dpd != null) dpd.setOnDateSetListener(this);

        DatePickerDialog dpd1 = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog1");

        if(dpd1 != null) dpd1.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if(view.getFragmentManager().findFragmentByTag("Datepickerdialog") != null) {
            String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
            tv_solar_birthday.setText(date);
            Solar solar = new Solar();
            solar.solarYear = year;
            solar.solarMonth = monthOfYear;
            solar.solarDay = dayOfMonth;
            lunarBirthday = LunarSolarConverter.SolarToLunar(solar);
            tv_lunar_birthday.setText("Lunar birthday: " + lunarBirthday.lunarDay + "/" + lunarBirthday.lunarMonth + "/" + lunarBirthday.lunarYear);
        }
        if(view.getFragmentManager().findFragmentByTag("Datepickerdialog1") != null) {
            String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
            tv_solar_timebaby.setText(date);
            Solar solar = new Solar();
            solar.solarYear = year;
            solar.solarMonth = monthOfYear;
            solar.solarDay = dayOfMonth;
            lunarPregnat = LunarSolarConverter.SolarToLunar(solar);
            tv_lunar_timebaby.setText("Lunar Pregnat Time: " + lunarPregnat.lunarDay + "/" + lunarPregnat.lunarMonth + "/" + lunarPregnat.lunarYear);
        }


    }

    public static int checkBoyorGirl(int lunarBirthdayYear, int lunarPregnatYear, int lunarPregnatMonth){
        int ageMom = lunarPregnatYear - lunarBirthdayYear;

        return magic_table[ageMom-18][lunarPregnatMonth-1];
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private DonutProgress donutProgress;
        private String resp;

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            donutProgress.setVisibility(View.GONE);
            try {
                if(checkBoyorGirl(lunarBirthday.lunarYear, lunarPregnat.lunarYear, lunarPregnat.lunarMonth) == 0) {
                    iv_boyorgirl.setImageResource(R.drawable.girl1);
                    iv_boyorgirl.setVisibility(View.VISIBLE);
                }else {
                    iv_boyorgirl.setImageResource(R.drawable.boy1);
                    iv_boyorgirl.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){
                Toast.makeText(getBaseContext(),"Please enter input exactly!", Toast.LENGTH_SHORT).show();
                donutProgress.setVisibility(View.VISIBLE);
            }

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            donutProgress = (DonutProgress)findViewById(R.id.donut_progress);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {

            try {
                int i = donutProgress.getProgress() + 5;
                Thread.sleep(100);
                donutProgress.setProgress(i);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
