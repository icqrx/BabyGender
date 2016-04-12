package com.marknguyen.babygenderpredictor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import date.DatePickerDialog;
import materialdesign.views.ButtonRectangle;
import sola2lunar.Lunar;

public class TimePredictorActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{
    private ButtonRectangle btn_timePredictor;
    private ImageButton btn_chooseBirthday;
    private TextView tv_solarBirthday;
    private TextView tv_lunarAge;
    private Lunar lunarBirthday;
    private Lunar lunarCurrent;
    private int ageMom;
    private Spinner spRangeOfAge;
    private TextView tvTimeResults;
    private Switch swBoyorGirl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_predictor);
        tv_solarBirthday = (TextView)findViewById(R.id.tv_solar_birthday2);
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
                    if(isCheck) temp = 1;
                    else temp = 0;
                    getMonthXXX(temp);
                }catch (Exception e ){
                    Toast.makeText(TimePredictorActivity.this, "Please check the input again!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        btn_chooseBirthday = (ImageButton)findViewById(R.id.btn_choose_birthday_screen2);
        btn_chooseBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        TimePredictorActivity.this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd.showYearPickerFirst(true);
//                dpd.show(getFragmentManager(), "Datepickerdialog");
               // showDialog(333);
            }
        });

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
                tvTimeResults.append(tmp_age + " Ages: " + "Predict time at months-> " + tmp_month.substring(1, tmp_month.length()));
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
//        tv_solarBirthday.setText(date);
//        Solar solar = new Solar();
//        solar.solarYear = year;
//        solar.solarMonth = monthOfYear;
//        solar.solarDay = dayOfMonth;
//        lunarBirthday = LunarSolarConverter.SolarToLunar(solar);
//
//        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String[] curr = currentDate.split("-");
//        Solar solar_curr = new Solar();
//        solar_curr.solarDay = Integer.parseInt(curr[2]);
//        solar_curr.solarMonth = Integer.parseInt(curr[1]);
//        solar_curr.solarYear = Integer.parseInt(curr[0]);
//        lunarCurrent = LunarSolarConverter.SolarToLunar(solar_curr);
//
//        ageMom = lunarCurrent.lunarYear - lunarBirthday.lunarYear;
//
//        tv_lunarAge.setText("Lunar age: " + ageMom);
    }


}
