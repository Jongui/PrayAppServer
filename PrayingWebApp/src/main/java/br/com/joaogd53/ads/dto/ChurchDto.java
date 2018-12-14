package br.com.joaogd53.ads.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.joaogd53.ads.model.Church;

public class ChurchDto {

	private Long idChurch;
	private String name;
	private String city;
	private String region;
	private String country;
	private Long createdBy;
	private Date createdAt;
	@JsonInclude(Include.NON_NULL)
	private Long changedBy;
	@JsonInclude(Include.NON_NULL)
	private Date changedAt;

	public ChurchDto() {

	}

	public ChurchDto(Church church) {
		this.idChurch = church.getIdChurch();
		this.name = church.getName();
		this.city = church.getCity();
		this.region = church.getRegion();
		this.country = church.getCountry();
		this.createdBy = church.getCreatedBy().getIdUser();
		this.createdAt = church.getCreatedAt();
		try {
			this.changedBy = church.getChangedBy().getIdUser();
			this.changedAt = church.getChangedAt();
		} catch (NullPointerException ex) {

		}
	}

	public Long getIdChurch() {
		return idChurch;
	}

	public void setIdChurch(Long idChurch) {
		this.idChurch = idChurch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(Long changedBy) {
		this.changedBy = changedBy;
	}

	public Date getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(Date chagedAt) {
		this.changedAt = chagedAt;
	}

}
