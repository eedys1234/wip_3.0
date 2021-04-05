package com.wip.bool.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
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
}
