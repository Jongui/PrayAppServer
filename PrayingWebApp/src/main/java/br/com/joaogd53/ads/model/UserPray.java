package br.com.joaogd53.ads.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.joaogd53.ads.dto.UserPrayDto;

@Entity
@Table(name = "UserGroup")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserPray implements Serializable {

	private static final long serialVersionUID = 3771158524716969050L;

	@EmbeddedId
	private UserPrayIdentity id;
	@Column(name = "acceptanceDate")
	private Date acceptanceDate;
	@Column(name = "exitDate")
	private Date exitDate;
	@Column(name = "rate")
	private Integer rate;

	public UserPray() {

	}

	public UserPray(UserPrayDto userPrayDto) {

	}

	public UserPray(User user, Pray pray, UserPrayDto userPrayDto) {
		this.id = new UserPrayIdentity();
		this.id.setUser(user);
		this.id.setPray(pray);
		this.acceptanceDate = userPrayDto.getAcceptanceDate();
		this.exitDate = userPrayDto.getExitDate();
		this.rate = userPrayDto.getRate();
	}

	public UserPrayIdentity getId() {
		return id;
	}

	public void setId(UserPrayIdentity id) {
		this.id = id;
	}

	public Date getAcceptanceDate() {
		return acceptanceDate;
	}

	public void setAcceptanceDate(Date acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return e.getLocalizedMessage();
		}
	}

}
