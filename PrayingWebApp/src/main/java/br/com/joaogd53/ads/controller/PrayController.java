package br.com.joaogd53.ads.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.joaogd53.ads.dto.PrayDto;
import br.com.joaogd53.ads.model.Pray;
import br.com.joaogd53.ads.model.User;
import br.com.joaogd53.ads.repository.PrayRepository;
import br.com.joaogd53.ads.repository.UserRepository;

@RestController
@RequestMapping(path = PrayController.PATH)
@RequestScope // @Scope(WebApplicationContext.SCOPE_REQUEST)
@Validated
public class PrayController {
	public static final String PATH = "/api/v1/pray";

	@Autowired
	private PrayRepository prayRepository;
	@Autowired
	private UserRepository userRepository;

	@Inject
	public PrayController(PrayRepository repository) {
		this.prayRepository = repository;
	}

	@GetMapping
	public PrayList users() {
		return new PrayList((Collection<Pray>) this.prayRepository.findAll());
	}

	@GetMapping("/user/{idUser}")
	// We do not use primitive "long" type here to avoid unnecessary autoboxing
	public PrayList prayByUser(@RequestHeader("Authorization")String token, @PathVariable("idUser") Long id) {
		User user = this.userRepository.findById(id).get();
		PrayList prayList = new PrayList((Collection<Pray>) this.prayRepository.findByCreator(user));
		return prayList;
	}

	@PostMapping
	public ResponseEntity<PrayDto> add(@RequestHeader("Authorization")String token, @Valid @RequestBody PrayDto prayDto, UriComponentsBuilder uriComponentsBuilder)
			throws URISyntaxException {
		User user = userRepository.findById(prayDto.getCreator()).get();
		Pray pray = new Pray(prayDto, user);
		Pray praySaved = this.prayRepository.save(pray);
		UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{id}").buildAndExpand(praySaved.getIdPray());
		return ResponseEntity.created(new URI(uriComponents.getPath())).body(new PrayDto(praySaved));
	}

	public static class PrayList {
		@JsonProperty("value")
		public List<PrayDto> prays = new ArrayList<>();

		public PrayList(Iterable<Pray> praysAdd) {
			praysAdd.forEach((pray) -> {
				PrayDto prayDto = new PrayDto(pray);
				prays.add(prayDto);
			});
		}
	}

}
