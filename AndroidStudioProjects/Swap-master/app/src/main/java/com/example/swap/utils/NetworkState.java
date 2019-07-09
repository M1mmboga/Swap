package com.example.swap.utils;

/*
Inspired by https://github.com/SaurabhSandav/PagingDemo
* */

import androidx.annotation.Nullable;

public class NetworkState {

    public enum Status {RUNNING, SUCCESS, FAILED}

    private Status status;
    private String message;
    private Retryable retryable;

    public static NetworkState LOADING = new NetworkState(Status.RUNNING);
    public static NetworkState LOADED = new NetworkState(Status.SUCCESS);

    private NetworkState(Status status, @Nullable String message, @Nullable Retryable retryable) {
        this.status = status;
        this.message = message;
        this.retryable = retryable;
    }

    private NetworkState(Status status, @Nullable String message) {
        this(status, message, null);
    }

    private NetworkState(Status status) {
        this(status, null);
    }

    public static NetworkState error(String message) {
        return new NetworkState(Status.FAILED, message);
    }

    public static NetworkState error(String message, Retryable retryable) {
        return new NetworkState(Status.FAILED, message, retryable);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Retryable getRetryable() {
        return retryable;
    }
}
