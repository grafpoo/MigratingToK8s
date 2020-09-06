package liveproject.m2k8s.service;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceTest {
    private ProfileRepository profileRepository = Mockito.mock(ProfileRepository.class);
    private ProfileService profileService;

    private Profile zasu;

    @Before
    public void setup() {
        profileService = new ProfileService(profileRepository);
        zasu = Profile.builder()
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
        assertEquals(zasu, zasupitts);
    }
}
