package com.example.user.cclub;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import Model.User;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTesting {


    @Test
    public void checkStringFromContext() {

        User user = Mockito.mock(User.class);

        Mockito.when(user.getPhoneNumber()).thenReturn("0542096095");

        assertEquals(user.getPhoneNumber(),"0542096095");

    }

}
