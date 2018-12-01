package br.com.joaogd53.ads.dto;

import java.util.Date;

import br.com.joaogd53.ads.model.UserPray;

public class UserPrayDto {

	private Long user;
	private Long pray;
	private Date acceptanceDate;
	private Date exitDate;
	private Integer rate;

	public UserPrayDto() {

	}

	public UserPrayDto(UserPray userPray) {
		this.user = userPray.getId().getUser().getIdUser();
		this.pray = userPray.getId().getPray().getIdPray();
		this.acceptanceDate = userPray.getAcceptanceDate();
		this.exitDate = userPray.getExitDate();
		this.rate = userPray.getRate();
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Long getPray() {
		return pray;
	}

	public void setPray(Long pray) {
		this.pray = pray;
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

}
