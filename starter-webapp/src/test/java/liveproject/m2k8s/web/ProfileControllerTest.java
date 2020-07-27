package liveproject.m2k8s.web;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.config.TestWebConfig;
import liveproject.m2k8s.config.WebInitializer;
import liveproject.m2k8s.service.ProfileService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Ignore
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestWebConfig.class})
//@WebAppConfiguration
public class ProfileControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private ProfileController controller;

//  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

//  @Test
  public void find_shouldAddEntryToModelAndRenderProfileView() throws Exception {

    mockMvc.perform(get("/zasupitts"))
            .andExpect(status().isOk())
            .andExpect(view().name("profile"))
            .andExpect(forwardedUrl("/WEB-INF/views/profile.html"))
            .andExpect(model().attribute("profile", hasProperty("id", is(1L))));

    verify(controller, times(1)).showProfile(anyString(), any(Model.class));
    verifyNoMoreInteractions(controller);
  }
}
