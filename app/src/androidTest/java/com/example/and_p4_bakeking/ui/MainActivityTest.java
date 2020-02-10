package com.example.and_p4_bakeking.ui;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.and_p4_bakeking.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

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
    public void test_scrollToItem_checkItsText() {
        onView(ViewMatchers.withId(R.id.main_recipe_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));



    }









}