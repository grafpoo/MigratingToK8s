package liveproject.m2k8s.web;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
@Slf4j
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

//    @GetMapping(value = "/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute(new Profile());
//        return "registerForm";
//    }

    @PostMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public Profile processRegistration(@PathVariable String username, @Valid @RequestBody Profile profile) {
        if ((profile == null) || StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Profile username or data not provided");
        }
        if (StringUtils.isEmpty(profile.getUsername())) {
            profile.setUsername(username);
        }
        if (!username.equals(profile)) {
            throw new IllegalArgumentException("Profile username and parameter mismatch");
        }
        profileRepository.save(profile);
        return profile;
    }

    @GetMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Profile showProfile(@PathVariable String username) {
        Profile profile = profileRepository.findByUsername(username);
        return profile;
    }

    @PutMapping(value = "/{username}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public Profile updateProfile(@RequestBody Profile profile) {
        Profile dbProfile = profileRepository.findByUsername(profile.getUsername());
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
        return profile;
    }

    @RequestMapping(value = "/{username}/image.jpg", method = GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> displayImage(@PathVariable String username) {
        log.debug("Reading image for: " + username);
        InputStream in = null;
        try {
            Profile profile = profileRepository.findByUsername(username);
            if ((profile == null) || StringUtils.isEmpty(profile.getImageFileName())) {
                in = defaultImage.getInputStream();
            } else {
                in = new FileInputStream(profile.getImageFileName());
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(IOUtils.toByteArray(in));
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(("Error: " + e.getMessage()).getBytes());
        } finally {
            if (in != null) {
                try { in.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    @RequestMapping(value = "/upload/{username}", method = POST)
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> uploadImage(@PathVariable String username, @RequestParam("file") MultipartFile file,
                                            RedirectAttributes redirectAttributes) {
        log.debug("Updating image for: "+username);
        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Empty file - please select a file to upload");
        }
        String fileName = file.getOriginalFilename();
        if (!(fileName.endsWith("jpg") || fileName.endsWith("JPG"))) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("JPG files only - please select a file to upload");
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
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("You successfully uploaded '" + fileName + "'");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

}
