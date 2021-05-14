package com.erp.farwood.gatewayservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.farwood.gatewayservice.model.PasswordProfile;

@Repository
public interface PasswordRepo extends JpaRepository<PasswordProfile, String> {

	@Query("select p from PasswordProfile p where p.profileName=:passwordProfile")
	PasswordProfile findProfileByProfileName(String passwordProfile);
}
