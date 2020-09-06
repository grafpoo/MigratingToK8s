package liveproject.m2k8s.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.service.ProfileService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/profile")
public class ProfileController {

    private ProfileService profileService;

    @Value("${images.directory:/tmp}")
    private String uploadFolder;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute(new Profile());
        return "registerForm";
    }

    @RequestMapping(value = "/register", method = POST)
    @Transactional
    public String processRegistration(
            @Valid Profile profile,
            Errors errors) {
        if (errors.hasErrors()) {
            return "registerForm";
        }

        profileService.save(profile);
        return "redirect:/profile/" + profile.getUsername();
    }

    @RequestMapping(value = "/{username}", method = GET)
    public String showProfile(@PathVariable String username, Model model) {
        log.debug("Reading model for: "+username);
        Profile profile = profileService.getProfile(username);
        model.addAttribute(profile);
        return "profile";
    }

    @RequestMapping(value = "/{username}", method = POST)
    @Transactional
    public String updateProfile(@PathVariable String username, @ModelAttribute Profile profile, Model model) {
        if (!username.equals(profile.getUsername())) {
            throw new RuntimeException("Cannot change username for Profile");
        }
        log.debug("Updating model for: "+username);
        profileService.update(profile);
        model.addAttribute(profile);
        return "profile";
    }

}
