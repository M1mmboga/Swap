package com.example.swap.rest;

import org.junit.Test;
import static org.junit.Assert.*;

public class NetworkStateTest {

    @Test
    public void testNetworkStateLOADING_ReturnsStatusRUNNING() {
        NetworkState networkState = NetworkState.LOADING;
        assertEquals(NetworkState.Status.RUNNING, networkState.getStatus());
    }

    @Test
    public void testNetworkStateLOADED_ReturnsStatusSUCCESS() {
        NetworkState networkState = NetworkState.LOADED;
        assertEquals(NetworkState.Status.SUCCESS, networkState.getStatus());
    }

    @Test
    public void testNetworkStateErrorMethod_ReturnsStatusFAILED() {
        String err = "An error occured";
        NetworkState networkState = NetworkState.error(err);

        assertEquals(NetworkState.Status.FAILED, networkState.getStatus());
    }

    @Test
    public void testNetworkStateErrorMethod_ReturnsGivenErrorMessage() {
        String err = "An error occured";
        NetworkState networkState = NetworkState.error(err);

        assertEquals(err, networkState.getMessage());
    }
}
