package br.com.joaogd53.ads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.joaogd53.ads.dto.UserPrayDto;
import br.com.joaogd53.ads.model.Pray;
import br.com.joaogd53.ads.model.User;
import br.com.joaogd53.ads.model.UserPray;
import br.com.joaogd53.ads.model.UserPrayIdentity;
import br.com.joaogd53.ads.repository.PrayRepository;
import br.com.joaogd53.ads.repository.UserPrayRepository;
import br.com.joaogd53.ads.repository.UserRepository;

@RestController
@RequestMapping(path = UserPrayController.PATH)
@RequestScope // @Scope(WebApplicationContext.SCOPE_REQUEST)
@Validated
public class UserPrayController {
	public static final String PATH = "/api/v1/userPray";

	@Autowired
	private UserPrayRepository userPrayRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PrayRepository prayRepository;

	@GetMapping("/user/{idUser}")
	public UserPrayList prayByUser(@RequestHeader("Authorization")String token, @PathVariable("idUser") Long id) {
		User user = userRepository.findById(id).get();
		UserPrayList prayList = new UserPrayList((Collection<UserPray>) this.userPrayRepository.findByIdUser(user));
		return prayList;
	}

	@PostMapping
	public UserPrayDto add(@RequestHeader("Authorization")String token, @Valid @RequestBody UserPrayDto userPrayDto) {
		UserPray userPraySaved;
		Pray pray = prayRepository.findById(userPrayDto.getPray()).get();
		User user = userRepository.findById(userPrayDto.getUser()).get();
		UserPray userPray = new UserPray(user, pray, userPrayDto);
		userPray.setAcceptanceDate(userPrayDto.getAcceptanceDate());
		try {
			userPraySaved = this.userPrayRepository.save(userPray);
		} catch (Exception ex) {
			userPraySaved = this.userPrayRepository.findById(new UserPrayIdentity()).get();
			ex.printStackTrace();
		}
		return new UserPrayDto(userPraySaved);
	}

	public static class UserPrayList {
		@JsonProperty("value")
		public List<UserPrayDto> users = new ArrayList<>();

		public UserPrayList(Iterable<UserPray> usersAdd) {
			usersAdd.forEach((userPray) -> {
				UserPrayDto userPrayDto = new UserPrayDto(userPray);
				users.add(userPrayDto);
			});
		}
	}

}
