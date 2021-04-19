package com.wip.bool.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_config")
public class UserConfig {

    @Id
    @GeneratedValue
    @Column(name = "config_id")
    private Long id;

    @Column(name = "font_size")
    private String fontSize;

    @Column(name = "view_type")
    private String viewType;

    @Column(name = "recv_alaram")
    private String recvAlaram;

    public static UserConfig createUserConfig() {
        UserConfig userConfig = new UserConfig();
        userConfig.init();
        return userConfig;
    }

    public void update(String fontSize, String viewType, String recvAlaram) {

        if(!Objects.isNull(fontSize)) {
            this.fontSize = fontSize;
        }

        if(!Objects.isNull(viewType)) {
            this.viewType = viewType;
        }

        if(!Objects.isNull(recvAlaram)) {
            this.recvAlaram = recvAlaram;
        }
    }

    private void init() {
        this.fontSize = "15";
        this.viewType = "0";
        this.recvAlaram = "1";
    }
}
