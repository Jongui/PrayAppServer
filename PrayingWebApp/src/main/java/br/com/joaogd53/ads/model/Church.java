package br.com.joaogd53.ads.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	public Church() {

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

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return e.getLocalizedMessage();
		}
	}

}
