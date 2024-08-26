package com.nt.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "DC_PLAN_MASTER")
@Data
public class PlanRegisterEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer planId;
	@Column(length = 30)
	private String planName;
	
	private LocalDate startDate;
	private LocalDate endDate;	
	@Column(length = 100)
	private String description;
	@Column(length = 15)
	private String activeSw;
	@Column(length = 40)
	private String createdBy;
	@Column(length = 40)
	private String updatedBy;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime creationDate;
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updationDate;
}
