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

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
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
public class UserContollerTest {
	private static final String LOCATION = "Location";

	@Inject
	private WebApplicationContext context;

	private static Long counter = new Long(0);

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
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated())
				.andExpect(header().string(LOCATION, is(not(""))))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.email", is(user.getEmail())));
	}

	@Test
	public void createDuplicateEmail() throws Exception {
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated());
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isBadRequest());
	}

	@Test
	public void createWithWrongChurch() throws Exception {
		UserDto userDto = new UserDto(user);
		userDto.setChurch(new Long(99));
		mockMvc.perform(buildPostRequest(userDto)).andExpect(status().isBadRequest());
	}

	@Test
	public void readAll() throws Exception {
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated());

		mockMvc.perform(buildGetRequest("")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	@Test
	public void searchByChurch() throws Exception {
		String id = performPostAndGetId(user);
		user.setIdUser(new Long(id));
		Church church = this.mockChurchToSave();
		user.setChurch(church);
		mockMvc.perform(buildPutRequest(new UserDto(user))).andExpect(status().isOk());
		mockMvc.perform(buildGetRequestByChurch(user.getChurch())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	@Test
	public void searchByEmail() throws Exception {
		mockMvc.perform(buildPostRequest(new UserDto(user))).andExpect(status().isCreated());
		mockMvc.perform(buildGetRequestByEmail(user.getEmail())).andExpect(status().isOk());
	}

	@Test
	public void searchById() throws Exception {
		String id = performPostAndGetId(user);
		mockMvc.perform(buildGetRequest(id)).andExpect(status().isOk());
	}

	@Test
	public void userNotFound() throws Exception {
		mockMvc.perform(buildGetRequest("999")).andExpect(status().isNotFound());
	}

	@Test
	public void updateUser() throws Exception {
		String id = performPostAndGetId(user);
		user.setIdUser(new Long(id));
		String newName = "New User Name";
		user.setUserName(newName);
		mockMvc.perform(buildPutRequest(new UserDto(user))).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.userName", is(newName)));
	}

	@Test
	public void updateWithWrongChurch() throws Exception {
		String id = performPostAndGetId(user);
		user.setIdUser(new Long(id));
		UserDto userDto = new UserDto(user);
		userDto.setChurch(new Long(99));
		mockMvc.perform(buildPutRequest(userDto)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void searchByPaginate() throws Exception{
		int i = 0;
		do {
			counter++;
			String emailAddress = "joaogd53" + counter.toString() + "@gmail.com";
			user = this.mockUserInstance(emailAddress);
			switch(i) {
			case 0:
				user.setUserName("Alfredo Júnio");
				break;
			case 1:
				user.setUserName("Alfredo Júnior");
				break;
			case 2:
				user.setUserName("Alfred Brecht");
				break;
			case 3:
				user.setUserName("Glauco Júnio");
				break;
			case 4:
				user.setUserName("Amancio Júnio");
				break;
			}
			performPostAndGetId(user);
			i++;
		}while(i < 5);
		mockMvc.perform(buildGetRequestByUserName("alf", "2"))
			.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(3)))));
	}

	@Test
	public void searchAllByPaginate() throws Exception{
		int i = 0;
		do {
			counter++;
			String emailAddress = "joaogd53" + counter.toString() + "@gmail.com";
			user = this.mockUserInstance(emailAddress);
			switch(i) {
			case 0:
				user.setUserName("Alfredo Júnio");
				break;
			case 1:
				user.setUserName("Alfredo Júnior");
				break;
			case 2:
				user.setUserName("Alfred Brecht");
				break;
			case 3:
				user.setUserName("Glauco Júnio");
				break;
			case 4:
				user.setUserName("Amancio Júnio");
				break;
			}
			performPostAndGetId(user);
			i++;
		}while(i < 5);
		mockMvc.perform(buildGetRequestByUserName("*", "10"))
			.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}
	
	private MockHttpServletRequestBuilder buildPutRequest(UserDto user) throws Exception {
		return put(UserController.PATH + "/" + user.getIdUser()).content(toJson(user))
				.contentType(APPLICATION_JSON_UTF8).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildPostRequest(UserDto user) throws Exception {
		return post(UserController.PATH).content(toJson(user)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByEmail(String email) throws Exception {
		return get(UserController.PATH + "/email/" + email + "/").header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByUserName(String namePattern, String size) throws Exception{
		return get(UserController.PATH + "/userName/" + namePattern + "/")
				.header("Authorization", "test")
				.param("size", size);
	}
	
	private MockHttpServletRequestBuilder buildGetRequest(String id) throws Exception {
		return get(UserController.PATH + "/" + id).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetRequestByChurch(Church church) {
		String idChurch = church.getIdChurch().toString();
		return get(UserController.PATH + "/church/" + idChurch).header("Authorization", "test");
	}

	private String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	private String performPostAndGetId(User user) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildPostRequest(new UserDto(user)))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private RequestBuilder buildChurchPostRequest(Church church) throws Exception {
		ChurchDto churchDto = new ChurchDto(church);
		return post(ChurchController.PATH).content(toJson(churchDto)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
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
		user.setCity("Curitiba");
		user.setCountry("Brasil");
		user.setEmail(email);
		user.setAvatarUrl("avatarURL");
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
		church.setCreatedAt(new Date());
		church.setCreatedBy(user);
		String id = this.performChurchPostAndGetId(church);
		church.setIdChurch(new Long(id));
		return church;
	}
}
