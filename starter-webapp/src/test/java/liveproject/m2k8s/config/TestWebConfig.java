package liveproject.m2k8s.config;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.service.ProfileService;
import liveproject.m2k8s.web.ProfileController;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.when;

@Configuration
public class TestWebConfig {

    @Bean
    public ProfileService profileService() {
        ProfileService mock = Mockito.mock(ProfileService.class);
        Profile zasu = Profile.builder()
                .firstName("Zasu")
                .lastName("Pitts")
                .id(1L)
                .email("zasupitts@hollywood.com")
                .username("zasupitts")
                .password("changeme")
                .build();
        when(mock.getProfile("zasupitts")).thenReturn(zasu);
        return mock;
    }

    @Bean
    public ProfileController profileController() {
        return new ProfileController(profileService());
    }
}
