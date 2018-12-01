package br.com.joaogd53.ads.controller;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.com.joaogd53.ads.dto.UserDto;
import br.com.joaogd53.ads.model.Church;
import br.com.joaogd53.ads.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppContextConfig.class })
@WebAppConfiguration
public class UserContollerTest {
	private static final String LOCATION = "Location";

	@Inject
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void create() throws Exception {
		// mockMvc.perform(buildDeleteRequest("")).andExpect(status().isNoContent());
		User user = mockUserInstance("joaogd53@gmail.com");
		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated())
				.andExpect(header().string(LOCATION, is(not(""))))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.email", is(user.getEmail())));
	}

	@Test
	public void createDuplicateEmail() throws Exception {
		// mockMvc.perform(buildDeleteRequest("")).andExpect(status().isNoContent());
		User user = mockUserInstance("joaogd53teste@gmail.com");
		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isBadRequest());
	}

	@Test
	public void createWithWrongChurch() throws Exception {
		User user = this.mockUserInstance("joaogd53teste4@gmail.com");
		Church church = user.getChurch();
		church.setIdChurch(new Long(999));
		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isBadRequest());
	}

	@Test
	public void readAll() throws Exception {
		// mockMvc.perform(buildDeleteRequest("")).andExpect(status().isNoContent());
		User user = this.mockUserInstance("joaogd53teste1@gmail.com");

		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated());

		mockMvc.perform(buildGetRequest("")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	@Test
	public void searchByChurch() throws Exception {
		User user = this.mockUserInstance("joaogd53teste3@gmail.com");
		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated());
		mockMvc.perform(buildGetRequestByChurch(user.getChurch())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	@Test
	public void searchByEmail() throws Exception {
		User user = this.mockUserInstance("joaogd53teste2@gmail.com");
		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated());
		mockMvc.perform(buildGetRequestByEmail(user.getEmail())).andExpect(status().isOk());
	}

	@Test
	public void searchById() throws Exception {
		User user = this.mockUserInstance("joaogd53teste5@gmail.com");
		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		String id = performPostAndGetId(user);
		mockMvc.perform(buildGetRequest(id)).andExpect(status().isOk());
	}

	@Test
	public void userNotFound() throws Exception {
		mockMvc.perform(buildGetRequest("999")).andExpect(status().isNotFound());
	}

	@Test
	public void updateUser() throws Exception {
		User user = this.mockUserInstance("joaogd53teste6@gmail.com");
		mockMvc.perform(buildChurchPostRequest(user.getChurch())).andExpect(status().isCreated());
		String id = performPostAndGetId(user);
		user.setIdUser(new Long(id));
		String newName = "New User Name";
		user.setUserName(newName);
		mockMvc.perform(buildPutRequest(new UserDto(user))).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.userName", is(newName)));
	}

	private MockHttpServletRequestBuilder buildChurchPostRequest(Church church) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return post(ChurchController.PATH)
				.content(toJson(church)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildPutRequest(UserDto user) throws Exception {
		return put(UserController.PATH + "/" + user.getIdUser())
				.content(toJson(user)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildPostRequest(UserDto user) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return post(UserController.PATH)
				.content(toJson(user)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByEmail(String email) throws Exception {
		return get(UserController.PATH + "/email/" + email + "/").header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildGetRequest(String id) throws Exception {
		return get(UserController.PATH + "/" + id).header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByChurch(Church church) {
		String idChurch = church.getIdChurch().toString();
		return get(UserController.PATH + "/church/" + idChurch).header("Authorization","test");
	}

	private String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	private String performPostAndGetId(User user) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildPostRequest(new UserDto(user)))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private String performChurchPostAndGetId(Church church) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildChurchPostRequest(church))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private String getIdFromLocation(String location) {
		return location.substring(location.lastIndexOf('/') + 1);
	}

	private User mockUserInstance(String email) throws Exception {
		User user = new User();
		user.setChurch(this.mockChurchToSave());
		user.setCity("Curitiba");
		user.setCountry("Brasil");
		user.setEmail(email);
		// user.setIdUser(new Long(1));
		user.setUserName("Jongui");
		return user;
	}

	private Church mockChurchToSave() throws Exception {
		Church church = new Church();
		church.setCity("Curitiba");
		church.setCountry("Brasil");
		church.setName("Primeira igreja irmãos menonitas do boqueirão");
		church.setRegion("Paraná");
		String id = this.performChurchPostAndGetId(church);
		church.setIdChurch(new Long(id));
		return church;
	}
}
