package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.PlanRegisterEntity;

public interface IPlanRepository extends JpaRepository<PlanRegisterEntity, Integer> {

}
