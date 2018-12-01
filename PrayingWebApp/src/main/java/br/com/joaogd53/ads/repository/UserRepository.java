package br.com.joaogd53.ads.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.joaogd53.ads.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, UserRepositoryCustom {

}
