package com.wip.bool.cmmn.util;

public final class WIPProperty {

    private WIPProperty() {
        throw new AssertionError();
    }

    public static final String LOCAL = "local";
    public static final String DEV = "dev";
    public static final String PROD = "prod";
    public static final String TEST = "test";
}
