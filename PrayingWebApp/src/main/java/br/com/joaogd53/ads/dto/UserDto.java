package br.com.joaogd53.ads.dto;

import br.com.joaogd53.ads.model.User;

public class UserDto {

	private Long idUser;
	private String email;
	private String userName;
	private String city;
	private String country;
	private String avatarUrl;
	private Long church;

	public UserDto() {

	}

	public UserDto(User user) {
		this.idUser = user.getIdUser();
		this.email = user.getEmail();
		this.userName = user.getUserName();
		this.city = user.getCity();
		this.country = user.getCountry();
		this.avatarUrl = user.getAvatarUrl();
		this.church = user.getChurch().getIdChurch();
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

	public Long getChurch() {
		return church;
	}

	public void setChurch(Long church) {
		this.church = church;
	}

}
