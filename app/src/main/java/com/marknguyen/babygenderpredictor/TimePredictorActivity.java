package com.marknguyen.babygenderpredictor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import datepicker.DatePicker;
import materialdesign.views.ButtonRectangle;

public class TimePredictorActivity extends FragmentActivity implements DatePicker.OnDateSetListener {
    private ButtonRectangle btn_timePredictor;
    private ImageButton btn_chooseBirthday;
    private TextView tv_solarBirthday;
    public static TextView tv_lunarAge;

    public static int ageMom;
    private Spinner spRangeOfAge;
    private TextView tvTimeResults;
    private Switch swBoyorGirl;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePicker pickerSolarBirthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_predictor);
//        tv_solarBirthday = (TextView)findViewById(R.id.tv_solar_birthday2);
        pickerSolarBirthday = (DatePicker)findViewById(R.id.long_date);
        tv_lunarAge = (TextView)findViewById(R.id.tv_lunar_birthday2);
        spRangeOfAge = (Spinner)findViewById(R.id.spinner_rangeOfAges);
        tvTimeResults = (TextView)findViewById(R.id.tv_time_results);
        swBoyorGirl = (Switch)findViewById(R.id.sw_boyorgirl);

        btn_timePredictor = (ButtonRectangle)findViewById(R.id.btn_time_predictor);
        btn_timePredictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvTimeResults.setText("");
                    int temp;
                    Boolean isCheck = swBoyorGirl.isChecked();
                    if (isCheck) temp = 1;
                    else temp = 0;
                    getMonthXXX(temp);
                } catch (Exception e) {
                    Toast.makeText(TimePredictorActivity.this, "Please check the input again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        pickerSolarBirthday.setDateFormat(DateFormat.getDateFormat(TimePredictorActivity.this));

//  This version for the new DatePicker Saved! Mark
//        btn_chooseBirthday = (ImageButton)findViewById(R.id.btn_choose_birthday_screen2);
//        btn_chooseBirthday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Calendar now = Calendar.getInstance();
////                DatePickerDialog dpd = DatePickerDialog.newInstance(
////                        TimePredictorActivity.this,
////                        now.get(Calendar.YEAR),
////                        now.get(Calendar.MONTH),
////                        now.get(Calendar.DAY_OF_MONTH)
////                );
////                dpd.showYearPickerFirst(true);
////                dpd.show(getFragmentManager(), "Datepickerdialog");
//
//            }
//        });

    }

    public void getMonthXXX(int temp){
        String rangeAges = spRangeOfAge.getSelectedItem().toString();
        int tmp_age = ageMom;
        int tmp = 0;
        String tmp_month = "";

        for(int i = 1; i <= 12; i++){
            if (BoyorGirl(tmp_age,i) == temp){
                tmp_month += ", " + i;
            }
            if (i == 12) {
                tvTimeResults.append("At the ages of " + tmp_age  + " the baby prediction time in months is: -> " + tmp_month.substring(1, tmp_month.length()));
                tvTimeResults.append("\n");
                tmp_age ++; i = 0; tmp ++; tmp_month = "";

            }
            if (tmp == Integer.parseInt(rangeAges)) {
                break;
            }
        }
    }
    public int BoyorGirl(int ageMom, int lunarPregnatMonth){

        return PickDayActivity.magic_table[ageMom-18][lunarPregnatMonth-1];
    }

    @Override
    public void onResume() {
        super.onResume();
//        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
//
//        if(dpd != null) dpd.setOnDateSetListener(this);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        }

}
