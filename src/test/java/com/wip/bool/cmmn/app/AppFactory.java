package com.wip.bool.cmmn.app;

import com.wip.bool.app.domain.AppVersion;
import org.springframework.test.util.ReflectionTestUtils;

public class AppFactory {

    public static AppVersion getAppVersion() {

        String name = "ILECTRON";
        String version = "1.0.0.0";
        AppVersion appVersion = AppVersion.createAppVersion(name, version);
        return appVersion;
    }

    public static AppVersion getAppVersion(long id) {

        String name = "ILECTRON";
        String version = "1.0.0.0";
        AppVersion appVersion = AppVersion.createAppVersion(name, version);
        ReflectionTestUtils.setField(appVersion, "id", id);
        return appVersion;
    }
}
