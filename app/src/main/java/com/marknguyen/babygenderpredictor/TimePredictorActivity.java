package com.marknguyen.babygenderpredictor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import datepicker.DatePicker;
import materialdesign.views.ButtonRectangle;
import sola2lunar.Lunar;
import sola2lunar.LunarSolarConverter;
import sola2lunar.Solar;

public class TimePredictorActivity extends FragmentActivity implements DatePicker.OnDateSetListener {
    private ButtonRectangle btn_timePredictor;
    private ImageButton btn_chooseBirthday;
    private TextView tv_solarBirthday;
    public static TextView tv_lunarAge;
    private AlertDialog theDialog;
    private CalendarPickerView dialogView;
    public static int ageMom;
    private Spinner spRangeOfAge;
    private TextView tvTimeResults;
    private Switch swBoyorGirl;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePicker pickerSolarBirthday;
    private static int thisYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_predictor);
        pickerSolarBirthday = (DatePicker) findViewById(R.id.long_date);
        tv_lunarAge = (TextView) findViewById(R.id.tv_lunar_birthday2);
        spRangeOfAge = (Spinner) findViewById(R.id.spinner_rangeOfAges);
        tvTimeResults = (TextView) findViewById(R.id.tv_time_results);
        swBoyorGirl = (Switch) findViewById(R.id.sw_boyorgirl);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        final Date today = new Date();


        Calendar calendar = Calendar.getInstance();
        thisYear = calendar.get(Calendar.YEAR);

//        //Mark Sample Data
//        int year = 2016;// generate a year between 1900 and 2010;
//        int dayOfYear = 4;// generate a number between 1 and 365 (or 366 if you need to handle leap year);
//        final List<Date> dates = new ArrayList<>();
//        final List<Date> dates1 = new ArrayList<>();
//        for (int i = 6; i < 10; i++) {
//            calendar.set(Calendar.YEAR, 2016);
//            calendar.set(Calendar.DAY_OF_MONTH, i);
//            final Date randomDoB = calendar.getTime();
//            dates.add(randomDoB);
//        }
//        // 0 Jan, 1 Feb
//        calendar.set(Calendar.YEAR, 2016);
//        calendar.set(Calendar.MONTH, 4);
//        calendar.set(Calendar.DAY_OF_MONTH, 15);
//        dates1.add(calendar.getTime());
//
//        calendar.set(Calendar.YEAR, 2016);
//        calendar.set(Calendar.MONTH, 4);
//        calendar.set(Calendar.DAY_OF_MONTH, 19);
//        dates1.add(calendar.getTime());
//
//        calendar.set(Calendar.YEAR, 2016);
//        calendar.set(Calendar.MONTH, 4);
//        calendar.set(Calendar.DAY_OF_MONTH, 23);
//        dates1.add(calendar.getTime());
//
//        calendar.set(Calendar.YEAR, 2016);
//        calendar.set(Calendar.MONTH, 5);
//        calendar.set(Calendar.DAY_OF_MONTH, 19);
//        dates1.add(calendar.getTime());
//
//        final Map<Date, Date> myMap = new HashMap<Date, Date>();
//        myMap.put(dates1.get(0), dates1.get(1));
//        myMap.put(dates1.get(2), dates1.get(3));

