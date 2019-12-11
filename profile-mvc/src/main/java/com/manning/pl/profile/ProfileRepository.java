package com.manning.pl.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Profile findOneByEmail(String email);

	@Query("select count(a) > 0 from Profile a where a.email = :email")
	boolean exists(@Param("email") String email);
}