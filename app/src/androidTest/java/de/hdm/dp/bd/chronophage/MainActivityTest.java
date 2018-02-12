package de.hdm.dp.bd.chronophage;

import android.app.Activity;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void taskList_activityStarted_isDisplayed() {
        onView(withId(R.id.listView))
            .check(matches(isDisplayed()));
    }

    @Test
    public void taskList_activityStarted_hasDefaultValues() {
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(2)
            .perform(click());
    }

    @Test
    public void taskList_onItemClick_toggleItemStart() throws InterruptedException {
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(0)
            .perform(click());

        onView(withText("Internet started."))
            .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void taskList_onItemClick_toggleItemEnd() throws InterruptedException {
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(0)
            .perform(click());
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(0)
            .perform(click());

        onView(withText("Internet stopped."))
            .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void pieChart_taskListRecordsFilled_containsValues() throws InterruptedException {
        preparePieChartTest();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(6, chartData.getXValCount());
    }

    @Test
    public void pieChart_taskListRecordsFilled_containsLabels() throws InterruptedException {
        preparePieChartTest();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        ArrayList<String> expectedChartDataVals = new ArrayList<String>() {{
            add("Internet");
            add("Lesen");
            add("Mails");
            add("Putzen");
            add("Spielen");
            add("Vorlesungen");
        }};
        assertEquals(expectedChartDataVals, chartData.getXVals());
    }

    private void preparePieChartTest() throws InterruptedException {
        clickAllListItems();
        Thread.sleep(1000);
        clickAllListItems();
    }

    private void clickAllListItems() {
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(0)
            .perform(click());
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(1)
            .perform(click());
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(2)
            .perform(click());
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(3)
            .perform(click());
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(4)
            .perform(click());
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(5)
            .perform(click());
    }

    private Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        onView(isRoot()).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (android.os.Build.VERSION.SDK_INT == 24) {
                    activity[0] = defineActivity(view);
                } else {
                    activity[0] = (Activity) view.getContext();
                }
            }
        });
        return activity[0];
    }

    private Activity defineActivity(View view) {
        Activity a = null;
        if
            (view.getContext().getClass().getName().contains("com.android.internal.policy.DecorContext")) {
            try {
                Field field = view.getContext().getClass().getDeclaredField("mPhoneWindow");
                field.setAccessible(true);
                Object obj = field.get(view.getContext());
                java.lang.reflect.Method m1 = obj.getClass().getMethod("getContext");
                a = (Activity) (m1.invoke(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            a = (Activity) view.getContext();
        }
        return a;
    }
}
