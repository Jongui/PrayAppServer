package br.com.joaogd53.ads.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.joaogd53.ads.model.UserPray;
import br.com.joaogd53.ads.model.UserPrayIdentity;

public interface UserPrayRepository
		extends PagingAndSortingRepository<UserPray, UserPrayIdentity>, UserPrayRepositoryCustom {

}
