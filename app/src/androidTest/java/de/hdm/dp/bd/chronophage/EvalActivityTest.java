package de.hdm.dp.bd.chronophage;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdm.dp.bd.chronophage.database.TaskDatabaseInMemoryMock;
import de.hdm.dp.bd.chronophage.models.TaskList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EvalActivityTest {
    @Rule
    public ActivityTestRule<EvalActivity> mActivityRule = new ActivityTestRule<>(EvalActivity.class);
    private TaskList taskList;

    @Before
    public void setUp() {
        taskList = TaskDatabaseInMemoryMock.TASK_LIST;
    }

    @Test
    public void test() {
        onView(withText("Hello world!")).check(matches(isDisplayed()));
    }

    @Test
    public void pieChart_taskListRecordsEmpty_containsNoValues() {
        final EvalActivity act = mActivityRule.getActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXValCount());
    }

    @Test
    public void pieChart_taskListRecordsEmpty_containsNoLabels() {
        final EvalActivity act = mActivityRule.getActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXVals().size());
    }
}
