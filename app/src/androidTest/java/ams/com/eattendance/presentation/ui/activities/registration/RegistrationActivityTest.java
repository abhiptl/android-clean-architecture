package ams.com.eattendance.presentation.ui.activities.registration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ams.com.eattendance.R;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistrationActivityTest {

	@Rule
	public ActivityTestRule<RegistrationActivity> registrationActivityActivityTestRule =
			new ActivityTestRule<RegistrationActivity>(RegistrationActivity.class);

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test_click_term_condition() {
		onView(withId(R.id.text_term_condition)).perform(click());
		onView(withText("Terms and Condition")).check(matches(isDisplayed()));
	}
}