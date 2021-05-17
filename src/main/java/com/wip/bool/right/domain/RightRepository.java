package com.wip.bool.right.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RightRepository extends JpaRepository<Right, Long> {
}
