package com.erp.farwood.gatewayservice.repo;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.farwood.gatewayservice.model.ListOptionsMaster;
import com.erp.farwood.gatewayservice.model.ListOptionsPK;

@Repository
public interface ListOptionsRepo extends JpaRepository<ListOptionsMaster, ListOptionsPK> {

	@Query("Select l from ListOptionsMaster l where l.idType=:idType")
	Stream<ListOptionsMaster> findByListOptionsByIdType(String idType);

	@Query("Select l from ListOptionsMaster l where l.idType=:idType and l.level1Value=:level1Value and l.level2Value=:level2Value")
	Stream<ListOptionsMaster> findListOptionsByLevel1ValueAndLevel2Value(String idType, String level1Value,
			String level2Value);

	@Query("Select l from ListOptionsMaster l where l.idType=:idType and l.level1Value=:level1Value")
	Stream<ListOptionsMaster> findListOptionsByLevel1Value(String idType, String level1Value);

	@Query("Select l.idValue from ListOptionsMaster l where l.optionId=:optionId and l.idType='USER_DESIGNATION'")
	String findRoleDesc(String optionId);
	
	@Query("Select l.idValue from ListOptionsMaster l where l.optionId=:optionId and l.idType='USER_DEPT'")
	String findDepartmentDesc(String optionId);
}
