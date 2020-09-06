package liveproject.m2k8s.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import liveproject.m2k8s.Profile;
import liveproject.m2k8s.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private ProfileService profileService;

    @Value("${images.directory:/tmp}")
    private String uploadFolder;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST")
    })
    public ResponseEntity<?> processRegistration(@Valid @RequestBody Profile profile) {
        profileService.save(profile);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profile);
    }

    @GetMapping(value = "/{username}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    public ResponseEntity<Profile>  showProfile(@PathVariable String username) {
        Profile profile = profileService.getProfile(username);
        if (profile == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profile);
    }

    @PutMapping(value = "/{username}")
    @Transactional
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST")
    })
    public ResponseEntity<Profile>  updateProfile(@Valid @RequestBody Profile profile) {
        profileService.update(profile);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profile);
    }

}
