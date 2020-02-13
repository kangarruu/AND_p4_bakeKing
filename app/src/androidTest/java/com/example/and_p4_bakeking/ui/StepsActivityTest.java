package com.example.and_p4_bakeking.ui;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4ClassRunner.class)
public class StepsActivityTest {

    @Rule public ActivityTestRule<StepsActivity> mActivityTestRule =
            new ActivityTestRule<>(StepsActivity.class);



}