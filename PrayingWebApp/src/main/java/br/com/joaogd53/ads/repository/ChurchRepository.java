package br.com.joaogd53.ads.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.joaogd53.ads.model.Church;

public interface ChurchRepository extends PagingAndSortingRepository<Church, Long>, ChurchRepositoryCustom {

}
