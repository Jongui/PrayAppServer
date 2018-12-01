package br.com.joaogd53.ads.repository;

import java.util.List;

import br.com.joaogd53.ads.model.Pray;
import br.com.joaogd53.ads.model.User;

public interface PrayRespositoryCustom {
	public List<Pray> findByCreator(User user);
}
