package com.portal.counsellor.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "enquiry")
public class Enquiry 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "e_id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "ph")
	private Long phoneNo;
	
	@Column(name = "classmode")
	private String classMode;
	
	@Column(name = "course")
	private String course;
	
	@Column(name = "status")
	private String status;


	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "c_id")
	private Counsellor counsellor;

	// @CreationTimestamp
	// private LocalDate createdDate;

	// @UpdateTimestamp
	// private LocalDate updatedDate;

}
