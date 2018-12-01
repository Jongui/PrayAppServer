package br.com.joaogd53.ads.repository;

import java.util.List;

import br.com.joaogd53.ads.model.Church;

public interface ChurchRepositoryCustom {

	public List<Church> findByCity(String city);
	public List<Church> findByRegion(String region);
	public List<Church> findByCountry(String country);

}
