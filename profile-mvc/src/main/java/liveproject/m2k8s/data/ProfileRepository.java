package liveproject.m2k8s.data;

import liveproject.m2k8s.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

  Profile save(Profile profile);
  
  Profile findByUsername(String username);

}
