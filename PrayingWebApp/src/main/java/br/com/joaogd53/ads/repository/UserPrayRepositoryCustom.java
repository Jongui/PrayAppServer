package br.com.joaogd53.ads.repository;

import java.util.List;

import br.com.joaogd53.ads.model.User;
import br.com.joaogd53.ads.model.UserPray;

public interface UserPrayRepositoryCustom {
	public List<UserPray> findByIdUser(User user);
}
