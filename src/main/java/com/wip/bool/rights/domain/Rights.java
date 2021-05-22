package com.wip.bool.rights.domain;

import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.cmmn.auth.Target;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "rights")
public class Rights extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "right_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target")
    private Target target;

    @Column(name = "target_id")
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @Column(name = "authority_id")
    private Long authorityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "right_type")
    private RightType rightType;

    public enum RightType {
        READ, WRITE
    }

    private Rights(Target target, Long targetId, Authority authority, Long authorityId) {
        this.target = target;
        this.targetId = targetId;
        this.authority = authority;
        this.authorityId = authorityId;
    }

    public static Rights of(Target target, Long targetId, Authority authority, Long authorityId) {
        Rights right = new Rights(target, targetId, authority, authorityId);
        return right;
    }

    public void updateRightType(RightType rightType) {
        this.rightType = rightType;
    }

}
