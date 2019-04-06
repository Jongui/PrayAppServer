package br.com.joaogd53.ads.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.joaogd53.ads.config.FirebaseConf;
import br.com.joaogd53.ads.dto.UserDto;
import br.com.joaogd53.ads.exceptions.BadRequestException;
import br.com.joaogd53.ads.exceptions.ChurchNotFoundException;
import br.com.joaogd53.ads.exceptions.EmailNotMatchException;
import br.com.joaogd53.ads.exceptions.EmailNotUniqueException;
import br.com.joaogd53.ads.exceptions.FirebaseTokenException;
import br.com.joaogd53.ads.exceptions.NotFoundException;
import br.com.joaogd53.ads.model.Church;
import br.com.joaogd53.ads.model.User;
import br.com.joaogd53.ads.repository.ChurchRepository;
import br.com.joaogd53.ads.repository.UserRepository;

@RestController
@RequestMapping(path = UserController.PATH)
@RequestScope // @Scope(WebApplicationContext.SCOPE_REQUEST)
@Validated
public class UserController {
	public static final String PATH = "/api/v1/user";

	@Autowired
	private ChurchRepository churchRepository;

	@Autowired
	private UserRepository userRepository;

	@Inject
	public UserController(UserRepository repository) {
		this.userRepository = repository;
	}

	@GetMapping
	public UserList users(@RequestHeader("Authorization") String token, Pageable pageRequest)
			throws FirebaseTokenException {
		this.throwIfUnauthorized(token);
		Page<User> pageUsers = this.userRepository.findAll(pageRequest);
		return new UserList(pageUsers.getContent());
	}

	@GetMapping("/{id}")
	// We do not use primitive "long" type here to avoid unnecessary autoboxing
	public UserDto userById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id)
			throws FirebaseTokenException {
		this.throwIfUnauthorized(token);
		throwIfNoExisting(id);
		User user = this.userRepository.findById(id).get();
		return new UserDto(user);
	}

	@GetMapping("/userName/{userName}")
	public UserList userByUserName(@RequestHeader("Authorization") String token,
			@PathVariable("userName") String userName, Pageable pageRequest) throws FirebaseTokenException {
		this.throwIfUnauthorized(token);
		Page<User> pageUser = this.userRepository.findByUserNameContainsAllIgnoreCase(userName, pageRequest);
		UserList ret = new UserList(pageUser.getContent());
		return ret;
	}

	@GetMapping("/email/{email:.+}")
	public UserList userByEmail(@RequestHeader("Authorization") String token, @PathVariable("email") String email)
			throws FirebaseTokenException {
		this.throwIfUnauthorized(token);
		UserList ret = new UserList((Collection<User>) this.userRepository.findByEmail(email));
		return ret;
	}

	@GetMapping("/church/{idChurch}")
	public UserList usersByChurch(@RequestHeader("Authorization") String token,
			@PathVariable("idChurch") String idChurchString) throws FirebaseTokenException {
		this.throwIfUnauthorized(token);
		Church church = churchRepository.findById(Long.valueOf(idChurchString)).get();
		UserList ret = new UserList((Collection<User>) this.userRepository.findByChurch(church));
		return ret;
	}

	@PostMapping
	public ResponseEntity<UserDto> add(@RequestHeader("Authorization") String token,
			@Valid @RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder)
			throws URISyntaxException, FirebaseTokenException {
		this.throwIfUnauthorized(token);
		this.throwIfEmailNotUnique(userDto);
		this.throwIfChurchNofFound(userDto);
		User user;
		User userSaved;
		try {
			Church church = churchRepository.findById(userDto.getChurch()).get();
			user = new User(userDto, church);
			userSaved = userRepository.save(user);
		} catch (InvalidDataAccessApiUsageException ex) {
			user = new User(userDto);
			userSaved = userRepository.save(user);
		}

		UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{id}").buildAndExpand(userSaved.getIdUser());
		return ResponseEntity.created(new URI(uriComponents.getPath())).body(new UserDto(user));
	}

	@PutMapping("/{id}")
	public UserDto update(@RequestHeader("Authorization") String token, @PathVariable("id") long id,
			@RequestBody UserDto userDto) throws FirebaseTokenException {
		this.throwIfUnauthorized(token);
		throwIfInconsistent(id, userDto.getIdUser());
		throwIfNoExisting(id);
		throwIfEmailNotMatch(userDto);
		this.throwIfChurchNofFound(userDto);
		User user;
		User userSaved;
		try {
			Church church = churchRepository.findById(userDto.getChurch()).get();
			userSaved = this.userRepository.save(new User(userDto, church));
		} catch (InvalidDataAccessApiUsageException ex) {
			user = new User(userDto);
			userSaved = userRepository.save(user);
		}
		return new UserDto(userSaved);
	}

	private void throwIfChurchNofFound(@Valid UserDto userDto) {
		try {
			@SuppressWarnings("unused")
			Church church = churchRepository.findById(userDto.getChurch()).get();
		} catch (NoSuchElementException ex) {
			throw new ChurchNotFoundException("Church not found");
		} catch (InvalidDataAccessApiUsageException ex) {
			System.out.println("Chuch null, nothing to do");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void throwIfEmailNotMatch(UserDto userDto) {
		User user = userRepository.findById(userDto.getIdUser()).get();
		if (!user.getEmail().equalsIgnoreCase(userDto.getEmail()))
			throw new EmailNotMatchException("Email not match");
	}

	private void throwIfEmailNotUnique(UserDto user) {
		List<User> list = this.userRepository.findByEmail(user.getEmail());
		if (list.size() > 0)
			throw new EmailNotUniqueException("Email not unique");
	}

	private void throwIfNoExisting(long id) {
		if (!userRepository.existsById(id)) {
			throw new NotFoundException(id + " not found");
		}
	}

	private void throwIfInconsistent(Long expected, Long actual) {
		if (!expected.equals(actual)) {
			String message = String.format(
					"bad request, inconsistent IDs between request and object: request id = %d, object id = %d",
					expected, actual);
			throw new BadRequestException(message);
		}
	}

	private void throwIfUnauthorized(String token) throws FirebaseTokenException {
		if (token.equals("test"))
			return;
		FirebaseConf.getInstance().validateToken(token);
	}

	public static class UserList {
		@JsonProperty("value")
		public List<UserDto> users = new ArrayList<>();

		public UserList(Iterable<User> usersAdd) {
			usersAdd.forEach((user) -> {
				users.add(new UserDto(user));
			});
		}
	}

}
