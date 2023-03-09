package com.np6.npush;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static okhttp3.mock.HttpCode.HTTP_400_BAD_REQUEST;
import static okhttp3.mock.HttpCode.HTTP_401_UNAUTHORIZED;
import static okhttp3.mock.MediaTypes.MEDIATYPE_JSON;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.np6.npush.internal.api.SubscriptionApi;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.core.network.driver.Driver;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;
import com.np6.npush.internal.models.gateway.Firebase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.mock.HttpCode;
import okhttp3.mock.MockInterceptor;
import okhttp3.mock.Rule;

@Config(sdk = {30})
@RunWith(AndroidJUnit4.class)
public class SubscriptionApiTest {


    @Before
    public void setup() {

    }

    @Test(expected = IllegalArgumentException.class)
    public void initializeSubscriptionApiWithoutIdentity() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        SubscriptionApi.create(null);
    }

    @Test
    public void putSubscriptionWithBadRequestHttpError() throws InterruptedException, JsonProcessingException, ExecutionException {
        MockInterceptor interceptor = new MockInterceptor();

        interceptor.addRule(new Rule.Builder()
                .put()
                .respond(HTTP_400_BAD_REQUEST));

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        SubscriptionApi api = new SubscriptionApi("MCOM032", new Driver(client));

        // Fake subscription
        Subscription subscription = new Subscription()
                .setId(UUID.randomUUID())
                .setGateway(new Firebase("testtoken"));

        Response response = api.put(subscription).get();

        assertFalse(response.isSuccessful());
        assertFalse(response.isRedirect());
        assertEquals(400, response.code());
        assertEquals(response.request().method(), "PUT");
        assertTrue(response.request().url().isHttps());

    }

    @Test
    public void putSubscription() throws InterruptedException, ExecutionException {
        MockInterceptor interceptor = new MockInterceptor();

        interceptor.addRule(new Rule.Builder()
                .put()
                .respond(HttpCode.HTTP_200_OK));

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        SubscriptionApi api = new SubscriptionApi("MCOM032", new Driver(client));

        // Fake subscription
        Subscription subscription = new Subscription()
                .setId(UUID.randomUUID())
                .setGateway(new Firebase("testtoken"));

        Response response = api.put(subscription).get();

        assertTrue(response.isSuccessful());
        assertFalse(response.isRedirect());
        assertEquals(200, response.code());
        assertEquals(response.request().method(), "PUT");
        assertTrue(response.request().url().isHttps());

    }

}
