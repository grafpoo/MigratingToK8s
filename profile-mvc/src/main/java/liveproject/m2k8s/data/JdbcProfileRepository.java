package liveproject.m2k8s.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import liveproject.m2k8s.Profile;

@Repository
public class JdbcProfileRepository implements ProfileRepository {
  
  private JdbcOperations jdbc;

  @Autowired
  public JdbcProfileRepository(JdbcOperations jdbc) {
    this.jdbc = jdbc;
  }

  public Profile save(Profile profile) {
    jdbc.update(
        "insert into Profile (username, password, first_name, last_name, email)" +
        " values (?, ?, ?, ?, ?)",
        profile.getUsername(),
        profile.getPassword(),
        profile.getFirstName(),
        profile.getLastName(),
        profile.getEmail());
    return profile; // TODO: Determine value for id
  }

  public Profile findByUsername(String username) {
    return jdbc.queryForObject(
        "select id, username, null, first_name, last_name, email from Profile where username=?",
        new ProfileRowMapper(),
        username);
  }
  
  private static class ProfileRowMapper implements RowMapper<Profile> {
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Profile(
          rs.getLong("id"),
          rs.getString("username"),
          null,
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getString("email"));
    }
  }

}
