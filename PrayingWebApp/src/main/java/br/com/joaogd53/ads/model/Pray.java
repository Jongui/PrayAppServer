package br.com.joaogd53.ads.model;

import java.util.Date;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.joaogd53.ads.dto.PrayDto;

@Entity
@Table(name = "Pray")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Pray {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idPray")
	private Long idPray;
	@Column(name = "description")
	private String description;
	@Column(name = "beginDate")
	private Date beginDate;
	@Column(name = "endDate")
	private Date endDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator")
	private User creator;
	@OneToMany(mappedBy = "id.pray", cascade = CascadeType.ALL, orphanRemoval = false)
	@JsonIgnore
	private Set<UserPray> userPray;

	public Pray() {

	}

	public Pray(PrayDto prayDto, User user) {
		this.idPray = prayDto.getIdPray();
		this.description = prayDto.getDescription();
		this.beginDate = prayDto.getBeginDate();
		this.endDate = prayDto.getEndDate();
		this.creator = user;
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

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Set<UserPray> getUserPray() {
		return userPray;
	}

	public void setUserPray(Set<UserPray> userPray) {
		this.userPray = userPray;
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
