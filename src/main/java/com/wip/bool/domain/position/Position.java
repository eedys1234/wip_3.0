package com.wip.bool.domain.position;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

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

//    @OneToMany(mappedBy = "position")
//    private List<User> users = new ArrayList<>();

    @Builder
    public Position(String positionName) {
        this.positionName = positionName;
    }

    public void update(String positionName) {

        if(!Objects.isNull(positionName)) {
            this.positionName = positionName;
        }
    }
}
