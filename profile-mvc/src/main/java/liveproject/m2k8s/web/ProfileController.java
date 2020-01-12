package liveproject.m2k8s.web;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private ProfileRepository profileRepository;

    @Value("${images.directory:/tmp}")
    private String uploadFolder;

    @Value("classpath:ghost.jpg")
    private Resource defaultImage;

    @Autowired
    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
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

        profileRepository.save(profile);
        return "redirect:/profile/" + profile.getUsername();
    }

    @RequestMapping(value = "/{username}", method = GET)
    public String showProfile(@PathVariable String username, Model model) {
        Profile profile = profileRepository.findByUsername(username);
        model.addAttribute(profile);
        return "profile";
    }

    @RequestMapping(value = "/{username}", method = POST)
    @Transactional
    public String updateProfile(@PathVariable String username, @ModelAttribute Profile profile, Model model) {
        Profile dbProfile = profileRepository.findByUsername(username);
        boolean dirty = false;
        if (!StringUtils.isEmpty(profile.getEmail())
                && !profile.getEmail().equals(dbProfile.getEmail())) {
            dbProfile.setEmail(profile.getEmail());
            dirty = true;
        }
        if (!StringUtils.isEmpty(profile.getFirstName())
                && !profile.getFirstName().equals(dbProfile.getFirstName())) {
            dbProfile.setFirstName(profile.getFirstName());
            dirty = true;
        }
        if (!StringUtils.isEmpty(profile.getLastName())
                && !profile.getLastName().equals(dbProfile.getLastName())) {
            dbProfile.setLastName(profile.getLastName());
            dirty = true;
        }
        if (dirty) {
            profileRepository.save(dbProfile);
        }
        model.addAttribute(profile);
        return "profile";
    }

    @RequestMapping(value = "/{username}/image.jpg", method = GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] displayImage(@PathVariable String username) throws IOException {
        InputStream in = null;
        try {
            Profile profile = profileRepository.findByUsername(username);
            if ((profile == null) || StringUtils.isEmpty(profile.getImageFileName())) {
                in = defaultImage.getInputStream();
            } else {
                in = new FileInputStream(profile.getImageFileName());
            }
            return IOUtils.toByteArray(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    @RequestMapping(value = "/upload/{username}", method = POST)
    @Transactional
    public String uploadImage(@PathVariable String username, @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("imageMessage", "Empty file - please select a file to upload");
            return "redirect:/profile/" + username;
        }
        String fileName = file.getOriginalFilename();
        if (!(fileName.endsWith("jpg") || fileName.endsWith("JPG"))) {
            redirectAttributes.addFlashAttribute("imageMessage", "JPG files only - please select a file to upload");
            return "redirect:/profile/" + username;
        }
        try {
            final String contentType = file.getContentType();
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder, username+".jpg");
            Files.write(path, bytes);
            Profile profile = profileRepository.findByUsername(username);
            profile.setImageFileName(path.toString());
            profile.setImageFileContentType(contentType);
            profileRepository.save(profile);
            redirectAttributes.addFlashAttribute("imageMessage",
                    "You successfully uploaded '" + fileName + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/profile/" + username;
    }

}
