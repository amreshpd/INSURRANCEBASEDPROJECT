package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.PlanCategoryEntity;

public interface IPlanCategoryRepository extends JpaRepository<PlanCategoryEntity, Integer>{

}
