package liveproject.m2k8s.web;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/profile")
public class ProfileController {

  private static final String UPLOADED_FOLDER = "/tmp/";

  private ProfileRepository profileRepository;

  @Autowired
  public ProfileController(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }
  
  @RequestMapping(value="/register", method=GET)
  public String showRegistrationForm(Model model) {
    model.addAttribute(new Profile());
    return "registerForm";
  }
  
  @RequestMapping(value="/register", method=POST)
  @Transactional
  public String processRegistration(
      @Valid Profile profile,
      Errors errors) {
    if (errors.hasErrors()) {
      return "registerForm";
    }
    
    profileRepository.save(profile);
    return "redirect:/profile/" + profile.getUsername();
  }
  
  @RequestMapping(value="/{username}", method=GET)
  public String showProfile(@PathVariable String username, Model model) {
    Profile profile = profileRepository.findByUsername(username);
    model.addAttribute(profile);
    return "profile";
  }

  @RequestMapping(value="/{username}", method=POST)
  public String updateProfile(@PathVariable String username, Model model) {
    Profile profile = profileRepository.findByUsername(username);
    Object profileModel = model.asMap().get("profile");
//    profile.setEmail(StringUtils.isEmpty());

    model.addAttribute(profile);
    return "profile";
  }

  @RequestMapping(value="/{username}/image", method = GET)
  public byte[] displayImage(@PathVariable String username) {
    return new byte[0];
  }

  @RequestMapping(value="/{username}/imageUpload", method = POST)
  public String uploadImage(@PathVariable String username, @RequestParam("file") MultipartFile file,
                            RedirectAttributes redirectAttributes) {
    if (file.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
      return "redirect:uploadStatus";
    }
    try {
      // Get the file and save it somewhere
      byte[] bytes = file.getBytes();
      Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
      Files.write(path, bytes);
      redirectAttributes.addFlashAttribute("message",
              "You successfully uploaded '" + file.getOriginalFilename() + "'");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "redirect:/profile/" + username;
  }

}
