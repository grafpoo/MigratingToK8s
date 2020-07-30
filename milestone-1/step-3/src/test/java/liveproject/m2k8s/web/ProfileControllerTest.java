package liveproject.m2k8s.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import liveproject.m2k8s.Profile;
import liveproject.m2k8s.service.ProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
  private ProfileService profileService;

  private Profile unsaved;
  private Profile saved;

  @Before
  public void setup() throws Exception {
    unsaved = new Profile("cheese", "stilton", "Chuck", "Cheese", "cecheese@example.com");
    saved = new Profile(24L, "cheese", "stilton", "Chuck", "Cheese", "cecheese@example.com");
  }

  @Test
  public void getProfile_whenAppContainsProfile_returnProfileSuccessfully() throws Exception {

    when(profileService.getProfile(any())).thenReturn(saved);

    String savedJson = objectMapper.writeValueAsString(saved);

    mockMvc.perform(get("/profile/cheese"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(savedJson, true));
  }

  @Test
  public void shouldProcessRegistration() throws Exception {

    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(post("/profile")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isCreated());

    verify(profileService, atLeastOnce()).save(unsaved);
  }

  @Test
  public void shouldNotProcessNullUsernameRegistration() throws Exception {
    // null out required atttribute
    unsaved.setUsername(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(post("/profile")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotProcessNullPasswordRegistration() throws Exception {
    // null out required atttribute
    unsaved.setPassword(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(post("/profile")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotProcessNullFirstNmeRegistration() throws Exception {
    // null out required atttribute
    unsaved.setFirstName(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(post("/profile")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotProcessNullLastNameRegistration() throws Exception {
    // null out required atttribute
    unsaved.setLastName(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(post("/profile")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotProcessNullEmailRegistration() throws Exception {
    // null out required atttribute
    unsaved.setEmail(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(post("/profile")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }


  @Test
  public void shouldNotUpdateNullUsernameRegistration() throws Exception {
    // null out required atttribute
    unsaved.setUsername(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(put("/profile/cheese")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotUpdateNullPasswordRegistration() throws Exception {
    // null out required atttribute
    unsaved.setPassword(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(put("/profile/cheese")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotUpdateNullFirstNmeRegistration() throws Exception {
    // null out required atttribute
    unsaved.setFirstName(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(put("/profile/cheese")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotUpdateNullLastNameRegistration() throws Exception {
    // null out required atttribute
    unsaved.setLastName(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(put("/profile/cheese")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldNotUpdateNullEmailRegistration() throws Exception {
    // null out required atttribute
    unsaved.setEmail(null);
    String profileJson = objectMapper.writeValueAsString(unsaved);

    mockMvc.perform(put("/profile/cheese")
            .content(profileJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

}
