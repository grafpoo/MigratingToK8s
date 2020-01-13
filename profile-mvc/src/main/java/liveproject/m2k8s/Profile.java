package liveproject.m2k8s;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;

@Data
@Entity(name = "emp_profile")
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

  @Id
  @GeneratedValue
  private Long id;
  
  @NotNull
  @Size(min=5, max=16, message="{username.size}")
  private String username;

  @NotNull
  @Size(min=5, max=25, message="{password.size}")
  private String password;
  
  @NotNull
  @Size(min=2, max=30, message="{firstName.size}")
  private String firstName;

  @NotNull
  @Size(min=2, max=30, message="{lastName.size}")
  private String lastName;
  
  @NotNull
  @Email
  private String email;

  private String imageFileName;
  private String imageFileContentType;

  public Profile(String username, String password, String firstName, String lastName, String email) {
    this(null, username, password, firstName, lastName, email);
  }

  public Profile(Long id, String username, String password, String firstName, String lastName, String email) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

}
