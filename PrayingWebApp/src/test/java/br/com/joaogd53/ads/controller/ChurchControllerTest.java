package br.com.joaogd53.ads.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.joaogd53.ads.config.WebAppContextConfig;
import br.com.joaogd53.ads.dto.ChurchDto;
import br.com.joaogd53.ads.dto.UserDto;
import br.com.joaogd53.ads.model.Church;
import br.com.joaogd53.ads.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppContextConfig.class })
@WebAppConfiguration
public class ChurchControllerTest {
	private static final String LOCATION = "Location";

	@Inject
	private WebApplicationContext context;

	private static Long counter = new Long(0);

	private MockMvc mockMvc;
	private User user;
	private User changer;

	@Before
	public void setUp() throws Exception {
		counter++;
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String emailAddress = "joaogd53" + counter.toString() + "@gmail.com";
		user = this.mockUserInstance(emailAddress);

		String emailAddressChanger = "joaogd53test" + counter.toString() + "@gmail.com";
		changer = this.mockUserInstance(emailAddressChanger);
	}

	@Test
	public void create() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("Brasil");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isCreated())
				.andExpect(header().string(LOCATION, is(not(""))))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.name", is(church.getName()))); // requires
		// com.jayway.jsonpath:json-path
	}

	@Test
	public void createBlancName() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("Brasil");
		church.setName("");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void createNullName() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("Brasil");
		church.setName(null);
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void createBlancCity() throws Exception {
		Church church = new Church();
		church.setCity("");
		church.setCountry("Brasil");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void createNullCity() throws Exception {
		Church church = new Church();
		church.setCity(null);
		church.setCountry("Brasil");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void createBlancCountry() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void createNullCountry() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry(null);
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void createBlancRegion() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void createNullRegion() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("Brasil");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion(null);
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isBadRequest());
	}

	@Test
	public void readAll() throws Exception {
		mockMvc.perform(buildDeleteRequest("")).andExpect(status().isNoContent());
		Church church = this.mockChurchToSave();

		mockMvc.perform(buildPostRequest(church)).andExpect(status().isCreated());

		mockMvc.perform(buildGetRequest("")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	@Test
	public void readByIdNotFound() throws Exception {
		mockMvc.perform(buildGetRequest("4711")).andExpect(status().isNotFound());
	}

	@Test
	public void readByIdNegative() throws Exception {
		mockMvc.perform(buildGetRequest("-1")).andExpect(status().isNotFound());
	}

	@Test
	public void readById() throws Exception {
		Church church = this.mockChurchToSave();
		String id = performPostAndGetId(church);

		mockMvc.perform(buildGetRequest(id)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.city", is("Curitiba")));
	}

	@Test
	public void searchByCity() throws Exception {
		Church curitiba = this.mockChurchToSave("Curitiba");
		mockMvc.perform(buildDeleteRequest("")).andExpect(status().isNoContent());
		mockMvc.perform(buildPostRequest(curitiba)).andExpect(status().isCreated());
		mockMvc.perform(buildGetRequestByCity(curitiba.getCity())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(equalTo(1))));
	}

	@Test
	public void searchByCountry() throws Exception {
		Church curitiba = this.mockChurchToSave("Curitiba");
		mockMvc.perform(buildDeleteRequest("")).andExpect(status().isNoContent());
		mockMvc.perform(buildPostRequest(curitiba)).andExpect(status().isCreated());
		mockMvc.perform(buildGetRequestByCountry(curitiba.getCountry())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(equalTo(1))));
	}

	@Test
	public void searchByRegion() throws Exception {
		Church curitiba = this.mockChurchToSave("Curitiba");
		mockMvc.perform(buildDeleteRequest("")).andExpect(status().isNoContent());
		mockMvc.perform(buildPostRequest(curitiba)).andExpect(status().isCreated());
		mockMvc.perform(buildGetRequestByRegion(curitiba.getRegion())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(equalTo(1))));
	}

	@Test
	public void updateChurchById() throws Exception {
		Date changed = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(changed);
		calendar.add(GregorianCalendar.DATE, +2);
		Church church = this.mockChurchToSave();
		String id = performPostAndGetId(church);
		church.setIdChurch(Long.valueOf(id));
		church.setCity("Palmeira");
		church.setChagedAt(changed);
		church.setChangedBy(user);
		mockMvc.perform(buildPutRequest(id, church)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.city", is("Palmeira")));
	}

	@Test
	public void updateChurchInvalidCity() throws Exception {
		Date changed = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(changed);
		calendar.add(GregorianCalendar.DATE, +2);
		Church church = this.mockChurchToSave();
		String id = performPostAndGetId(church);
		church.setIdChurch(Long.valueOf(id));
		church.setCity("");
		church.setChagedAt(changed);
		church.setChangedBy(user);
		mockMvc.perform(buildPutRequest(id, church)).andExpect(status().isBadRequest());
	}

	@Test
	public void updateChurchInvalidConutry() throws Exception {
		Date changed = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(changed);
		calendar.add(GregorianCalendar.DATE, +2);
		Church church = this.mockChurchToSave();
		String id = performPostAndGetId(church);
		church.setIdChurch(Long.valueOf(id));
		church.setCountry("");
		church.setChagedAt(changed);
		church.setChangedBy(user);
		mockMvc.perform(buildPutRequest(id, church)).andExpect(status().isBadRequest());
	}

	@Test
	public void updateChurchInvalidName() throws Exception {
		Date changed = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(changed);
		calendar.add(GregorianCalendar.DATE, +2);
		Church church = this.mockChurchToSave();
		String id = performPostAndGetId(church);
		church.setIdChurch(Long.valueOf(id));
		church.setName("");
		church.setChagedAt(changed);
		church.setChangedBy(user);
		mockMvc.perform(buildPutRequest(id, church)).andExpect(status().isBadRequest());
	}

	@Test
	public void updateChurchInvalidRegion() throws Exception {
		Date changed = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(changed);
		calendar.add(GregorianCalendar.DATE, +2);
		Church church = this.mockChurchToSave();
		String id = performPostAndGetId(church);
		church.setIdChurch(Long.valueOf(id));
		church.setRegion("");
		church.setChagedAt(changed);
		church.setChangedBy(user);
		mockMvc.perform(buildPutRequest(id, church)).andExpect(status().isBadRequest());
	}

	@Test
	public void updateChurchInvalidUser() throws Exception {
		Church church = this.mockChurchToSave();
		String id = performPostAndGetId(church);
		church.setIdChurch(Long.valueOf(id));

		Date changed = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(changed);
		calendar.add(GregorianCalendar.DATE, +2);
		church.setChagedAt(changed);
		church.setChangedBy(changer);
		mockMvc.perform(buildPutRequest(id, church)).andExpect(status().isBadRequest());
	}

	private MockHttpServletRequestBuilder buildDeleteRequest(String id) throws Exception {
		return delete(ChurchController.PATH + "/" + id);
	}

	private MockHttpServletRequestBuilder buildPostRequest(Church church) throws Exception {
		ChurchDto churchDto = new ChurchDto(church);
		return post(ChurchController.PATH).content(toJson(churchDto)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequest(String id) throws Exception {
		return get(ChurchController.PATH + "/" + id).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByCity(String city) throws Exception {
		return get(ChurchController.PATH + "/city/" + city).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByCountry(String country) throws Exception {
		return get(ChurchController.PATH + "/country/" + country).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByRegion(String region) throws Exception {
		return get(ChurchController.PATH + "/region/" + region).header("Authorization", "test");
	}

	private String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	private String performPostAndGetId(Church church) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildPostRequest(church)).andExpect(status().isCreated())
				.andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private MockHttpServletRequestBuilder buildPutRequest(String id, Church church) throws Exception {
		ChurchDto churchDto = new ChurchDto(church);
		return put(ChurchController.PATH + "/" + id).content(toJson(churchDto)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private String performUserPostAndGetId(User user) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildUserPostRequest(new UserDto(user)))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private MockHttpServletRequestBuilder buildUserPostRequest(UserDto user) throws Exception {
		return post(UserController.PATH).content(toJson(user)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private String getIdFromLocation(String location) {
		return location.substring(location.lastIndexOf('/') + 1);
	}

	private Church mockChurchToSave() {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("Brasil");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		return church;
	}

	private Church mockChurchToSave(String city) {
		Church church = new Church();
		church.setCity(city);
		church.setCountry("Brasil");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		church.setCreatedBy(user);
		church.setCreatedAt(new Date());

		return church;
	}

	private User mockUserInstance(String email) throws Exception {
		User user = new User();
		// user.setChurch(this.mockChurchToSave());
		user.setCity("Curitiba");
		user.setCountry("Brasil");
		user.setEmail(email);
		user.setUserName("Jongui");
		user.setAvatarUrl("AvatarUrl");
		String id = this.performUserPostAndGetId(user);
		user.setIdUser(new Long(id));
		return user;
	}
}
