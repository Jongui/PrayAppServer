package br.com.joaogd53.ads.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class UserPrayIdentity implements Serializable {

	private static final long serialVersionUID = -6778443250863598849L;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@ManyToOne
	@JoinColumn(name = "pray")
	private Pray pray;

	public UserPrayIdentity() {

	}

	public UserPrayIdentity(User user, Pray pray) {
		this.user = user;
		this.pray = pray;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Pray getPray() {
		return pray;
	}

	public void setPray(Pray pray) {
		this.pray = pray;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	
	
}
