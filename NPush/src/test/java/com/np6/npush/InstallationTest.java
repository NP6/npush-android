package com.np6.npush;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.np6.npush.internal.Installation;
import com.np6.npush.internal.api.SubscriptionApi;
import com.np6.npush.internal.core.Constants;
import com.np6.npush.internal.core.Serializer;
import com.np6.npush.internal.core.persistence.SharedPreferenceStorage;
import com.np6.npush.internal.models.Subscription;
import com.np6.npush.internal.models.contact.ContactHash;
import com.np6.npush.internal.models.contact.Linked;
import com.np6.npush.internal.models.gateway.Firebase;
import com.np6.npush.internal.repository.IdentifierRepository;
import com.np6.npush.internal.repository.TokenRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import java9.util.concurrent.CompletableFuture;
import okhttp3.Response;

@Config(sdk = {30})
@RunWith(AndroidJUnit4.class)
public class InstallationTest {


    Context context = ApplicationProvider.getApplicationContext();

    com.np6.npush.Config config = new com.np6.npush.Config(
            UUID.fromString("12345678-1234-1234-1234-123456789abc"),
            "ABCD012",
            "default channel test name",
            false
    );

    @Test(expected = IllegalArgumentException.class)
    public void initializeWithNullConfig() {
        Installation.create(ApplicationProvider.getApplicationContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializeWithNullContext() {
        Installation.create(null, config);
    }

    @Test
    public void subscribeWithNullLinked() {

        CompletableFuture<Response> future =Installation.create(context, config).subscribe(null);
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    public void subscribeWithNullToken() {

        TokenRepository tokenRepositoryMock = Mockito.mock(TokenRepository.class);
        Mockito.when(tokenRepositoryMock.Get()).thenReturn(null);

        IdentifierRepository identifierRepositoryMock = Mockito.mock(IdentifierRepository.class);
        SubscriptionApi subscriptionApMock = Mockito.mock(SubscriptionApi.class);

        Installation installation = new Installation(config, tokenRepositoryMock, identifierRepositoryMock, subscriptionApMock);

        CompletableFuture<Response> future = installation.subscribe(new ContactHash("d4790c65207479aaf6d9869fa86dd3d3"));

        Mockito.verify(tokenRepositoryMock, Mockito.times(1)).Get();
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    public void subscribeWithValidToken() throws ExecutionException, InterruptedException {

        String validToken = "this_is_a_valid_mocked_token";

        TokenRepository tokenRepositoryMock = Mockito.mock(TokenRepository.class);

        Mockito.when(tokenRepositoryMock.Get()).thenReturn(validToken);

        IdentifierRepository identifierRepositoryMock = Mockito.mock(IdentifierRepository.class);

        SubscriptionApi subscriptionApMock = Mockito.mock(SubscriptionApi.class);

        Response responseMock = Mockito.mock(Response.class);

        Mockito.when(subscriptionApMock.put(ArgumentMatchers.any())).thenReturn(CompletableFuture.completedFuture(responseMock));

        Installation installation = new Installation(config, tokenRepositoryMock, identifierRepositoryMock, subscriptionApMock);

        Response response = installation.subscribe(new ContactHash("d4790c65207479aaf6d9869fa86dd3d3")).get();

        Mockito.verify(tokenRepositoryMock, Mockito.times(1)).Get();
    }

    @Test
    public void subscribeWithExistingIdentifier() throws ExecutionException, InterruptedException {

        String validToken = "this_is_a_valid_mocked_token";

        String validIdentifier = "8bb151a2-bc1e-4a86-bfb9-964115d4740d";

        TokenRepository tokenRepositoryMock = Mockito.mock(TokenRepository.class);

        Mockito.when(tokenRepositoryMock.Get()).thenReturn(validToken);

        IdentifierRepository identifierRepositoryMock = Mockito.mock(IdentifierRepository.class);

        Mockito.when(identifierRepositoryMock.Exist()).thenReturn(true);
        Mockito.when(identifierRepositoryMock.Get()).thenReturn(UUID.fromString(validIdentifier));

        SubscriptionApi subscriptionApMock = Mockito.mock(SubscriptionApi.class);

        Response responseMock = Mockito.mock(Response.class);

        Mockito.when(subscriptionApMock.put(ArgumentMatchers.any())).thenReturn(CompletableFuture.completedFuture(responseMock));

        Installation installation = new Installation(config, tokenRepositoryMock, identifierRepositoryMock, subscriptionApMock);

        installation.subscribe(new ContactHash("d4790c65207479aaf6d9869fa86dd3d3")).get();

        Mockito.verify(tokenRepositoryMock, Mockito.times(1)).Get();
        Mockito.verify(identifierRepositoryMock, Mockito.times(1)).Get();
    }


    @Test
    public void checkIfCreateInstallationIdentifier() {

        TokenRepository tokenRepositoryMock = Mockito.mock(TokenRepository.class);
        IdentifierRepository identifierRepositoryMock = Mockito.mock(IdentifierRepository.class);

        SubscriptionApi subscriptionApMock = Mockito.mock(SubscriptionApi.class);

        Installation installation = new Installation(config, tokenRepositoryMock, identifierRepositoryMock, subscriptionApMock);

        installation.getIdentifier();

        ArgumentCaptor<UUID> argument = ArgumentCaptor.forClass(UUID.class);

        Mockito.verify(identifierRepositoryMock, Mockito.times(1)).Exist();
        Mockito.verify(identifierRepositoryMock, Mockito.times(1)).Add(argument.capture());
        Mockito.verify(identifierRepositoryMock, Mockito.times(0)).Get();

    }


    @Test
    public void checkIfGetInstallationIdentifier() {

        TokenRepository tokenRepositoryMock = Mockito.mock(TokenRepository.class);
        IdentifierRepository identifierRepositoryMock = Mockito.mock(IdentifierRepository.class);

        Mockito.when(identifierRepositoryMock.Exist()).thenReturn(true);

        SubscriptionApi subscriptionApMock = Mockito.mock(SubscriptionApi.class);

        Installation installation = new Installation(config, tokenRepositoryMock, identifierRepositoryMock, subscriptionApMock);

        installation.getIdentifier();

        ArgumentCaptor<UUID> argument = ArgumentCaptor.forClass(UUID.class);

        Mockito.verify(identifierRepositoryMock, Mockito.times(1)).Exist();
        Mockito.verify(identifierRepositoryMock, Mockito.times(0)).Add(argument.capture());
        Mockito.verify(identifierRepositoryMock, Mockito.times(1)).Get();

    }

}
