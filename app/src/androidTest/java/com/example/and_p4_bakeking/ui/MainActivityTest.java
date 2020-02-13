package com.example.and_p4_bakeking.ui;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.utilities.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class MainActivityTest {

    private EspressoIdlingResource mIdlingResource;

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);



    //Register an idlingResource before the test starts
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

    }

    @Test
    public void test_isActivityInView() {
        onView(withId(R.id.main_parent)).check(matches(isDisplayed()));
    }

    @Test
    public void test_clickRecipeOpensStepsActivity() {
        onView(withId(R.id.main_recipe_rv)).perform(click());
        onView(withId(R.id.steps_parent)).check(matches(isDisplayed()));
    }

    @Test
    public void test_clickRecipeAtPosition_OpensCorrectRecipe() {
        onView(ViewMatchers.withId(R.id.main_recipe_rv)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        String itemElementText = getApplicationContext().getResources().getString(R.string.test_element_string);
        onView(ViewMatchers.withText(itemElementText)).check(matches(isDisplayed()));
    }


    @After
    public void unregisterIdlingResource(){
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}