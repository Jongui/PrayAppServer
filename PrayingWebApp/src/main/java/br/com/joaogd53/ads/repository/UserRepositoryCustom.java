package br.com.joaogd53.ads.repository;

import java.util.List;

import br.com.joaogd53.ads.model.Church;
import br.com.joaogd53.ads.model.User;

public interface UserRepositoryCustom {
	public List<User> findByEmail(String email);
	public List<User> findByChurch(Church church);
}
