package com.wip.bool.domain.position;

import com.wip.bool.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Position {

    @Id
    @GeneratedValue
    @Column(name = "position_id")
    private Long id;

    @Column(name = "position_name")
    private String positionName;

    @OneToMany(mappedBy = "position")
    private List<User> users = new ArrayList<>();
}
