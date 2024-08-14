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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CITIZEN_APPLICATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DcPlanRegisterEntity {
	@Id
	@SequenceGenerator(name = "gen1_seq",sequenceName = "app_id_seq",initialValue = 1000,allocationSize = 1)
	@GeneratedValue(generator = "gen1_seq",strategy = GenerationType.SEQUENCE)
	private Integer appId;
	@Column(length = 30)
	private String fullName;
	@Column(length = 40)
	private String email;
	@Column(length = 1)
	private String gender;
	private Long phoneNo;
	private Long ssn;
	@Column(length = 50)
	private String stateName;
	private LocalDate dob;
	@Column(length = 40)
	private String createdBy;
	@Column(length = 40)
	private String updatedBy;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime creationDate;
	@UpdateTimestamp
	@Column(insertable  = false)
	private LocalDateTime updationDate;	
}
