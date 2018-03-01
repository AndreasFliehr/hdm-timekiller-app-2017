package de.hdm.dp.bd.chronophage;

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

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.EditText;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EvalActivityDatePickerTest {
    @Rule
    public ActivityTestRule<EvalActivity> rule = new ActivityTestRule<>(EvalActivity.class);

    @Test
    public void datePicker_startDateSelected_isDisplayedCorrectly() {
        selectStartDate(1);
        final EvalActivity act = rule.getActivity();
        EditText startDateEditText = act.findViewById(R.id.startDateEditText);
        assertEquals("01.01.2018", startDateEditText.getText().toString());
    }

    @Test
    public void datePicker_endDateSelected_isDisplayedCorrectly() {
        selectEndDate(2);
        final EvalActivity act = rule.getActivity();
        EditText endDateEditText = act.findViewById(R.id.endDateEditText);
        assertEquals("02.01.2018", endDateEditText.getText().toString());
    }

    @Test
    public void datePicker_startDateAfterEndDate_cantBeApplied() {
        selectEndDate(1);
        selectStartDate(2);

        onView(withText("Start Date must be before end!"))
            .inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void datePicker_endDateBeforeStartDate_cantBeApplied() {
        selectStartDate(2);
        selectEndDate(1);

        onView(withText("End Date must be after start!"))
            .inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void datePicker_startDateAfterEndDate_noChangesInPresentation() {
        selectEndDate(1);
        selectStartDate(2);

        final EvalActivity act = rule.getActivity();
        EditText startDateEditText = act.findViewById(R.id.startDateEditText);
        assertEquals("Start date", startDateEditText.getText().toString());
    }

    @Test
    public void datePicker_endDateBeforeStartDate_noChangesInPresentation() {
        selectStartDate(2);
        selectEndDate(1);

        final EvalActivity act = rule.getActivity();
        EditText endDateEditText = act.findViewById(R.id.endDateEditText);
        assertEquals("End date", endDateEditText.getText().toString());
    }

    private void selectStartDate(int day) {
        onView(withId(R.id.startDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .perform(PickerActions.setDate(2018, 1, day));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void selectEndDate(int day) {
        onView(withId(R.id.endDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .perform(PickerActions.setDate(2018, 1, day));
        onView(withId(android.R.id.button1)).perform(click());
    }
}
