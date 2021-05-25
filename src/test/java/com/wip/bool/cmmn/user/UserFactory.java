package com.wip.bool.cmmn.user;

import com.wip.bool.dept.domain.Dept;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserFactory {

    public static String[] emails = {
        "test_1@gmail.com",
        "test_2@gmail.com",
        "test_3@gmail.com",
        "test_4@gmail.com",
        "test_5@gmail.com",
        "test_6@gmail.com",
        "test_7@gmail.com",
        "test_8@gmail.com",
        "test_9@gmail.com",
        "test_10@gmail.com",
    };

    public static List<User> getNormalUsers() {
        return Arrays.stream(emails)
                .map(email -> getNormalUser(email))
                .collect(Collectors.toList());
    }

    public static List<User> getNormalUsersWithId() {
        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(emails)
                .map(email -> getNormalUser(email, index.incrementAndGet()))
                .collect(Collectors.toList());
    }

    public static User getRequestUser() {
        String email = "test@gmail.com";
        String password = "test1234";
        String profiles = "";
        UserType userType = UserType.WIP;
        Role role = Role.ROLE_REQUEST;
        User user = User.createUser(email, password, profiles, userType, role);
        return user;
    }

    public static User getRequestUser(long id) {
        User user = getRequestUser();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static User getNormalUser(String email) {
        String password = "test1234";
        String profiles = "";
        UserType userType = UserType.WIP;
        Role role = Role.ROLE_NORMAL;
        User user = User.createUser(email, password, profiles, userType, role);
        return user;
    }

    public static User getNormalUser(String email, long id) {
        User user = getNormalUser(email);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static User getNormalUser() {
        String email = "test@gmail.com";
        User user = getNormalUser(email);
        return user;
    }

    public static User getNormalUser(long id) {
        User user = getNormalUser();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static User getNormalUser(Dept dept) {
        User user = getNormalUser();
        user.updateDept(dept);
        return user;
    }

    public static User getNormalUser(Dept dept, long id) {
        User user = getNormalUser(dept);
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
