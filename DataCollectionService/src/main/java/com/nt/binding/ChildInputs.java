package com.nt.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ChildInputs {
	private Integer childId;
	private Integer caseNo;
	private LocalDate childDb;
	private Long childSSN;
}
