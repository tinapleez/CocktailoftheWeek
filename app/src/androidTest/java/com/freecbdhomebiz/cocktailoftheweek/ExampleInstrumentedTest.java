/*
 * Copyright (c) 2018. Tina Taylor
 * CREATIVE COMMONS Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * https://creativecommons.org/licenses/by-sa/3.0/
 */

package com.freecbdhomebiz.cocktailoftheweek;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.freecbdhomebiz.cocktailoftheweek", appContext.getPackageName());
    }
}
