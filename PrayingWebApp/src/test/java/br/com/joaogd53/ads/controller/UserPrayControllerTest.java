package br.com.joaogd53.ads.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.joaogd53.ads.config.WebAppContextConfig;
import br.com.joaogd53.ads.dto.PrayDto;
import br.com.joaogd53.ads.dto.UserDto;
import br.com.joaogd53.ads.dto.UserPrayDto;
import br.com.joaogd53.ads.model.Pray;
import br.com.joaogd53.ads.model.User;
import br.com.joaogd53.ads.model.UserPray;
import br.com.joaogd53.ads.model.UserPrayIdentity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppContextConfig.class })
@WebAppConfiguration
public class UserPrayControllerTest {
	private static final String LOCATION = "Location";

	@Inject
	private WebApplicationContext context;

	private static Long counter = new Long(0);

	private MockMvc mockMvc;
	private User user;
	private User user1;
	private Pray pray;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		counter++;
		String emailAddress = "joaogd53teste" + counter.toString() + "@gmail.com";
		user = this.mockUserInstance(emailAddress);

		counter++;
		emailAddress = "joaogd53teste" + counter.toString() + "@gmail.com";
		user1 = this.mockUserInstance(emailAddress);

		String prayAddress = "joaogd53" + counter.toString() + "@gmail.com";
		pray = this.mockPray(prayAddress);
	}

	@Test
	public void create() throws Exception {
		UserPray userPray = new UserPray();
		userPray.setAcceptanceDate(new Date());
		userPray.setId(new UserPrayIdentity(user, pray));
		userPray.setRate(4);
		UserPrayDto userPrayDto = new UserPrayDto(userPray);
		mockMvc.perform(buildPostRequest(userPrayDto)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.rate", is(userPray.getRate()))); // requires
	}

	@Test
	public void findByUser() throws Exception {
		// Pray pray = this.mockPray("joaogd531@gmail.com");
		// User user = this.mockUserInstance("joaogd53teste1@gmail.com");
		UserPray userPray = new UserPray();
		userPray.setAcceptanceDate(new Date());
		userPray.setId(new UserPrayIdentity(user, pray));
		userPray.setRate(4);
		UserPrayDto userPrayDto = new UserPrayDto(userPray);
		mockMvc.perform(buildPostRequest(userPrayDto)).andExpect(status().isOk());
		mockMvc.perform(buildGetByUserRequest(user.getIdUser().toString())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", is(both(greaterThan(0)).and(lessThan(10)))));
	}

	@Test
	public void findPrayUsers() throws Exception {
		// Pray pray = this.mockPray("joaogd532@gmail.com");
		// User user = this.mockUserInstance("joaogd53teste2@gmail.com");
		UserPray userPray = new UserPray();
		userPray.setAcceptanceDate(new Date());
		userPray.setId(new UserPrayIdentity(user, pray));
		userPray.setRate(4);
		UserPrayDto userPrayDto = new UserPrayDto(userPray);
		mockMvc.perform(buildPostRequest(userPrayDto)).andExpect(status().isOk());

		// Second pray user
		userPray = new UserPray();
		userPray.setAcceptanceDate(new Date());
		userPray.setId(new UserPrayIdentity(user1, pray));
		userPray.setRate(4);
		userPrayDto = new UserPrayDto(userPray);
		mockMvc.perform(buildPostRequest(userPrayDto)).andExpect(status().isOk());
		mockMvc.perform(buildGetByPrayRequest(pray.getIdPray().toString())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.value.length()", is(equalTo(2))));
	}

	@Test
	public void changeUserPrayStartDate() throws Exception {
		// Fist pray user
		// Pray pray = this.mockPray("joaogd534@gmail.com");
		// User user = this.mockUserInstance("joaogd53teste4@gmail.com");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(GregorianCalendar.DATE, 5);

		UserPray userPray = new UserPray();
		userPray.setAcceptanceDate(new Date());
		userPray.setExitDate(calendar.getTime());
		userPray.setId(new UserPrayIdentity(user, pray));
		userPray.setRate(4);
		UserPrayDto userPrayDto = new UserPrayDto(userPray);
		mockMvc.perform(buildPostRequest(userPrayDto)).andExpect(status().isOk());

		userPray.setRate(5);
		userPrayDto = new UserPrayDto(userPray);
		mockMvc.perform(buildPutRequest(userPrayDto)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.rate", is(equalTo(5))));
	}

	@Test
	public void calcAvgPrayRate() throws Exception {

		UserPray userPray = new UserPray();
		userPray.setAcceptanceDate(new Date());
		userPray.setExitDate(new Date());
		userPray.setId(new UserPrayIdentity(user, pray));
		userPray.setRate(5);
		mockMvc.perform(buildPostRequest(new UserPrayDto(userPray))).andExpect(status().isOk());

		UserPray userPray1 = new UserPray();
		userPray1.setAcceptanceDate(new Date());
		userPray1.setExitDate(new Date());
		userPray1.setId(new UserPrayIdentity(user1, pray));
		userPray1.setRate(3);
		mockMvc.perform(buildPostRequest(new UserPrayDto(userPray1))).andExpect(status().isOk());

		double avg = (5 + 3) / 2;

		mockMvc.perform(buildAvgRequest(pray.getIdPray())).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.avg", is(equalTo(avg))));
	}

	private RequestBuilder buildAvgRequest(Long idPray) {
		return get(UserPrayController.PATH + "/avg/" + idPray).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildPostRequest(UserPrayDto userPray) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return post(UserPrayController.PATH).content(toJson(userPray)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildPutRequest(UserPrayDto userPray) throws Exception {
		return put(UserPrayController.PATH + "/" + userPray.getUser()).content(toJson(userPray))
				.contentType(APPLICATION_JSON_UTF8).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildPrayPostRequest(PrayDto pray) throws Exception {
		// post the advertisement as a JSON entity in the request body
		return post(PrayController.PATH).content(toJson(pray)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildUserPostRequest(UserDto user) throws Exception {
		return post(UserController.PATH).content(toJson(user)).contentType(APPLICATION_JSON_UTF8)
				.header("Authorization", "test");
	}

	private String performPrayPostAndGetId(Pray pray) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildPrayPostRequest(new PrayDto(pray)))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private String performUserPostAndGetId(User user) throws Exception {
		MockHttpServletResponse response = mockMvc.perform(buildUserPostRequest(new UserDto(user)))
				.andExpect(status().isCreated()).andReturn().getResponse();

		return getIdFromLocation(response.getHeader(LOCATION));
	}

	private MockHttpServletRequestBuilder buildGetByUserRequest(String id) throws Exception {
		return get(UserPrayController.PATH + "/user/" + id).header("Authorization", "test");
	}

	private MockHttpServletRequestBuilder buildGetByPrayRequest(String id) throws Exception {
		return get(UserPrayController.PATH + "/pray/" + id).header("Authorization", "test");
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
		String id = this.performPrayPostAndGetId(ret);
		ret.setIdPray(new Long(id));
		return ret;
	}

	private User mockUserInstance(String email) throws Exception {
		User user = new User();
		// user.setChurch(this.mockChurchToSave());
		user.setCity("Curitiba");
		user.setCountry("Brasil");
		user.setEmail(email);
		user.setUserName("Jongui");
		user.setAvatarUrl("avatarUrl");
		String id = this.performUserPostAndGetId(user);
		user.setIdUser(new Long(id));
		return user;
	}

	private String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

}
