package com.manning.pl.profile;

import org.springframework.core.SpringVersion;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.StringJoiner;

@Controller
class ProfileController {
	private static final String UPLOADED_FOLDER = "/tmp";
	private final ProfileRepository profileRepository;

	public ProfileController(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@PostMapping("profile/uploadPhoto")
	public String multiFileUpload(@RequestParam("files") MultipartFile[] files,
								  RedirectAttributes redirectAttributes) {

		StringJoiner sj = new StringJoiner(" , ");

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; //next pls
			}

			try {

				byte[] bytes = file.getBytes();
				Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
				Files.write(path, bytes);

				sj.add(file.getOriginalFilename());

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		String uploadedFileName = sj.toString();
		if (StringUtils.isEmpty(uploadedFileName)) {
			redirectAttributes.addFlashAttribute("message",
					"Please select a file to upload");
		} else {
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + uploadedFileName + "'");
		}

		return "redirect:/uploadStatus";

	}

	@GetMapping("profile/current")
	@ResponseStatus(value = HttpStatus.OK)
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public String viewCurrentAccount(Principal principal, Model model) {
		Assert.notNull(principal);
		model.addAttribute("profile", profileRepository.findOneByEmail(principal.getName()));
		return "profile/profile";
	}

	@GetMapping("profile/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	@Secured("ROLE_ADMIN")
	public String viewAccount(@PathVariable("id") Long id, Model model) {

		model.addAttribute("profile", profileRepository.findOne(id));
		return "profile/profile";
	}
}
