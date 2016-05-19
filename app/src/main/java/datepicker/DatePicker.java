package datepicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.marknguyen.babygenderpredictor.TimePredictorActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import sola2lunar.Lunar;
import sola2lunar.LunarSolarConverter;
import sola2lunar.Solar;

public class DatePicker extends EditText implements DatePickerDialog.OnDateSetListener {
	public interface OnDateSetListener {
		void onDateSet(DatePicker view, int year, int month, int day);
	}
	private Lunar lunarBirthday;
	private Lunar lunarCurrent;
	protected int year;
	protected int month;
	protected int day;
	protected long maxDate = -1;
	protected long minDate = -1;
	protected OnDateSetListener onDateSetListener = null;
	protected java.text.DateFormat dateFormat;
	
	public OnDateSetListener getOnDateSetListener() {
		return onDateSetListener;
	}

	public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
		this.onDateSetListener = onDateSetListener;
	}
	
	public DatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);

		dateFormat = DateFormat.getMediumDateFormat(getContext());
		
		setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
		setFocusable(false);
		setToday();
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		updateText();
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
		updateText();
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
		updateText();
	}
	
	public void setDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		
		updateText();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			showDatePicker();
		
		return super.onTouchEvent(event);
	}
	
	public java.text.DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(java.text.DateFormat dateFormat) {
		this.dateFormat = dateFormat;
		updateText();
	}
	
	public void setMaxDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, 23, 59, 59);
		this.maxDate = c.getTimeInMillis();
	}

	public void setMinDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, 0, 0, 0);
		this.minDate = c.getTimeInMillis();
	}
	
	public void setToday() {
		Calendar c = Calendar.getInstance();
		//setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		setDate(1991, Calendar.AUGUST, 20);
	}

	protected void showDatePicker() {
		DatePickerDialog datePickerDialog;
		
		datePickerDialog = new DatePickerDialog(
				getContext(),
				this,
				1991,
				Calendar.AUGUST,
				20)
//				getYear(),
//				getMonth(),
//				getDay())
		;
		
		if (this.maxDate != -1) {
			datePickerDialog.getDatePicker().setMaxDate(maxDate);
		}
		if (this.minDate != -1) {
			datePickerDialog.getDatePicker().setMinDate(minDate);
		}
		
		datePickerDialog.show();
	}

	@Override
	public void onDateSet(android.widget.DatePicker view, int year,
			int monthOfYear, int dayOfMonth) {
		setDate(year, monthOfYear, dayOfMonth);
		clearFocus();

		Solar solar = new Solar();
		solar.solarYear = year;
		solar.solarMonth = monthOfYear+1;
		solar.solarDay = dayOfMonth;
		lunarBirthday = LunarSolarConverter.SolarToLunar(solar);

		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String[] curr = currentDate.split("-");
		Solar solar_curr = new Solar();
		solar_curr.solarDay = Integer.parseInt(curr[2]);
		solar_curr.solarMonth = Integer.parseInt(curr[1]);
		solar_curr.solarYear = Integer.parseInt(curr[0]);
		lunarCurrent = LunarSolarConverter.SolarToLunar(solar_curr);

		TimePredictorActivity.ageMom = lunarCurrent.lunarYear - lunarBirthday.lunarYear;

//		if(onDateSetListener != null)
//			onDateSetListener.onDateSet(this, year, month, day);
	}
	
	public void updateText() {
		Calendar cal = new GregorianCalendar(getYear(), getMonth(), getDay());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		setText(formatter.format(cal.getTime()));
	}
}
