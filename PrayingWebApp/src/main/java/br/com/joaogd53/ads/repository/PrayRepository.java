package br.com.joaogd53.ads.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.joaogd53.ads.model.Pray;

public interface PrayRepository extends PagingAndSortingRepository<Pray, Long>, PrayRespositoryCustom {

}
