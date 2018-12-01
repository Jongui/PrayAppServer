package br.com.joaogd53.ads.dto;

import java.util.Date;

import br.com.joaogd53.ads.model.Pray;

public class PrayDto {

	private Long idPray;
	private String description;
	private Date beginDate;
	private Date endDate;
	private Long creator;

	public PrayDto() {

	}

	public PrayDto(Pray pray) {
		this.idPray = pray.getIdPray();
		this.description = pray.getDescription();
		this.beginDate = pray.getBeginDate();
		this.endDate = pray.getEndDate();
		this.creator = pray.getCreator().getIdUser();
	}

	public Long getIdPray() {
		return idPray;
	}

	public void setIdPray(Long idPray) {
		this.idPray = idPray;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

}
