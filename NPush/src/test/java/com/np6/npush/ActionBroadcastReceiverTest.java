package com.np6.npush;

import static org.junit.Assert.assertEquals;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.system.ActionBroadcastReceiver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 30)
public class ActionBroadcastReceiverTest {

    @Test
    public void testParsingDismissActionIntent() throws Exception {

        Intent intentMock = Mockito.mock(Intent.class);

        Mockito.when(intentMock.getAction()).thenReturn(Constants.Intent.INTENT_DISMISS_INTENT);

        ArgumentCaptor<String> extraArgument = ArgumentCaptor.forClass(String.class);

        ActionBroadcastReceiver.parseAction(intentMock);

        Mockito.verify(intentMock, Mockito.times(1)).getStringExtra(extraArgument.capture());
        assertEquals(Constants.Extra.BUNDLE_KEY_DISMISS_KEY, extraArgument.getValue());
    }

    @Test
    public void testParsingRedirectionActionIntent() throws Exception {

        Intent intentMock = Mockito.mock(Intent.class);

        Mockito.when(intentMock.getAction()).thenReturn(Constants.Intent.INTENT_REDIRECTION_INTENT);

        ArgumentCaptor<String> extraArgument = ArgumentCaptor.forClass(String.class);

        ActionBroadcastReceiver.parseAction(intentMock);

        Mockito.verify(intentMock, Mockito.times(1)).getStringExtra(extraArgument.capture());
        assertEquals(Constants.Extra.BUNDLE_KEY_REDIRECTION_KEY, extraArgument.getValue());
    }

    @Test(expected = Exception.class)
    public void testParsingInvalidActionIntent() throws Exception {

        Intent intentMock = Mockito.mock(Intent.class);

        Mockito.when(intentMock.getAction()).thenReturn("fake_intent_key");

        ActionBroadcastReceiver.parseAction(intentMock);
    }


    @Test
    public void testParsingActionIntent() throws Exception {

        Intent intentMock = Mockito.mock(Intent.class);

        Mockito.when(intentMock.getAction()).thenReturn(Constants.Intent.INTENT_DISMISS_INTENT);
        Mockito.when(intentMock.getStringExtra(ArgumentMatchers.anyString())).thenReturn("radical_mock");

        ArgumentCaptor<String> extraArgument = ArgumentCaptor.forClass(String.class);

        ActionBroadcastReceiver.parse(intentMock);

        Mockito.verify(intentMock, Mockito.times(2)).getStringExtra(extraArgument.capture());
        assertEquals(Constants.Extra.BUNDLE_KEY_DISMISS_KEY, extraArgument.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingActionIntentWithoutRadical() throws Exception {

        Intent intentMock = Mockito.mock(Intent.class);

        Mockito.when(intentMock.getAction()).thenReturn(Constants.Intent.INTENT_DISMISS_INTENT);
        Mockito.when(intentMock.getStringExtra(ArgumentMatchers.anyString())).thenReturn(null);

        ActionBroadcastReceiver.parse(intentMock);

    }


}
