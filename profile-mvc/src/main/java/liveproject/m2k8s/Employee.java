package liveproject.m2k8s;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue
    private Integer employeeId;

    private String firstName;
    private String lastName;
    private Integer age;
    private String education;
    private Double salary;
}