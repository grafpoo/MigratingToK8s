package liveproject.m2k8s.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import liveproject.m2k8s.ProfileThing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProfileThingRepository implements ProfileThingRepository {

  private JdbcOperations jdbc;

  @Autowired
  public JdbcProfileThingRepository(JdbcOperations jdbc) {
    this.jdbc = jdbc;
  }

  public List<ProfileThing> findRecentProfileThings() {
    return jdbc.query(
        "select id, message, created_at, latitude, longitude" +
        " from ProfileThing" +
        " order by created_at desc limit 20",
        new ProfileThingRowMapper());
  }

  public List<ProfileThing> findProfileThings(long max, int count) {
    return jdbc.query(
        "select id, message, created_at, latitude, longitude" +
        " from ProfileThing" +
        " where id < ?" +
        " order by created_at desc limit 20",
        new ProfileThingRowMapper(), max);
  }

  public ProfileThing findOne(long id) {
    return jdbc.queryForObject(
        "select id, message, created_at, latitude, longitude" +
        " from ProfileThing" +
        " where id = ?",
        new ProfileThingRowMapper(), id);
  }

  public void save(ProfileThing profileThing) {
    jdbc.update(
        "insert into ProfileThing (message, created_at, latitude, longitude)" +
        " values (?, ?, ?, ?)",
        profileThing.getMessage(),
        profileThing.getTime(),
        profileThing.getLatitude(),
        profileThing.getLongitude());
  }

  private static class ProfileThingRowMapper implements RowMapper<ProfileThing> {
    public ProfileThing mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new ProfileThing(
          rs.getLong("id"),
          rs.getString("message"), 
          rs.getDate("created_at"), 
          rs.getDouble("longitude"), 
          rs.getDouble("latitude"));
    }
  }
  
}
