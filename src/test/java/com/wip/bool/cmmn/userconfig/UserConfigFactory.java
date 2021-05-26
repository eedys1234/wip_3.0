package com.wip.bool.cmmn.userconfig;

import com.wip.bool.user.domain.UserConfig;
import org.springframework.test.util.ReflectionTestUtils;

public class UserConfigFactory {

    public static UserConfig getUserConfig() {
        UserConfig userConfig = UserConfig.createUserConfig();
        return userConfig;
    }

    public static UserConfig getUserConfig(long id) {
        UserConfig userConfig = UserConfig.createUserConfig();
        ReflectionTestUtils.setField(userConfig, "id", id);
        return userConfig;
    }

}
