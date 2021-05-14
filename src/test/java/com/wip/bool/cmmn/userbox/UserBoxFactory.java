package com.wip.bool.cmmn.userbox;

import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.user.domain.User;
import com.wip.bool.userbox.domain.UserBox;

public class UserBoxFactory {

    public static UserBox getUserBox(User user) {

        String userBoxName = "사용자박스_1";
        ShareType shareType = ShareType.PRIVATE;
        UserBox userBox = UserBox.createUserBox(user, userBoxName, shareType);
        return userBox;
    }

    public static UserBox getUserBox(User user, String userBoxName) {

        ShareType shareType = ShareType.PRIVATE;
        UserBox userBox = UserBox.createUserBox(user, userBoxName, shareType);
        return userBox;
    }
}
