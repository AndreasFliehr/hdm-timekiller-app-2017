package de.hdm.dp.bd.chronophage;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EvalActivityTest {
    @Rule
    public ActivityTestRule<EvalActivity> mActivityRule = new ActivityTestRule<>(EvalActivity.class);

    @Test
    public void test() {
        onView(withText("Hello world!")).check(matches(isDisplayed()));
    }
}
