package br.com.joaogd53.ads.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.joaogd53.ads.dto.UserDto;

@Entity
@Table(name = "User")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idUser")
	private Long idUser;
	@NotBlank
	@Column(name = "email", unique = true)
	private String email;
	@NotBlank
	@Column(name = "userName")
	private String userName;
	@Column(name = "city")
	private String city;
	@Column(name = "country")
	private String country;
	@Column(name = "avatarUrl")
	private String avatarUrl;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "church")
	private Church church;
	@OneToMany(mappedBy = "id.user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<UserPray> userPray = new ArrayList<>();

	public User() {

	}

	public User(UserDto userDto) {
		this.idUser = userDto.getIdUser();
		this.email = userDto.getEmail();
		this.userName = userDto.getUserName();
		this.city = userDto.getCity();
		this.country = userDto.getCountry();
		this.avatarUrl = userDto.getAvatarUrl();
	}

	public User(UserDto userDto, Church church) {
		this.idUser = userDto.getIdUser();
		this.email = userDto.getEmail();
		this.userName = userDto.getUserName();
		this.city = userDto.getCity();
		this.country = userDto.getCountry();
		this.avatarUrl = userDto.getAvatarUrl();
		this.church = church;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Church getChurch() {
		return church;
	}

	public void setChurch(Church church) {
		this.church = church;
	}

	public List<UserPray> getUserPray() {
		return userPray;
	}

	public void setUserPray(List<UserPray> userPray) {
		this.userPray.clear();
		this.userPray.addAll(userPray);
		// this.userPray = userPray;
	}

	@Override
	public String toString() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		System.out.println(gson.toJson(this));
		return gson.toJson(this);
		// try {
		// return new ObjectMapper().writeValueAsString(this);
		// } catch (JsonProcessingException e) {
		// return e.getLocalizedMessage();
		// }
	}

}
