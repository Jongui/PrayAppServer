package br.com.joaogd53.ads.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.joaogd53.ads.model.UserPray;
import br.com.joaogd53.ads.model.UserPrayIdentity;

public interface UserPrayRepository
		extends PagingAndSortingRepository<UserPray, UserPrayIdentity>, UserPrayRepositoryCustom {

	@Query("select avg(rate) from UserPray u where u.id.pray.idPray = :idPray")
	float calculatePrayAvg(@Param("idPray") Long idPray);

}
