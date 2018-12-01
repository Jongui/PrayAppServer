package br.com.joaogd53.ads.controller;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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
import br.com.joaogd53.ads.dto.PrayDto;
import br.com.joaogd53.ads.dto.UserDto;
import br.com.joaogd53.ads.model.Church;
import br.com.joaogd53.ads.model.Pray;
import br.com.joaogd53.ads.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppContextConfig.class })
@WebAppConfiguration
public class PrayControllerTest {

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
		Pray pray = this.mockPray("joaogd53@gmail.com");

		mockMvc.perform(buildPostRequest(new PrayDto(pray))).andExpect(status().isCreated())
				.andExpect(header().string(LOCATION, is(not(""))))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is(pray.getDescription()))); // requires
		// com.jayway.jsonpath:json-path
	}

	@Test
	public void readAll() throws Exception {
		Pray pray = this.mockPray("joaogd531@gmail.com");
		String id = this.performPostAndGetId(pray);
		pray.setIdPray(new Long(id));

		mockMvc.perform(buildGetRequest("")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	@Test
	public void readByUser() throws Exception {
		Pray pray = this.mockPray("joaogd532@gmail.com");
		String id = this.performPostAndGetId(pray);
		pray.setIdPray(new Long(id));

		mockMvc.perform(buildGetByUserRequest(pray.getCreator().getIdUser().toString())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	private MockHttpServletRequestBuilder buildPostRequest(PrayDto pray) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return post(PrayController.PATH).
				content(toJson(pray)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildUserPostRequest(UserDto user) throws Exception {
		return post(UserController.PATH)
				.content(toJson(user)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildChurchPostRequest(Church church) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return post(ChurchController.PATH)
				.content(toJson(church)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildGetRequest(String id) throws Exception {
		return get(PrayController.PATH + "/" + id).header("Authorization","test");
	}

	private MockHttpServletRequestBuilder buildGetByUserRequest(String id) throws Exception {
		return get(PrayController.PATH + "/user/" + id).header("Authorization","test");
	}

	private String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	private String performChurchPostAndGetId(Church church) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildChurchPostRequest(church))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private String performUserPostAndGetId(User user) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildUserPostRequest(new UserDto(user)))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private String performPostAndGetId(Pray pray) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildPostRequest(new PrayDto(pray)))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private String getIdFromLocation(String location) {
		return location.substring(location.lastIndexOf('/') + 1);
	}

	private Pray mockPray(String email) throws Exception {
		Pray ret = new Pray();
		ret.setBeginDate(new Date());
		ret.setDescription("Primeiro motivo de oração");
		ret.setEndDate(new Date());
		ret.setCreator(this.mockUserInstance(email));
		return ret;
	}

	private User mockUserInstance(String email) throws Exception {
		User user = new User();
		user.setChurch(this.mockChurchToSave());
		user.setCity("Curitiba");
		user.setCountry("Brasil");
		user.setEmail(email);
		user.setUserName("Jongui");
		String id = this.performUserPostAndGetId(user);
		user.setIdUser(new Long(id));
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
