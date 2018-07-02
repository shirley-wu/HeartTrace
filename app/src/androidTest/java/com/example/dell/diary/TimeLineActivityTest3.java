package com.example.dell.diary;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TimeLineActivityTest3 {
    private static final String THE_NOTE_CONTENT = "我做了一个梦";

    @Rule
    public ActivityTestRule<TimeLineActivity> mActivityTestRule = new ActivityTestRule<>(TimeLineActivity.class);

    @Test
    public void mainActivityTest3() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.enter_bottle),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());
       /* 浮动按钮测试*/
        onView(withId(R.id.add_bottle_fab)).perform(click());
        /*新建一个瓶子*/
            /*保存*/
        onView(withText("新建一个瓶子##")).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.edit_In_BottleName))).perform(replaceText("测试内容"));
        onView(allOf(withText("保存"))).perform(click());
            /* 取消*/
        onView(withId(R.id.add_bottle_fab)).perform(click());
        onView(allOf(withId(R.id.edit_In_BottleName))).perform(replaceText("草稿"));
        onView(allOf(withText("取消"))).perform(click());

        /*测试recycleView*/
        onView(withId(R.id.recycler_bottle_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        /*进入瓶子封面*/
       /* 查看详情*/


        onView(withId(R.id.recycler_note_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.next_note)).perform(click());
        onView(withId(R.id.pre_note)).perform(click());
        pressBack();
        /* 添加一张纸条*/
        onView(withId(R.id.note_add_fab)).perform(click());
        pressBack();

    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
