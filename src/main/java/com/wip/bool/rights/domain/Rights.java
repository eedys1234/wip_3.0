package com.wip.bool.rights.domain;

import com.wip.bool.cmmn.auth.Authority;
import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.cmmn.auth.Target;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

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
    @Column(name = "target", length = 30, nullable = false)
    private Target target;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", length = 30, nullable = false)
    private Authority authority;

    @Column(name = "authority_id", nullable = false)
    private Long authorityId;

    @Column(name = "right_type")
    private Long rightType;

    public enum RightType {
        READ(1L),
        WRITE(2L);

        private Long value;

        RightType(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return this.value;
        }
    }

    private Rights(Target target, Long targetId, Authority authority, Long authorityId) {
        this.target = target;
        this.targetId = targetId;
        this.authority = authority;
        this.authorityId = authorityId;
    }

    public static Rights of(Target target, Long targetId, Authority authority, Long authorityId) {
        Rights rights = new Rights(target, targetId, authority, authorityId);
        String rightType = "read";
        rights.updateRightType(rightType);
        return rights;
    }

    public static Rights of(Target target, Long targetId, Authority authority, Long authorityId, String rightType) {
        Rights rights = new Rights(target, targetId, authority, authorityId);
        rights.updateRightType(rightType);
        return rights;
    }

    public void updateRightType(String rightType) {
        this.rightType = Arrays.stream(rightType.split(","))
                                .mapToLong(right -> RightType.valueOf(right.toUpperCase()).getValue())
                                .sum();
    }

    public void updateRightType(Long rightType) {
        this.rightType = rightType;
    }

}
