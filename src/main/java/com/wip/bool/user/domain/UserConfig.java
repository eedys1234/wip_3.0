package com.wip.bool.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_config")
public class UserConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    private Long id;

    @Column(name = "font_size", length = 3, nullable = false)
    private String fontSize;

    @Column(name = "view_type", length = 1, nullable = false)
    private String viewType;

    @Column(name = "recv_alaram", length = 1, nullable = false)
    private String recvAlaram;

    public static UserConfig createUserConfig() {
        UserConfig userConfig = new UserConfig();
        userConfig.init();
        return userConfig;
    }

    public void update(String fontSize, String viewType, String recvAlaram) {

        if(!Objects.isNull(fontSize)) {
            updateFontSize(fontSize);
        }

        if(!Objects.isNull(viewType)) {
            updateViewType(viewType);
        }

        if(!Objects.isNull(recvAlaram)) {
            updateRecvAlaram(recvAlaram);
        }
    }

    public void updateFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public void updateViewType(String viewType) {
        this.viewType = viewType;
    }

    public void updateRecvAlaram(String recvAlaram) {
        this.recvAlaram = recvAlaram;
    }

    private void init() {
        this.fontSize = "15";
        this.viewType = "0";
        this.recvAlaram = "1";
    }
}
