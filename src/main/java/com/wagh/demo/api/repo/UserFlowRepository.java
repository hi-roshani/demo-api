package com.wagh.demo.api.repo;

import com.wagh.demo.api.model.UserFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFlowRepository extends JpaRepository<UserFlow, Long> {
}
