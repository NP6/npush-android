package com.np6.npush;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static okhttp3.mock.HttpCode.HTTP_400_BAD_REQUEST;
import static okhttp3.mock.HttpCode.HTTP_401_UNAUTHORIZED;
import static okhttp3.mock.MediaTypes.MEDIATYPE_JSON;

import com.np6.npush.internal.api.SubscriptionApi;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.concurrency.Concurrent;
import com.np6.npush.internal.core.network.driver.Driver;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.common.Completion;
import com.np6.npush.internal.models.common.Result;
import com.np6.npush.internal.models.gateway.Firebase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.mock.MockInterceptor;
import okhttp3.mock.Rule;

@Config(sdk = {30})
@RunWith(RobolectricTestRunner.class)
public class SubscriptionApiTest {


    @Before
    public void setup() {

    }

    public void putSubscriptionWithoutHttpError() throws InterruptedException {
        MockInterceptor interceptor = new MockInterceptor();

        interceptor.addRule(new Rule.Builder()
                .get()
                .respond(HTTP_400_BAD_REQUEST));
                //.url(Constants.WebServices.Subscription_Endpoint + "MCOM" + "/" + "032" + "/subscriptions")

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        SubscriptionApi api = new SubscriptionApi("MCOM032", new Driver(client));

        Subscription subscription = new Subscription()
                .setId(UUID.randomUUID())
                .setGateway(new Firebase("testtoken"));

        Thread.sleep(7000);


    }

}
