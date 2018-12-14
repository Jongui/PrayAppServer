package br.com.joaogd53.ads.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.joaogd53.ads.dto.ChurchDto;

@Entity
@Table(name = "Church")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Church {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idChurch")
	private Long idChurch;
	@NotBlank
	@Column(name = "name")
	private String name;
	@NotBlank
	@Column(name = "city")
	private String city;
	@NotBlank
	@Column(name = "region")
	private String region;
	@NotBlank
	@Column(name = "country")
	private String country;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	private User createdBy;
	@Column(name = "createdAt")
	private Date createdAt;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "changedBy")
	private User changedBy;
	@Column(name = "changedAt")
	private Date chagedAt;

	public Church() {

	}

	public Church(ChurchDto churchDto, User createdBy, User changedBy) {
		this.idChurch = churchDto.getIdChurch();
		this.name = churchDto.getName();
		this.city = churchDto.getCity();
		this.region = churchDto.getRegion();
		this.country = churchDto.getCountry();
		this.createdBy = createdBy;
		this.createdAt = churchDto.getCreatedAt();
		try {
			this.changedBy = changedBy;
			this.chagedAt = churchDto.getChagedAt();
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

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}

	public Date getChagedAt() {
		return chagedAt;
	}

	public void setChagedAt(Date chagedAt) {
		this.chagedAt = chagedAt;
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
