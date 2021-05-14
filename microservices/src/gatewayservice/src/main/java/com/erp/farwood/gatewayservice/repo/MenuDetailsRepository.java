package com.erp.farwood.gatewayservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.farwood.gatewayservice.model.MenuDetails;

@Repository
public interface MenuDetailsRepository extends JpaRepository<MenuDetails, String> {

	@Query("select m from MenuDetails m where m.menuId=:menuId and m.subMenuId=:subMenuId")
	MenuDetails findMenuDtlsByMenuAndSubMenu(String menuId, String subMenuId);

	@Query("select m from MenuDetails m where m.menuId=:menuId")
	List<MenuDetails> findMenuDtlsByMenuId(String menuId);
	
	@Query("select m from MenuDetails m where m.subMenuId=:subMenuId")
	List<MenuDetails> findMenuDtlsBySubMenuId(String subMenuId);
}
