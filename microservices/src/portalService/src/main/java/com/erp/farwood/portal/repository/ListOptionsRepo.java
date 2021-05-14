package com.erp.farwood.portal.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.farwood.portal.model.ListOptionsMaster;

@Repository
public interface ListOptionsRepo extends JpaRepository<ListOptionsMaster, String> {

	@Query("Select l from ListOptionsMaster l where l.idType=:idType order by l.orderIdSeq asc")
	Stream<ListOptionsMaster> findByListOptionsByIdType(String idType);

	@Query("Select l from ListOptionsMaster l where l.idType=:idType and l.level1Type=:level1Type and l.level1Id=:level1Id "
			+ "and l.level2Type=:level2Type and l.level2Id=:level2Id order by l.orderIdSeq asc")
	Stream<ListOptionsMaster> findListOptionsByLevel1AndLevel2TypeValue(String idType, String level1Type,
			String level1Id, String level2Type, String level2Id);

	@Query("Select l from ListOptionsMaster l where l.idType=:idType and l.level1Type=:level1Type and l.level1Id=:level1Id order by l.orderIdSeq asc")
	Stream<ListOptionsMaster> findListOptionsByLevel1TypeAndValue(String idType, String level1Type, String level1Id);

	@Query("Select l.idValue from ListOptionsMaster l where l.idType=:idType and l.optionId=:optionId order by l.orderIdSeq asc")
	String findValueByTypeAndId(String idType, String optionId);

	@Query("Select l.idValue from ListOptionsMaster l where l.optionId=:optionId and l.idType=:idType")
	String findIdValueByOptionId(String optionId, String idType);

}
