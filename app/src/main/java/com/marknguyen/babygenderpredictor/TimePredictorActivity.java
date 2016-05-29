package com.marknguyen.babygenderpredictor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kyleduo.switchbutton.SwitchButton;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Utils.MultiValueMap;
import blurdialogfragment.SampleDialogFragment1;
import materialdesign.views.ButtonRectangle;
import sola2lunar.Lunar;
import sola2lunar.LunarSolarConverter;
import sola2lunar.Solar;


public class TimePredictorActivity extends FragmentActivity {
    private ButtonRectangle btn_timePredictor;
    private ImageButton btn_chooseBirthday;
    private TextView tv_solarBirthday;
    private AlertDialog theDialog;
    private CalendarPickerView dialogView;
    public static int ageMom;
    private Spinner spRangeOfAge;
    private TextView tvTimeResults;
    private SwitchButton swBoyorGirl;
    private CheckBox cbBoy;
    private CheckBox cbGirl;
    private int mYear, mMonth, mDay;
    private TextView pickerSolarBirthday;
    private static int thisYear;
    private CalendarPickerView calendarView;
    private ScrollView scrollText;
    private boolean isExpectedBoy;
    private ImageView ivBoyOrGirl;
    private Lunar lunarBirthday;
    private Lunar lunarCurrent;
    private CalendarPickerView calendarPickerView;
    private ImageView ivConfusing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_predictor);

        scrollText = (ScrollView) findViewById(R.id.scroll_text);
        pickerSolarBirthday = (TextView) findViewById(R.id.long_date);
        spRangeOfAge = (Spinner) findViewById(R.id.spinner_rangeOfAges);
        tvTimeResults = (TextView) findViewById(R.id.tv_time_results);
        swBoyorGirl = (SwitchButton) findViewById(R.id.sw_boy_or_girl);
        ivBoyOrGirl = (ImageView) findViewById(R.id.iv_boy_or_girl);
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view_date);
        ivConfusing = (ImageView) findViewById(R.id.iv_confusing);

        FloatingActionButton fab_info = (FloatingActionButton) findViewById(R.id.fab_info1);
        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SampleDialogFragment1 fragment
                        = SampleDialogFragment1.newInstance();
                fragment.show(getFragmentManager(), "blur_sample");
            }
        });

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Date today = new Date();

        final Calendar calendar = Calendar.getInstance();
        thisYear = calendar.get(Calendar.YEAR);

        pickerSolarBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        calendarView = (CalendarPickerView) findViewById(R.id.calendar_view_date);
        calendarView.init(today, nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .displayOnly();

        swBoyorGirl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ivBoyOrGirl.setImageResource(R.drawable.boy2);
                } else {
                    ivBoyOrGirl.setImageResource(R.drawable.girl2);
                }
            }
        });
        btn_timePredictor = (ButtonRectangle) findViewById(R.id.btn_time_predictor);

        btn_timePredictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    scrollText.setVisibility(View.VISIBLE);
                    tvTimeResults.setText("");
                    int temp = 0;
                    Boolean isCheck = swBoyorGirl.isChecked();
                    if (isCheck) {
                        temp = 1;
                        isExpectedBoy = true;
                    } else {
                        temp = 0;
                        isExpectedBoy = false;
                    }

                    List<Map<Date, Date>> result = getMonthXXX(temp);

                    final Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, Integer.parseInt(spRangeOfAge.getSelectedItem().toString()));

                    ivConfusing.setVisibility(View.GONE);
