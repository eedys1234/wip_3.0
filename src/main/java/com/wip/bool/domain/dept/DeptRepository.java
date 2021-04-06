package com.wip.bool.domain.dept;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public interface DeptRepository extends JpaRepository<Dept, Long> {


}
