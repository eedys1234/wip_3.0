package com.wip.bool.domain.user;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
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

    protected UserConfig() {

    }

    public static UserConfig createUserConfig() {
        UserConfig userConfig = new UserConfig();
        userConfig.init();
        return userConfig;
    }

    private void init() {
        this.fontSize = "15";
        this.viewType = "0";
        this.recvAlaram = "1";
    }
}
