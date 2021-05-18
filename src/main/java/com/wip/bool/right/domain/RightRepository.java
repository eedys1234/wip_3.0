package com.wip.bool.right.domain;

import com.wip.bool.cmmn.auth.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RightRepository extends JpaRepository<Right, Long> {

    Optional<Right> findByTargetIdAndTarget(Target target, Long targetId);
}
