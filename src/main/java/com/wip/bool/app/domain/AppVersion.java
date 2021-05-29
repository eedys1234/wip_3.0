package com.wip.bool.app.domain;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_version_id")
    private Long id;

    @Column(name = "app_name", nullable = false, length = 20, unique = true)
    private String name;

    @Column(name = "app_version", nullable = false, length = 7)
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
