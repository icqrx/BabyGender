package com.marknguyen.babygenderpredictor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
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
    private CalendarPickerView calendarView;
    private ScrollView scrollText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_predictor);

        scrollText = (ScrollView) findViewById(R.id.scroll_text);
        pickerSolarBirthday = (DatePicker) findViewById(R.id.long_date);
        tv_lunarAge = (TextView) findViewById(R.id.tv_lunar_birthday2);
        spRangeOfAge = (Spinner) findViewById(R.id.spinner_rangeOfAges);
        tvTimeResults = (TextView) findViewById(R.id.tv_time_results);
        swBoyorGirl = (Switch) findViewById(R.id.sw_boyorgirl);
        // Init
        tv_lunarAge.setText("Lunar age: 25");
        ageMom = 25;

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Date today = new Date();


        Calendar calendar = Calendar.getInstance();
        thisYear = calendar.get(Calendar.YEAR);

        calendarView = (CalendarPickerView) findViewById(R.id.calendar_view_date);
        calendarView.init(today, nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .displayOnly();

        btn_timePredictor = (ButtonRectangle) findViewById(R.id.btn_time_predictor);
        btn_timePredictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    scrollText.setVisibility(View.VISIBLE);
                    tvTimeResults.setText("");
                    int temp;
                    Boolean isCheck = swBoyorGirl.isChecked();
                    if (isCheck) temp = 1;
                    else temp = 0;
                    List<Map<Date, Date>> result = getMonthXXX(temp);

                    final Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, Integer.parseInt(spRangeOfAge.getSelectedItem().toString()));

                    //calendarView.setCustomDayView(new SampleDayViewAdapter());
                    calendarView.init(today, nextYear.getTime()) //
                            .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                            .withHighlightedRangeDates(result)
                            .displayOnly();

                } catch (Exception e) {
                    Toast.makeText(TimePredictorActivity.this, "Please check the input again!", Toast.LENGTH_SHORT).show();
                    scrollText.setVisibility(View.GONE);
                }

            }
        });

        pickerSolarBirthday.setDateFormat(DateFormat.getDateFormat(TimePredictorActivity.this));

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

    /**
     * get range XXX
     *
     * @param temp
     * @return
     */
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
                Map.Entry<Date, Date> pairs = iterator.next();
                Date start = pairs.getKey();
                Date end = pairs.getValue();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                tmp_solar_month += formatter.format(start.getTime()) + " --> " + formatter.format(end.getTime()) + "\n";
            }
            if (i == 12) {
                tvTimeResults.append("At the ages of " + tmp_age + " the baby prediction time in the range of dates (dd/mm/yyyy): \n" + tmp_solar_month);
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
