package com.pabu.raisingsuccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    ScheduleFragment scheduleFragment;
    CalendarFragment calendarFragment;
    MyInfoFragment myInfoFragment;


    private MenuItem prevMenuItem; // 이전에 선택된 메뉴 아이템

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleFragment = new ScheduleFragment();
        calendarFragment = new CalendarFragment();
        myInfoFragment = new MyInfoFragment();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationview);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.scheduleNevi) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, scheduleFragment).commit();
                    setItemSelected(item);
                } else if (itemId == R.id.calendarNevi) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, calendarFragment).commit();
                    setItemSelected(item);
                } else if (itemId == R.id.myInfoNevi) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, myInfoFragment).commit();
                    setItemSelected(item);
                }
                return true;
            }
        });

        // 앱을 실행할 때 scheduleNevi 아이템을 선택한 상태로 설정
        MenuItem scheduleMenuItem = bottomNavigationView.getMenu().findItem(R.id.scheduleNevi);
        setItemSelected(scheduleMenuItem);
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, scheduleFragment).commit();
    }

    // 아이템 선택 상태 변경 및 아이콘 주변 배경색 변경 메서드
    private void setItemSelected(MenuItem item) {
        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        }
        item.setChecked(true);
        prevMenuItem = item;

        // 아이콘 주변 배경색 변경
        int itemId = item.getItemId();
        if (itemId == R.id.scheduleNevi) {
            // scheduleNevi 아이콘 주변 배경색 변경
            // 예시: item.getActionView().setBackgroundResource(R.drawable.schedule_background);
        } else if (itemId == R.id.calendarNevi) {
            // calendarNevi 아이콘 주변 배경색 변경
            // 예시: item.getActionView().setBackgroundResource(R.drawable.calendar_background);
        } else if (itemId == R.id.myInfoNevi) {
            // myInfoNevi 아이콘 주변 배경색 변경
            // 예시: item.getActionView().setBackgroundResource(R.drawable.myinfo_background);
        }
    }
}