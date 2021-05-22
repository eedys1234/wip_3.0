package com.wip.bool.rights.domain;

import com.wip.bool.cmmn.auth.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RightsRepository extends JpaRepository<Rights, Long> {

    Optional<Rights> findByTargetIdAndTarget(Target target, Long targetId);

}
