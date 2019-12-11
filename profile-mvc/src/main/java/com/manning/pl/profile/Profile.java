package com.manning.pl.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Data
//@Table(name = "account")
public class Profile implements java.io.Serializable {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String email;
	private String fullName;
	private String orgCode;
	@JsonIgnore
	private String password;
	private String role = "ROLE_USER";
	private Instant created;

    protected Profile() {

	}
	
	public Profile(String email, String password, String role) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.created = Instant.now();
	}
}
