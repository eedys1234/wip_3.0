package com.wip.bool.cmmn.user;

import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserType;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFactory {

    public static User getNormalUser() {
        String email = "test@gmail.com";
        String password = "test1234";
        String profiles = "";
        UserType userType = UserType.WIP;
        Role role = Role.ROLE_NORMAL;
        User user = User.createUser(email, password, profiles, userType, role);
        return user;
    }

    public static User getNormalUser(long id) {
        User user = getNormalUser();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static User getAdminUser() {
        String email = "test@gmail.com";
        String password = "test1234";
        String profiles = "";
        UserType userType = UserType.WIP;
        Role role = Role.ROLE_ADMIN;
        User user = User.createUser(email, password, profiles, userType, role);
        return user;
    }

    public static User getAdminUser(long id) {
        User user = getAdminUser();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
