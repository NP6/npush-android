package com.np6.npush;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.app.NotificationManager;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.np6.npush.internal.core.persistence.SharedPreferenceStorage;
import com.np6.npush.internal.repository.TokenRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(sdk = {30})
@RunWith(AndroidJUnit4.class)
public class TokenRepositoryTest {

    Context context = ApplicationProvider.getApplicationContext();

    String token = "15fz15e19FE18f1zeR7HTH9U49jy89er4r784ze8g74t784i7iu7yt4rez8a7ergt8h7y487zf4";

    @Test
    public void testAddValidTokenToRepository() {
        SharedPreferenceStorage sharedPreferenceStorageMock = Mockito.mock(SharedPreferenceStorage.class);
        TokenRepository repository = new TokenRepository(sharedPreferenceStorageMock);
        repository.Add(token);

        ArgumentCaptor<String> valueArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyArgument = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sharedPreferenceStorageMock, Mockito.times(1)).put(keyArgument.capture(), valueArgument.capture());
        assertEquals(token, valueArgument.getValue());
        assertEquals(TokenRepository.TOKEN_KEY, keyArgument.getValue());
    }

    @Test
    public void testGetValueFromRepository() {
        SharedPreferenceStorage sharedPreferenceStorageMock = Mockito.mock(SharedPreferenceStorage.class);
        Mockito.when(sharedPreferenceStorageMock.fetch(TokenRepository.TOKEN_KEY)).thenReturn(SharedPreferenceStorage.DEFAULT);

        TokenRepository repository = new TokenRepository(sharedPreferenceStorageMock);
        repository.Get();

        ArgumentCaptor<String> valueArgument = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sharedPreferenceStorageMock, Mockito.times(1)).fetch(valueArgument.capture());
    }

    @Test
    public void CheckIfValueExistFromRepository() {
        TokenRepository repository = TokenRepository.create(context);
        repository.Add(token);
        assertTrue(repository.Exist());
    }
}