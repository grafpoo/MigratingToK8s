package liveproject.m2k8s.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import liveproject.m2k8s.web.ProfileController;

public class ProfileControllerTest {

  @Test
  public void shouldShowRegistration() throws Exception {
    ProfileRepository mockRepository = mock(ProfileRepository.class);
    ProfileController controller = new ProfileController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller).build();
    mockMvc.perform(get("/profile/register"))
           .andExpect(view().name("registerForm"));
  }
  
  @Test
  public void shouldProcessRegistration() throws Exception {
    ProfileRepository mockRepository = mock(ProfileRepository.class);
    Profile unsaved = new Profile("jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
    Profile saved = new Profile(24L, "jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
    when(mockRepository.save(unsaved)).thenReturn(saved);
    
    ProfileController controller = new ProfileController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller).build();

    mockMvc.perform(post("/profile/register")
           .param("firstName", "Jack")
           .param("lastName", "Bauer")
           .param("username", "jbauer")
           .param("password", "24hours")
           .param("email", "jbauer@ctu.gov"))
           .andExpect(redirectedUrl("/profile/jbauer"));
    
    verify(mockRepository, atLeastOnce()).save(unsaved);
  }

  @Test
  public void shouldFailValidationWithNoData() throws Exception {
    ProfileRepository mockRepository = mock(ProfileRepository.class);
    ProfileController controller = new ProfileController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller).build();
    
    mockMvc.perform(post("/profile/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("registerForm"))
        .andExpect(model().errorCount(5))
        .andExpect(model().attributeHasFieldErrors(
            "profile", "firstName", "lastName", "username", "password", "email"));
  }

}
