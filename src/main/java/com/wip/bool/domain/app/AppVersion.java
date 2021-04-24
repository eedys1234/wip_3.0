package com.wip.bool.domain.app;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "app_version")
public class AppVersion {

    @Id
    @GeneratedValue
    @Column(name = "app_version_id")
    private Long id;

    private String name;

    private String version;

    public static AppVersion createAppVersion(String name, String version) {
        AppVersion app = new AppVersion();
        app.updateAppName(name);
        app.updateAppVersion(version);
        return app;
    }

    public void updateAppName(String name) {
        this.name = name;
    }

    public void updateAppVersion(String version) {
        this.version = version;
    }
}
