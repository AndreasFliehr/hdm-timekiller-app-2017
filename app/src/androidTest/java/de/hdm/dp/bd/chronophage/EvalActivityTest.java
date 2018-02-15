package de.hdm.dp.bd.chronophage;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EvalActivityTest {
    @Rule
    public ActivityTestRule<EvalActivity> eActivityRule = new ActivityTestRule<>(EvalActivity.class);

    @Test
    public void pieChart_taskListRecordsEmpty_containsNoValues() {
        final EvalActivity act = eActivityRule.getActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXValCount());
    }

    @Test
    public void pieChart_taskListRecordsEmpty_containsNoLabels() {
        final EvalActivity act = eActivityRule.getActivity();
        PieChart pieChart = act.findViewById(R.id.chart);
        ChartData chartData = pieChart.getData();
        assertEquals(0, chartData.getXVals().size());
    }

    @Test
    public void datePicker_startDateSelected_isDisplayedCorrectly() {
        selectStartDate(2018, 1, 1);
        final EvalActivity act = eActivityRule.getActivity();
        EditText startDateEditText = act.findViewById(R.id.startDateEditText);
        assertEquals("01.01.2018", startDateEditText.getText().toString());
    }

    @Test
    public void datePicker_endDateSelected_isDisplayedCorrectly() {
        selectEndDate(2018, 1, 2);
        final EvalActivity act = eActivityRule.getActivity();
        EditText endDateEditText = act.findViewById(R.id.endDateEditText);
        assertEquals("02.01.2018", endDateEditText.getText().toString());
    }

    @Test
    public void datePicker_startDateAfterEndDate_cantBeApplied() {
        selectEndDate(2018, 1, 1);
        selectStartDate(2018, 1, 2);

        onView(withText("Start Date must be before end!"))
            .inRoot(withDecorView(not(is(eActivityRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void datePicker_EndDateBeforeStartDate_cantBeApplied() {
        selectStartDate(2018, 1, 2);
        selectEndDate(2018, 1, 1);

        onView(withText("End Date must be after start!"))
            .inRoot(withDecorView(not(is(eActivityRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void datePicker_startDateAfterEndDate_noChangesInPresentation() {
        selectEndDate(2018, 1, 1);
        selectStartDate(2018, 1, 2);

        final EvalActivity act = eActivityRule.getActivity();
        EditText startDateEditText = act.findViewById(R.id.startDateEditText);
        assertEquals("Start date", startDateEditText.getText().toString());
    }

    @Test
    public void datePicker_EndDateBeforeStartDate_noChangesInPresentation() {
        selectStartDate(2018, 1, 2);
        selectEndDate(2018, 1, 1);

        final EvalActivity act = eActivityRule.getActivity();
        EditText endDateEditText = act.findViewById(R.id.endDateEditText);
        assertEquals("End date", endDateEditText.getText().toString());
    }

    public static void selectStartDate(int year, int month, int day) {
        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void selectEndDate(int year, int month, int day) {
        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
    }
}
