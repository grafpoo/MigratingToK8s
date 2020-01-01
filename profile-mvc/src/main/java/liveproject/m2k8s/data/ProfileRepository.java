package liveproject.m2k8s.data;

import liveproject.m2k8s.Profile;

public interface ProfileRepository {

  Profile save(Profile profile);
  
  Profile findByUsername(String username);

}
