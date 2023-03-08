package com.np6.npush;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.np6.npush.internal.repository.TokenRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(sdk = {30})
@RunWith(RobolectricTestRunner.class)
public class TokenRepositoryTest {

    Context context = ApplicationProvider.getApplicationContext();

    String token = "15fz15e19FE18f1zeR7HTH9U49jy89er4r784ze8g74t784i7iu7yt4rez8a7ergt8h7y487zf4";

    @Test
    public void AddValueToRepository() {
        TokenRepository repository = TokenRepository.create(context);
        repository.Add(token);
        assertEquals(token, repository.Get());
    }

    @Test
    public void DeleteValueFromRepository() {
        TokenRepository repository = TokenRepository.create(context);
        repository.Remove();
        assertNull(repository.Get());
    }

    @Test
    public void CheckIfValueExistFromRepository() {
        TokenRepository repository = TokenRepository.create(context);
        repository.Add(token);
        assertTrue(repository.Exist());
    }
}