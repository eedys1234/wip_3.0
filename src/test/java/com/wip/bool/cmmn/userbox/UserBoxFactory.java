package com.wip.bool.cmmn.userbox;

import com.wip.bool.user.domain.User;
import com.wip.bool.userbox.domain.UserBox;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserBoxFactory {

    public static String[] userBoxNames = {
            "사용자박스_1",
            "사용자박스_2",
            "사용자박스_3",
            "사용자박스_4",
            "사용자박스_5",
            "사용자박스_6",
            "사용자박스_7",
            "사용자박스_8",
            "사용자박스_9",
            "사용자박스_10"};

    public static List<String> getUserBoxNames() {
        return Arrays.stream(userBoxNames)
                .collect(Collectors.toList());
    }

    public static List<UserBox> getUserBoxes(User user) {

        return Arrays.stream(userBoxNames)
                .map(userBoxName -> UserBoxFactory.getUserBox(user, userBoxName))
                .collect(Collectors.toList());
    }

    public static List<UserBox> getUserBoxesWithId(User user) {
        AtomicInteger index = new AtomicInteger(1);

        return Arrays.stream(userBoxNames)
                .map(userBoxName -> {
                    UserBox userBox = UserBoxFactory.getUserBox(user, userBoxName, index.intValue());
                    index.incrementAndGet();
                    return userBox;
                })
                .collect(Collectors.toList());
    }

    public static UserBox getUserBox(User user) {
        String userBoxName = "사용자박스_1";
        UserBox userBox = UserBox.createUserBox(user, userBoxName);
        return userBox;
    }

    public static UserBox getUserBox(User user, long id) {
        UserBox userBox = getUserBox(user);
        ReflectionTestUtils.setField(userBox, "id", id);
        return userBox;
    }

    public static UserBox getUserBox(User user, String userBoxName) {
        UserBox userBox = UserBox.createUserBox(user, userBoxName);
        return userBox;
    }

    public static UserBox getUserBox(User user, String userBoxName, long id) {
        UserBox userBox = getUserBox(user, userBoxName);
        ReflectionTestUtils.setField(userBox, "id", id);
        return userBox;
    }
}
