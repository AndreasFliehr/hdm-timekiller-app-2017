package de.hdm.dp.bd.chronophage;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.hdm.dp.bd.chronophage.models.db.DbManager.ALL_TASK_NAMES;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

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
    public void taskList_onItemClick_toggleItemStart() throws InterruptedException {
        final int taskPosition = 0;
        clickListItemAt(taskPosition);

        final String taskName = ALL_TASK_NAMES.get(taskPosition);
        onView(withText(taskName + " started."))
            .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
        //click again to stop task and thereby reset its state
        Thread.sleep(1000);
        clickListItemAt(taskPosition);
    }

    @Test
    public void taskList_onItemClick_toggleItemEnd() throws InterruptedException {
        final int taskPosition = 0;
        clickListItemAt(taskPosition);
        Thread.sleep(1000);
        clickListItemAt(taskPosition);

        final String taskName = ALL_TASK_NAMES.get(taskPosition);

        onView(withText(taskName + " stopped."))
            .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    private void clickListItemAt(int atPosition) {
        onData(anything())
            .inAdapterView(withId(R.id.listView))
            .atPosition(atPosition)
            .perform(click());
    }
}
