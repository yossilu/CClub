package com.example.user.cclub;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
public class UIAutomator {

    public UiDevice device;

    @Before
    public void setUp() {
        // Get the device instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        assertThat(device, notNullValue());

        // Start from the home screen
        device.pressHome();

    }


    private UiObject2 waitForObject(BySelector s) throws InterruptedException {
        UiObject2 object = null;
        int timeout = 25000;
        int delay = 1200;
        long time = System.currentTimeMillis();
        while (object == null) {
            UiObject2 mDevice;
            object = device.findObject(s);
            Thread.sleep(delay);
            if (System.currentTimeMillis() - timeout > time) {
                fail();
            }
        }
        return object;
    }


    private void openApp(String packageName) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }



    @Test
    public void test() throws InterruptedException {
        openApp("com.example.computer.books4u");

        UiObject2 email = waitForObject(By.res("com.example.user.cclub:id/userEmail"));

        email.setText("shaul@gmail.com");

        UiObject2 pass = waitForObject(By.res("com.example.user.cclub:id/userPass"));
        pass.setText("123456");


        UiObject2 protectObject = waitForObject(By.res("com.example.user.cclub:id/LoginBtn"));
        protectObject.click();


        Thread.sleep(8000);
    }



}

