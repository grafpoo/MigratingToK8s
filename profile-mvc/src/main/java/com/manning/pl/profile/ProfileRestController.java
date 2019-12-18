package com.manning.pl.profile;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.StringJoiner;

@RestController
public class ProfileRestController {
    private final ProfileRepository profileRepository;

    public ProfileRestController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping("api/profile/current")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Profile currentAccount(Principal principal) {
        Assert.notNull(principal);
        return profileRepository.findOneByEmail(principal.getName());
    }

    @GetMapping("api/profile/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public Profile account(@PathVariable("id") Long id) {
        return profileRepository.findOne(id);
    }

}
