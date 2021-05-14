
package com.erp.farwood.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.farwood.portal.model.LoginDetails;

@Repository
public interface UserRepo extends JpaRepository<LoginDetails, Integer> {

	@Query("Select u from LoginDetails u where Upper(u.emailId) = Upper(:emailId)")
	public LoginDetails findByEmailId(String emailId);

	@Query("Select u from LoginDetails u where u.userId=:userId ")
	public LoginDetails getUserById(@Param(value = "userId") Integer userId);

}
