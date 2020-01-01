package liveproject.m2k8s.web;

import java.util.Date;
import java.util.List;

import liveproject.m2k8s.ProfileThing;
import liveproject.m2k8s.data.ProfileThingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profileThings")
public class ProfileThingController {

  private static final String MAX_LONG_AS_STRING = "9223372036854775807";
  
  private ProfileThingRepository profileThingRepository;

  @Autowired
  public ProfileThingController(ProfileThingRepository profileThingRepository) {
    this.profileThingRepository = profileThingRepository;
  }

  @RequestMapping(method=RequestMethod.GET)
  public List<ProfileThing> profileThings(
      @RequestParam(value="max", defaultValue=MAX_LONG_AS_STRING) long max,
      @RequestParam(value="count", defaultValue="20") int count) {
    return profileThingRepository.findProfileThings(max, count);
  }

  @RequestMapping(value= "/{profileThingId}", method=RequestMethod.GET)
  public String ProfileThing(
      @PathVariable("profileThingId") long profileThingId,
      Model model) {
    model.addAttribute(profileThingRepository.findOne(profileThingId));
    return "profileThing";
  }

  @RequestMapping(method=RequestMethod.POST)
  public String saveProfileThing(ProfileThingForm form, Model model) throws Exception {
    profileThingRepository.save(new ProfileThing(null, form.getMessage(), new Date(),
        form.getLongitude(), form.getLatitude()));
    return "redirect:/profileThings";
  }

}
