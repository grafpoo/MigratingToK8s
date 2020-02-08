package liveproject.m2k8s.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileControllerTest {
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProfileRepository profileRepository;

  private Profile unsaved;
  private Profile saved;

  @Before
  public void setup() throws Exception {
    unsaved = new Profile("cheese", "stilton", "Chuck", "Cheese", "cecheese@example.com");
    saved = new Profile(24L, "cheese", "stilton", "Chuck", "Cheese", "cecheese@example.com");
  }

  @Test
  public void getProfile_whenAppContainsProfile_returnProfileSuccessfully() throws Exception {

    when(profileRepository.findByUsername(any())).thenReturn(saved);

    String savedJson = objectMapper.writeValueAsString(saved);

    mockMvc.perform(get("/profile/cheese"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(savedJson, true));
  }

  @Test
  @Ignore
  public void shouldProcessRegistration() throws Exception {
    ProfileRepository mockRepository = mock(ProfileRepository.class);
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
}
