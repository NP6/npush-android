package com.np6.npush;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.np6.npush.internal.repository.IdentifierRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

@Config(sdk = {30})
@RunWith(AndroidJUnit4.class)
public class IdentifierRepositoryTest {

    private static Context context;
    private static UUID identifier;

    @Before
    public void Init() {
        context = ApplicationProvider.getApplicationContext();
        identifier = UUID.fromString("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    public void AddValueToRepository() {
        IdentifierRepository repository = IdentifierRepository.create(context);
        repository.Add(identifier);
        assertEquals(identifier, repository.Get());
    }

    @Test
    public void DeleteValueFromRepository() {
        IdentifierRepository repository = IdentifierRepository.create(context);
        repository.Remove();
        assertNull(repository.Get());
    }

    @Test
    public void CheckIfValueExistFromRepository() {
        IdentifierRepository repository = IdentifierRepository.create(context);
        repository.Add(identifier);
        assertTrue(repository.Exist());
    }
}