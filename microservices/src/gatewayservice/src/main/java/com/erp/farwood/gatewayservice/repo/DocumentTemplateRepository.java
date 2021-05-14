package com.erp.farwood.gatewayservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.farwood.gatewayservice.model.DocumentTemplate;

@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Integer> {

	@Query("select t from DocumentTemplate t where t.templateName=:templateName and t.templateType=:templateType")
	Optional<DocumentTemplate> findDocumentTemplate(String templateName, String templateType);

}