//                    calendarView.setCustomDayView(new SampleDayViewAdapter());
                    calendarPickerView.setVisibility(View.VISIBLE);
                    calendarView.init(today, nextYear.getTime()) //
                            .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                            .withHighlightedRangeDates(result)
                            .displayOnly();

                } catch (Exception e) {
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.long_date));
                    Toast.makeText(TimePredictorActivity.this, "Please check the input again!", Toast.LENGTH_SHORT).show();
                    scrollText.setVisibility(View.GONE);
                }

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

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker myCalendar, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                mYear = selectedYear;
                mMonth = selectedMonth;
                mDay = selectedDay;
                updateLabel(mYear, mMonth, mDay );
                updateResult(selectedYear, selectedMonth, selectedDay);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TimePredictorActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth, datePickerListener, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ageMom = 0;
    }

    public void updateLabel(int year, int monthOfYear, int dayOfMonth) {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        myCalendar.set(year, monthOfYear, dayOfMonth);
        pickerSolarBirthday.setText(sdf.format(myCalendar.getTime()));

    }

    /**
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    public void updateResult(int year, int monthOfYear, int dayOfMonth) {
        Solar solar = new Solar();
        solar.solarYear = year;
        solar.solarMonth = monthOfYear + 1;
        solar.solarDay = dayOfMonth;
        lunarBirthday = LunarSolarConverter.SolarToLunar(solar);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String[] curr = currentDate.split("-");
        Solar solar_curr = new Solar();
        solar_curr.solarDay = Integer.parseInt(curr[2]);
        solar_curr.solarMonth = Integer.parseInt(curr[1]);
        solar_curr.solarYear = Integer.parseInt(curr[0]);
        lunarCurrent = LunarSolarConverter.SolarToLunar(solar_curr);

        ageMom = lunarCurrent.lunarYear - lunarBirthday.lunarYear;
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

    /**
     * @param luMonth
     * @param luYear
     * @return
     */
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

    public String getMonthList(ArrayList<Integer> listOfMonth) {
        String tmplistOfMonth = "";
        for (Integer i : listOfMonth) {
            tmplistOfMonth += i + ", ";
        }
        return tmplistOfMonth;
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

        int tmp_age = ageMom; int tmp_year = 0;
        int tmp_total = 0; String tmp_month = "";
        String gender = ""; int tmpStartYear;
        int tmpEndYear; int tmpStartMonth;
        int tmpEndMonth;

        MultiValueMap<Integer, Integer> listOfMonth = new MultiValueMap<Integer, Integer>();
        Map<Integer, List<Integer>> listData = new TreeMap<>();

        if (isExpectedBoy)
            gender = "boy";
        else
            gender = "girl";

        tvTimeResults.append("To conceive a " + gender + " baby, try to have sexual intercourse in the following months (Please find detail dates in the calendar bellow): \n");
        for (int i = 1; i <= 12; i++) {
            if (BoyorGirl(tmp_age, i) == temp) {
                tmp_total = thisYear + tmp_year; // year after + range of years
                range = convertLunarMonthToSolarMonthRange(i, tmp_total);
                listOfRange.add(range);

                // get range of month
                SimpleDateFormat formatter_month = new SimpleDateFormat("M");
                SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy");

                Iterator<Map.Entry<Date, Date>> iterator = range.entrySet().iterator();
                Map.Entry<Date, Date> pairs = iterator.next();
                Date start = pairs.getKey();
                Date end = pairs.getValue();
                //get month
                tmpStartMonth = Integer.parseInt(formatter_month.format(start.getTime()).toString());
                tmpEndMonth = Integer.parseInt(formatter_month.format(end.getTime()).toString());
                tmpStartYear = Integer.parseInt(formatter_year.format(start.getTime()).toString());
                tmpEndYear = Integer.parseInt(formatter_year.format(end.getTime()).toString());
                listOfMonth.putValue(tmpStartYear, tmpStartMonth);
                listOfMonth.putValue(tmpEndYear, tmpEndMonth);
            }

            if (i == 12) {
                tmp_age++; tmp_year++; i = 0;
            }
            if (tmp_year == Integer.parseInt(rangeAges)) {
                break;
            }
        }
        // show range of month by textview
        listData = listOfMonth.getData();
        Iterator<Integer> iter = listData.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = iter.next();
            List<Integer> value = listData.get(key);
            Collections.sort(value);
            for(Integer item : value ){
                tmp_month += item + ", ";
            }
            tvTimeResults.append("At the year of " + key + ": " + tmp_month.substring(0,tmp_month.length()-2) + "\n");
            tmp_month = "";
        }
        return listOfRange;
    }

    /**
     * @param ageMom
     * @param lunarPregnatMonth
     * @return
     */
    public int BoyorGirl(int ageMom, int lunarPregnatMonth) {
        return PickDayActivity.magic_table[ageMom - 18][lunarPregnatMonth - 1];
    }

}
