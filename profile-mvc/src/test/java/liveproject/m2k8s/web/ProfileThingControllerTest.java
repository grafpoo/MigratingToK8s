package liveproject.m2k8s.web;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import liveproject.m2k8s.ProfileThing;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import liveproject.m2k8s.data.ProfileThingRepository;

public class ProfileThingControllerTest {

  @Test
  public void shouldShowRecentProfileThings() throws Exception {
    List<ProfileThing> expectedProfileThings = createProfileThingList(20);
    ProfileThingRepository mockRepository = mock(ProfileThingRepository.class);
    when(mockRepository.findProfileThings(Long.MAX_VALUE, 20))
        .thenReturn(expectedProfileThings);

    ProfileThingController controller = new ProfileThingController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller)
        .setSingleView(new InternalResourceView("/WEB-INF/views/profileThings.jsp"))
        .build();

    mockMvc.perform(get("/profileThings"))
       .andExpect(view().name("profileThings"))
       .andExpect(model().attributeExists("profileThingList"))
       .andExpect(model().attribute("profileThingList",
                  hasItems(expectedProfileThings.toArray())));
  }

  @Test
  public void shouldShowPagedProfileThings() throws Exception {
    List<ProfileThing> profileThingList = createProfileThingList(50);
    ProfileThingRepository mockRepository = mock(ProfileThingRepository.class);
    when(mockRepository.findProfileThings(238900, 50))
        .thenReturn(profileThingList);
    
    ProfileThingController controller = new ProfileThingController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller)
        .setSingleView(new InternalResourceView("/WEB-INF/views/profileThings.jsp"))
        .build();

    mockMvc.perform(get("/profileThings?max=238900&count=50"))
      .andExpect(view().name("profileThings"))
      .andExpect(model().attributeExists("profileThingList"))
      .andExpect(model().attribute("profileThingList",
                 hasItems(profileThingList.toArray())));
  }
  
  @Test
  public void testProfileThing() throws Exception {
    ProfileThing profileThing = new ProfileThing("Hello", new Date());
    ProfileThingRepository mockRepository = mock(ProfileThingRepository.class);
    when(mockRepository.findOne(12345)).thenReturn(profileThing);
    
    ProfileThingController controller = new ProfileThingController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller).build();

    mockMvc.perform(get("/profileThings/12345"))
      .andExpect(view().name("profileThing"))
      .andExpect(model().attributeExists("profileThing"))
      .andExpect(model().attribute("profileThing", profileThing));
  }

  @Test
  public void saveProfileThing() throws Exception {
    ProfileThingRepository mockRepository = mock(ProfileThingRepository.class);
    ProfileThingController controller = new ProfileThingController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller).build();

    mockMvc.perform(post("/profileThings")
           .param("message", "Hello World") // this works, but isn't really testing what really happens
           .param("longitude", "-81.5811668")
           .param("latitude", "28.4159649")
           )
           .andExpect(redirectedUrl("/profileThings"));
    
    verify(mockRepository, atLeastOnce()).save(new ProfileThing(null, "Hello World", new Date(), -81.5811668, 28.4159649));
  }
  
  private List<ProfileThing> createProfileThingList(int count) {
    List<ProfileThing> profileThings = new ArrayList<ProfileThing>();
    for (int i=0; i < count; i++) {
      profileThings.add(new ProfileThing("ProfileThing " + i, new Date()));
    }
    return profileThings;
  }
}
