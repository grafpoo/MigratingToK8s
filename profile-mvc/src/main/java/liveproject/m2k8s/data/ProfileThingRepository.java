package liveproject.m2k8s.data;

import java.util.List;

import liveproject.m2k8s.ProfileThing;

public interface ProfileThingRepository {

  List<ProfileThing> findRecentProfileThings();

  List<ProfileThing> findProfileThings(long max, int count);

  ProfileThing findOne(long id);

  void save(ProfileThing profileThing);

}
