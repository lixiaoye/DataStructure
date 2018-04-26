package lxy.android.com.datastructuredemo.week;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import lxy.android.com.datastructuredemo.R;

/**
 * Created by LIXIAOYE on 2018/4/26.
 */

public class WeekFragment extends Fragment implements ViewSwitcher.ViewFactory {

    private static final String TAG="WeekFragment";
    private int mNumDays;
    Time mSelectedDay = new Time();

    protected ViewSwitcher mViewSwitcher;

    private static final int VIEW_ID = 1;


    public WeekFragment(long timeMillis, int numOfDays) {
        Log.e(TAG,"WeekFragment构造函数");
        mNumDays = numOfDays;
        if (timeMillis == 0) {
            mSelectedDay.setToNow();
        } else {
            mSelectedDay.set(timeMillis);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"WeekFragment#onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"WeekFragment#onCreateView");
        View view = inflater.inflate(R.layout.fragment_week, null);
        mViewSwitcher = view.findViewById(R.id.switcher);
        mViewSwitcher.setFactory(this);
        mViewSwitcher.getCurrentView().requestFocus();

        return view;
    }

    @Override
    public View makeView() {
        Log.e(TAG,"WeekFragment#makeView");
        DayView dayView = new DayView(getActivity(), mViewSwitcher, mNumDays);
        dayView.setId(VIEW_ID);
        dayView.setLayoutParams(new ViewSwitcher.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        dayView.setSelected(false);
        return dayView;
    }
}