// input zo range bao nhieu nam, input list ngày cần highlight)

        btn_timePredictor = (ButtonRectangle) findViewById(R.id.btn_time_predictor);
        btn_timePredictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvTimeResults.setText("");
                    int temp;
                    Boolean isCheck = swBoyorGirl.isChecked();
                    if (isCheck) temp = 1;
                    else temp = 0;
                    List<Map<Date, Date>> result = getMonthXXX(temp);

                    String title = "Prediction time";
                    showCalendarInDialog(title, R.layout.dialog_calendarsquare);
                    final Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, Integer.parseInt(spRangeOfAge.getSelectedItem().toString()));
                    dialogView.init(today, nextYear.getTime())
                            .inMode(CalendarPickerView.SelectionMode.SINGLE)
                                    //.withSelectedDates(dates1)
                                    //.withHighlightedDates(dates)
                            .withHighlightedRangeDates(result)
                            .displayOnly();

                    ;
                    // set mode RANGE, input select 2 ngay
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

    /**
     * THis fucntion to show the calendar square Mark
     *
     * @param title
     * @param layoutResId
     */
    private void showCalendarInDialog(String title, int layoutResId) {
        dialogView = (CalendarPickerView) getLayoutInflater().inflate(layoutResId, null, false);
        theDialog = new AlertDialog.Builder(this) //
                .setTitle(title)
                .setView(dialogView)
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialogView.fixDialogDimens();
            }
        });
        theDialog.show();
    }

    public int getLastDate(int month, int year) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.set(year, month, 2);
        int maxDay = dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //return "Last date of " + monthName + " :" + maxDay;
        return maxDay;
    }

    public Map<Date, Date> convertLunarMonthToSolarMonthRange(int luMonth, int luYear) {
        Map<Date, Date> range = new HashMap<Date, Date>();
        java.text.DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date firstDay = null, lastDay = null;
        String solarMonthRange = "";

        Lunar luDate = new Lunar();
        Solar soDate = new Solar();

        luDate.lunarYear = luYear;
        luDate.lunarMonth = luMonth;

        //Convert first date of a month
        luDate.lunarDay = 1;
        soDate = LunarSolarConverter.LunarToSolar(luDate);
        solarMonthRange = soDate.solarMonth + "/" + soDate.solarDay + "/" + soDate.solarYear;
        try {
            firstDay = df.parse(solarMonthRange);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Convert last date of a month
        //Find last date of a month
        luDate.lunarDay = getLastDate(luMonth, luYear);
        soDate = LunarSolarConverter.LunarToSolar(luDate);
        solarMonthRange = soDate.solarMonth + "/" + soDate.solarDay + "/" + soDate.solarYear;
        try {
            lastDay = df.parse(solarMonthRange);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        range.put(firstDay, lastDay);
        return range;
    }

    public List<Map<Date, Date>> getMonthXXX(int temp) {
        List<Map<Date, Date>> listOfRange = new ArrayList<>();
        Map<Date, Date> range = new HashMap<Date, Date>();
        String rangeAges = spRangeOfAge.getSelectedItem().toString();
        int tmp_age = ageMom;
        int tmp_year = 0;
        String tmp_month = "";
        String tmp_solar_month = "";

        for (int i = 1; i <= 12; i++) {
            if (BoyorGirl(tmp_age, i) == temp) {
                tmp_month += ", " + i;
                range = convertLunarMonthToSolarMonthRange(i, thisYear + tmp_year);
                listOfRange.add(range);

                // show range of dates by textview
                Iterator<Map.Entry<Date, Date>> iterator = range.entrySet().iterator();
                Map.Entry<Date, Date> pairs = (Map.Entry<Date, Date>) iterator.next();
                Date start = pairs.getKey();
                Date end = pairs.getValue();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                tmp_solar_month += formatter.format(start.getTime()) + " --> " + formatter.format(end.getTime()) + "\n";
            }
            if (i == 12) {
                tvTimeResults.append("At the ages of " + tmp_age + " the baby prediction time in the range of dates: \n" + tmp_solar_month);
                tvTimeResults.append("\n");
                tmp_age++;
                i = 0;
                tmp_year++;
                tmp_solar_month = "";
            }
            if (tmp_year == Integer.parseInt(rangeAges)) {
                break;
            }
        }
        tvTimeResults.append(tmp_solar_month);
        return listOfRange;
    }

    public int BoyorGirl(int ageMom, int lunarPregnatMonth) {
        return PickDayActivity.magic_table[ageMom - 18][lunarPregnatMonth - 1];
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
