package ru.denfad.dks;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.denfad.dks.model.Day;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.BaseMarkView;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.MarkedDates;

public class MainActivity extends AppCompatActivity {

    String[] monthes = {"Январь", "Февраль", "Март", "Апрель","Май","Июнь","Июль","Август","Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    int[] daysInMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
    String[] time;
    MCalendarView calendarView;
    int work=2;
    int week=2;
    int startDay=18;
    Day[] actualDays;
    Map<Integer, List<Day[]>> drawDays = new HashMap();
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int schedule = sharedPreferences.getInt("schedule", R.id.ched_1);

        switch (schedule){
            case R.id.ched_1:{
                work = 2;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
            case R.id.ched_2:{
                work = 5;
                week = 2;
                time = new String[]{"Утро", "День","Ночь"};
                break;
            }
            case R.id.ched_3:{
                work = 5;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
            case R.id.ched_4:{
                work = 4;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
        }
        calendarView = findViewById(R.id.calendar);


        startDay = sharedPreferences.getInt("date", 17);
        actualDays = getMassive(work, week,Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR), startDay );
        redraw(Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR),actualDays);



        final TextView monthText = findViewById(R.id.month);
        final TextView yearText = findViewById(R.id.year);
        final TextView timeText = findViewById(R.id.time);
        final TextView dayOfWeek = findViewById(R.id.dayOfWeek);
        ImageButton button = findViewById(R.id.adding);

        Calendar calendar = new GregorianCalendar();
        dayOfWeek.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.ENGLISH));
        timeText.setText(actualDays[startDay-1].hour);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                for(int i=0; i<10;i++) {
                    MarkedDates markedDates = calendarView.getMarkedDates();
                    ArrayList markData = markedDates.getAll();
                    for (int k = 0; k < markData.size(); k++) {
                        calendarView.unMarkDate((DateData) markData.get(k));
                    }
                }
            }
        });

        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {

            @Override
            public void onMonthChange(int year, int month) {
                monthText.setText(monthes[month-1]);
                yearText.setText(String.valueOf(year));
                actualDays = getMassive(work,week,month,year,getRealDelta(actualDays,week,work));
                redraw(month,year,actualDays);
            }
        });

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                Calendar calendar = new GregorianCalendar();
                calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
                dayOfWeek.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.ENGLISH));
                timeText.setText(actualDays[date.getDay()-1].hour);

            }
        });

        calendarView.hasTitle(false);

    }


    @Override
    protected void onStop() {
        actualDays = null;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        actualDays = null;
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int schedule = sharedPreferences.getInt("schedule", R.id.ched_1);

        switch (schedule){
            case R.id.ched_1:{
                work = 2;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
            case R.id.ched_2:{
                work = 5;
                week = 2;
                time = new String[]{"Утро", "День","Ночь"};
                break;
            }
            case R.id.ched_3:{
                work = 5;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
            case R.id.ched_4:{
                work = 4;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
        }


        startDay = sharedPreferences.getInt("date", 17);
        actualDays = getMassive(work, week,Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR), startDay );
        redraw(Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR),actualDays);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int schedule = sharedPreferences.getInt("schedule", R.id.ched_1);

        switch (schedule){
            case R.id.ched_1:{
                work = 2;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
            case R.id.ched_2:{
                work = 5;
                week = 2;
                time = new String[]{"Утро", "День","Ночь"};
                break;
            }
            case R.id.ched_3:{
                work = 5;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
            case R.id.ched_4:{
                work = 4;
                week = 2;
                time = new String[]{"Утро", "Вечер"};
                break;
            }
        }

        startDay  = sharedPreferences.getInt("date", 17);
        actualDays = getMassive(work, week,Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR), startDay );
        redraw(Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR),actualDays);

    }

    private void redraw(int month, int year, Day[] days){


        for(int i=1; i<=days.length; i++){
            if(days[i-1].isWork){
                switch (days[i-1].hour){
                    case "Утро":
                        calendarView.markDate(new DateData(year, month, i).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getColor(R.color.work))));
                        break;
                    case "День":
                        calendarView.markDate(new DateData(year, month, i).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getColor(R.color.week))));
                        break;
                    case "Вечер":
                        calendarView.markDate(new DateData(year, month, i).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getColor(R.color.week))));
                        break;
                    case "Ночь":
                        calendarView.markDate(new DateData(year, month, i).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getColor(R.color.our_gray))));
                        break;
                }

            }

        }
    }

    public Day[] getMassive(int work, int week, int month,  int year, int startDate){

        int colDayInMonth;
        if(year % 4 == 0 & month == 2) colDayInMonth = daysInMonth[month-1]+1;
        else colDayInMonth = daysInMonth[month-1];
        Day[] days = new Day[colDayInMonth];

        Log.d("startDate", String.valueOf(startDate));

        int n = 0;
        for(int i = startDate-1; i<colDayInMonth; i=i+work+week){
            for(int j=0; j<work; j++){
                try {
                    days[i+j] = new Day(true, time[n]);
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            for(int k=0; k<week; k++){
                try {
                    days[i + k + work] =  new Day(false, "Выходной");
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            n++;
            if(n >= time.length)n = 0;
        }

        n=time.length-1;
        for(int i = startDate-1; i>=0; i=i-work-week){
            for(int k=1; k<=week; k++){
                try {
                    days[i-k] =  new Day(false, "Выходной");;
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            for(int j=1; j<=work; j++){
                try {
                    days[i - week - j] = new Day(true, time[n]);
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            n--;
            if(n <= 0) n = time.length-1;
        }

        return days;

//        int colDayInMonth;
//        if(year % 4 == 0 & month == 2) colDayInMonth = daysInMonth[month-1]+1;
//        else colDayInMonth = daysInMonth[month-1];
//        Day[] days = new Day[colDayInMonth];
//
//        Log.d("startDate", String.valueOf(startDate));
//
//        int n = 0;
//        for(int i = startDate-1; i<colDayInMonth; i=i+work+week){
//            for(int j=0; j<work; j++){
//                try {
//                    days[i+j] = new Day(true, time[n]);
//                }catch(ArrayIndexOutOfBoundsException e){
//                    e.printStackTrace();
//                }
//            }
//            for(int k=0; k<week; k++){
//                try {
//                    days[i + k + work] =  new Day(false, "Выходной");
//                }catch(ArrayIndexOutOfBoundsException e){
//                    e.printStackTrace();
//                }
//            }
//            n++;
//            if(n >= time.length)n = 0;
//
//        }
//
//        n=time.length-1;
//        for(int i = startDate-1; i>=0; i=i-work-week){
//            for(int k=1; k<=week; k++){
//                try {
//                    days[i-k] =  new Day(false, "Выходной");
//                }catch(ArrayIndexOutOfBoundsException e){
//                    e.printStackTrace();
//                }
//            }
//            for(int j=1; j<=work; j++){
//                try {
//                    days[i - week - j] =  new Day(true, time[n]);
//                }catch(ArrayIndexOutOfBoundsException e){
//                    e.printStackTrace();
//                }
//            }
//            n--;
//            if(n <= 0) n = time.length-1;
//        }
//
//        return days;
    }

    public int getRealDelta(Day[] days, int week,int work){
        boolean b;
        int n=0;
        b = days[days.length-1].isWork;
        if(b){
            for(int i = days.length-2; i >= days.length-week-work; i--){
                if(days[i].isWork != b) {
                    break;
                }
                else n--;
            }
        }
        else{
            n=week-1;
            for(int i = days.length-2; i >= days.length-week-work; i--){
                if(days[i].isWork != b) {
                    break;
                }
                else n = n-1;
            }
        }
        return n;

    }

    @Override
    public void onBackPressed() {
        super.finishAffinity();
    }
}