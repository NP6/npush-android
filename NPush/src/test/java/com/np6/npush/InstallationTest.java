package com.np6.npush;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.np6.npush.internal.Installation;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.core.persistence.SharedPreferenceStorage;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.contact.ContactHash;
import com.np6.npush.internal.models.contact.Linked;
import com.np6.npush.internal.repository.IdentifierRepository;
import com.np6.npush.internal.repository.TokenRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

@Config(sdk = {30})
@RunWith(RobolectricTestRunner.class)
public class InstallationTest {

    private static com.np6.npush.Config config;
    private static String token;
    private static Linked linked;
    private static Context context;
    private static Installation installation;
    private static Serializer serializer;

    @Before
    public void Init() {
        config = new com.np6.npush.Config(
                UUID.fromString("12345678-1234-1234-1234-123456789abc"),
                "ABCD012",
                "default channel test name"
        );
        token = "7iu7yt4rez8a7ergt8h7y487zf4";
        linked = new ContactHash("d4790c65207479aaf6d9869fa86dd3d3");
        context = ApplicationProvider.getApplicationContext();
        installation = Installation.initialize(context, config);
        serializer = new Serializer();

    }

    @Test(expected = IllegalArgumentException.class)
    public void ConfigIsNull() {
        Installation.initialize(context, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ContextIsNull() {
        Installation.initialize(null, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TokenIsNull() {
        installation.subscribe(new ContactHash("d4790c65207479aaf6d9869fa86dd3d3"));
    }

    @Test
    public void CheckSubscription() throws JsonProcessingException {

//        String expectedSubscription =
//                "{\"id\":\"12345678-1234-1234-1234-123456789abc\"," +
//                "\"application\":\"12345678-1234-1234-1234-123456789abc\"," +
//                "\"gateway\":{\"type\":\"firebase\",\"token\":\"7iu7yt4rez8a7ergt8h7y487zf4\"}," +
//                "\"linked\":{\"type\":\"hash\",\"value\":\"d4790c65207479aaf6d9869fa86dd3d3\"}," +
//                "\"protocol\":\"1.0.0\",\"culture\":\"en\"}";

        TokenRepository
                .Create(context)
                .Add(token);
        IdentifierRepository
                .Create(context)
                .Add(UUID.fromString("12345678-1234-1234-1234-123456789abc"));
        Subscription s = installation
                .subscribe(linked);

        assertEquals(token, s.getGateway());
        assertEquals(linked, s.getLinked());
    }


}
