package com.np6.npush;

import static org.junit.Assert.assertEquals;

import com.np6.npush.Config;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

public class ConfigTest {

    private static com.np6.npush.Config configTest;
    @BeforeClass
    public static void init() {
        configTest = new Config(UUID.fromString("b1c1b2c2-1111-2222-3333-b1c1b2c2b3c3"),
                "MCOM032","defaultChannel");
    }

    @Test
    public void testGetIdentity() {
        assertEquals("GetIdentity() failed", configTest.getIdentity(), "MCOM032");
    }

    @Test
    public void testGetDefaultChannel(){
        assertEquals("GetDefaultChannel() failed", configTest.getDefaultChannel(), "defaultChannel");
    }

    @Test
    public void testGetApplication(){
        assertEquals("GetApplication() failed", configTest.getApplication(),
                UUID.fromString("b1c1b2c2-1111-2222-3333-b1c1b2c2b3c3"));
    }
}
