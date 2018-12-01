package br.com.joaogd53.ads.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.joaogd53.ads.exceptions.BadRequestException;
import br.com.joaogd53.ads.exceptions.NotFoundException;
import br.com.joaogd53.ads.model.Church;
import br.com.joaogd53.ads.repository.ChurchRepository;

@RestController
@RequestMapping(path = ChurchController.PATH)
@RequestScope // @Scope(WebApplicationContext.SCOPE_REQUEST)
@Validated
public class ChurchController {
	public static final String PATH = "/api/v1/church";
	
	@Autowired
	private ChurchRepository repo;

	@Inject
	public ChurchController(ChurchRepository repository) {
		this.repo = repository;
	}

	@GetMapping
	public ChurchList churchs() {
		return new ChurchList((Collection<Church>) this.repo.findAll());
	}

	@GetMapping("/{id}")
	// We do not use primitive "long" type here to avoid unnecessary autoboxing
	public Church churchById(@RequestHeader("Authorization")String token, @PathVariable("id") Long id) {
		throwIfNoExisting(id);
		System.out.println(token);
		Optional<Church> church = this.repo.findById(id);
		return church.get();
	}

	@GetMapping("/city/{city}")
	public ChurchList churchesForCity(@RequestHeader("Authorization")String token, @PathVariable("city") String city) {
		return new ChurchList((Collection<Church>) this.repo.findByCity(city));
	}

	@GetMapping("/country/{country}")
	public ChurchList churchesForCountry(@RequestHeader("Authorization")String token, @PathVariable("country") String country) {
		return new ChurchList((Collection<Church>) this.repo.findByCountry(country));
	}

	@GetMapping("/region/{region}")
	public ChurchList churchesForRegion(@RequestHeader("Authorization")String token, @PathVariable("region") String region) {
		return new ChurchList((Collection<Church>) this.repo.findByRegion(region));
	}

	/**
	 * @RequestBody is bound to the method argument. HttpMessageConverter resolves
	 *              method argument depending on the content type.
	 */
	@PostMapping
	public ResponseEntity<Church> add(@RequestHeader("Authorization")String token, @Valid @RequestBody Church church, UriComponentsBuilder uriComponentsBuilder)
			throws URISyntaxException {
		this.throwIfChurchNotValid(church);

		Church savedChurch = repo.save(church);

		UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{id}")
				.buildAndExpand(savedChurch.getIdChurch());
		return ResponseEntity.created(new URI(uriComponents.getPath())).body(church);
	}

	@DeleteMapping
	@ResponseStatus(NO_CONTENT)
	public void deleteAll() {
		repo.deleteAll();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(NO_CONTENT)
	public void deleteById(@RequestHeader("Authorization")String token, @PathVariable("id") Long id) {
		throwIfNoExisting(id);
		repo.deleteById(id);
	}

	@PutMapping("/{id}")
	public Church update(@RequestHeader("Authorization")String token, @PathVariable("id") long id, @RequestBody Church church) {
		throwIfInconsistent(id, church.getIdChurch());
		throwIfNoExisting(id);
		throwIfChurchNotValid(church);
		return this.repo.save(church);
	}

	private void throwIfChurchNotValid(final Church church) {
		try {
			if (church.getName().equals(""))
				throw new BadRequestException("Name cannot be empty");
			if (church.getCity().equals(""))
				throw new BadRequestException("City cannot be empty");
			if (church.getCountry().equals(""))
				throw new BadRequestException("Country cannot be empty");
			if (church.getRegion().equals(""))
				throw new BadRequestException("Region cannot be empty");
		} catch (NullPointerException ex) {
			throw new BadRequestException("Name cannot be null");
		}
	}

	private void throwIfNoExisting(long id) {
		if (!repo.existsById(id)) {
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

	public static class ChurchList {
		@JsonProperty("value")
		public List<Church> churchs = new ArrayList<Church>();

		public ChurchList(Iterable<Church> chuchesAdd) {
			chuchesAdd.forEach(churchs::add);
		}
	}
}
