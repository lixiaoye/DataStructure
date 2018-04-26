package lxy.android.com.datastructuredemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lxy.android.com.datastructuredemo.week.WeekFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = new WeekFragment(System.currentTimeMillis(), 7);
        ft.add(R.id.container, fragment, "WeekFragment");
        ft.commitAllowingStateLoss();
    }
}
