package com.erp.farwood.gatewayservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.farwood.gatewayservice.model.RoleGroup;

@Repository
public interface RoleGroupRepo extends JpaRepository<RoleGroup, String> {

	@Query("select r from RoleGroup r where r.userRole in (?1) ")
	List<RoleGroup> findRoleGroupByRoleDesc(List<String> roleDesc);

}
