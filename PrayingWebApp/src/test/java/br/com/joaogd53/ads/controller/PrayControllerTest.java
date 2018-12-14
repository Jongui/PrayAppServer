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
import br.com.joaogd53.ads.dto.PrayDto;
import br.com.joaogd53.ads.dto.UserDto;
import br.com.joaogd53.ads.model.Pray;
import br.com.joaogd53.ads.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppContextConfig.class })
@WebAppConfiguration
public class PrayControllerTest {

	private static final String LOCATION = "Location";
	private static Long counter = new Long(0);

	@Inject
	private WebApplicationContext context;

	private MockMvc mockMvc;
	private User user;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

		counter++;
		String emailAddress = "joaogd53" + counter.toString() + "@gmail.com";
		user = this.mockUserInstance(emailAddress);

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

	@Test
	public void editPray() throws Exception {
		Pray pray = this.mockPray("joaogd533@gmail.com");
		String id = this.performPostAndGetId(pray);
		pray.setIdPray(new Long(id));

		pray.setDescription("New Description");

		mockMvc.perform(buildPutRequest(id, new PrayDto(pray))).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("New Description")));

	}

	@Test
	public void prayDateValidation() throws Exception {
		Pray pray = this.mockPray("joaogd534@gmail.com");
		GregorianCalendar newCalendar = new GregorianCalendar();
		newCalendar.setTime(pray.getBeginDate());
		newCalendar.add(GregorianCalendar.DATE, -1);
		pray.setEndDate(newCalendar.getTime());

		mockMvc.perform(buildPostRequest(new PrayDto(pray))).andExpect(status().isBadRequest());
	}

	private MockHttpServletRequestBuilder buildPostRequest(PrayDto pray) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return post(PrayController.PATH).content(toJson(pray)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildPutRequest(String id, PrayDto pray) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return put(PrayController.PATH + "/" + id).content(toJson(pray)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildUserPostRequest(UserDto user) throws Exception {
		return post(UserController.PATH).content(toJson(user)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequest(String id) throws Exception {
		return get(PrayController.PATH + "/" + id).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetByUserRequest(String id) throws Exception {
		return get(PrayController.PATH + "/user/" + id).header("Authorization", "test");
	}

	private String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
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
		ret.setCreator(user);
		return ret;
	}

	private User mockUserInstance(String email) throws Exception {
		User user = new User();
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
