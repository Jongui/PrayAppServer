package br.com.joaogd53.ads.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.joaogd53.ads.model.Church;
import br.com.joaogd53.ads.model.User;

public interface UserRepositoryCustom {
	public List<User> findByEmail(String email);
	public List<User> findByChurch(Church church);
	public Page<User> findByUserNameContainsAllIgnoreCase(String userNamePart, Pageable pageRequest);
}
