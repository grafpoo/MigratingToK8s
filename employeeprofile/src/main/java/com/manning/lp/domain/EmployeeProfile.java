package com.manning.lp.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Entity
public class EmployeeProfile {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long employeeId;
    private String fullName;
    private String orgCode;
    private String email;
    private String phone;
}
