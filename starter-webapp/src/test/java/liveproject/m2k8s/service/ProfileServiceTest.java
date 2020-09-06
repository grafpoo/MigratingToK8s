package liveproject.m2k8s.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceTest {
    private ProfileRepository profileRepository = Mockito.mock(ProfileRepository.class);
    private ProfileService profileService;

    @Before
    public void setup() {
        profileService = new ProfileService(profileRepository);
        Profile zasu = Profile.builder()
                .id(1L)
                .firstName("Zasu")
                .lastName("Pitts")
                .email("zasu@hollywood.com")
                .username("zasupitts")
                .password("changeme")
                .build();
        when(profileRepository.findByUsername("zasupitts")).thenReturn(zasu);
    }

    @Test
    public void test_getProfile() {
        Profile zasupitts = profileService.getProfile("zasupitts");
        assertEquals("Zasu", zasupitts.getFirstName());
    }
}
