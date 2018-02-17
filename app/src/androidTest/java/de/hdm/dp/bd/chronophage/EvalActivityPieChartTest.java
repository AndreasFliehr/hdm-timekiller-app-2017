package de.hdm.dp.bd.chronophage;

import android.app.Activity;
import android.content.Context;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdm.dp.bd.chronophage.models.db.DbManager;
import de.hdm.dp.bd.chronophage.models.db.DbStatements;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.hdm.dp.bd.chronophage.models.db.DbManager.ALL_TASK_NAMES;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EvalActivityPieChartTest {
    private DbManager dbManager = new DbManager(TARGET_CONTEXT);
    private static final Context TARGET_CONTEXT = getTargetContext();
    private int year;
    private int month;
    private int day;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initTestDb() {
        DbManager.DATABASE_NAME = "guiTest.db";
        new DbManager(TARGET_CONTEXT);
    }

    @Before
    @After
    public void clearRecords() {
        dbManager.getWritableDatabase().delete(DbStatements
                .TABLE_NAME_DURATION,
            null,
            null
        );
    }

    @Test
    public void pieChart_taskListRecordsEmpty_containsNoValues() {
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXValCount());
    }

    @Test
    public void pieChart_taskListRecordsEmpty_containsNoLabels() {
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXVals().size());
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
        assertEquals(ALL_TASK_NAMES, chartData.getXVals());
    }

    @Test
    public void pieChart_startAndEndDateSelectedOutOfRange_containsNoValues() throws InterruptedException {
        preparePieChartTest();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());
        selectStartDate(2018, 1, 1);
        selectEndDate(2018, 1, 2);

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXValCount());
    }

    @Test
    public void pieChart_startAndEndDateSelectedOutOfRange_containsNoLabels() throws InterruptedException {
        preparePieChartTest();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());
        selectStartDate(2018, 1, 1);
        selectEndDate(2018, 1, 2);

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXVals().size());
    }

    @Test
    public void pieChart_startAndEndDateSelectedInRange_containsValues() throws InterruptedException {
        clickFirstThreeItems();
        clickFirstThreeItems();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());
        getCurrentDate();
        selectStartDate(year, month, day);
        selectEndDate(year, month, day + 1);

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(3, chartData.getXValCount());
    }

    @Test
    public void pieChart_startAndEndDateSelectedInRange_containsLabels() throws InterruptedException {
        clickFirstThreeItems();
        clickFirstThreeItems();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(R.string.evaluation)).perform(click());
        getCurrentDate();
        selectStartDate(year, month, day);
        selectEndDate(year, month, day + 1);

        Activity act = getCurrentActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        ArrayList<String> expectedTaskNames = new ArrayList<String>() {{
            add("Internet");
            add("Lesen");
            add("Mails");
        }};
        assertEquals(expectedTaskNames, chartData.getXVals());
    }

    private void clickFirstThreeItems() {
        clickListItemAt(0);
        clickListItemAt(1);
        clickListItemAt(2);
    }

    private void preparePieChartTest() throws InterruptedException {
        clickAllListItems();
        Thread.sleep(1000);
        clickAllListItems();
    }

    private void clickAllListItems() {
        for (int i = 0; i < 6; i++) {
            clickListItemAt(i);
        }
    }

    private void clickListItemAt(int atPosition) {
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(atPosition)
            .perform(click());
    }

    private void getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void selectStartDate(int year, int month, int day) {
        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void selectEndDate(int year, int month, int day) {
        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
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
            (view.getContext().getClass().getName().contains(
            "com.android.internal.policy.DecorContext"
        )) {
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
