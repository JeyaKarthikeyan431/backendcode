package com.erp.farwood.portal.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.farwood.portal.model.UdsDocFiles;

@Repository
public interface UdsDocFilesRepo extends JpaRepository<UdsDocFiles, Integer> {

	List<UdsDocFiles> findByUserId(Integer userId);

	UdsDocFiles findByFileId(Long fileId);

	@Transactional
	@Modifying
	@Query("delete from UdsDocFiles pdf where pdf.userId=:userId and pdf.docId=:docId ")
	void deleteByUserIdAndDocId(int userId, String docId);

	@Query(" from UdsDocFiles pdf where pdf.userId=:userId and pdf.docId=:docId ")
	List<UdsDocFiles> findByUserIdAndDocId(int userId, String docId);

}
