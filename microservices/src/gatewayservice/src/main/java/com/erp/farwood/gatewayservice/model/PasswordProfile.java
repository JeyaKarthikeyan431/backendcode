package com.erp.farwood.gatewayservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "SDS_PWD_PROFILE")
@Data
public class PasswordProfile implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "SPP_ID")
	private Integer id;

	@Column(name = "SPP_NAME")
	private String profileName;

	@Column(name = "SPP_SPL_CHAR_REQD_YN")
	private String specialCharYN;

	@Column(name = "SPP_LENGTH")
	private Integer pwdLength;

	@Column(name = "SPP_FRCHAR_OPT")
	private String startsWith;// CH for char and CN for num

	@Column(name = "SPP_LTCHAR_OPT")
	private String endsWith;// CH for char and CN for num

	@Column(name = "SPP_ALPHA_CONT_YN")
	private String contAlphabets;// YN

	@Column(name = "SPP_NUM_CONT_YN")
	private String contNumbers;

	@Column(name = "SPP_REPEAT_CHAR_YN")
	private String repeatCharAllowed;// YN

	@Column(name = "SPP_REPEAT_NO_YN")
	private String repeatNumAllowed;// YN

	@Column(name = "SPP_NO_SIMLAR_CHAR")
	private Integer numSimilarCharsAllowed;

	@Column(name = "SPP_NO_NONREPT")
	private Integer numNonRepatedNumAllowed;

	@Column(name = "SPP_EXP_DAYS")
	private Integer numDaysLeftPwdExp;

	@Column(name = "SPP_NO_WRONG_PWD")
	private Integer numWrongPwdAttempts;

	@Column(name = "SPP_SPL_CHAR_SET")
	private String splCharsAllowed; // (what set of spl chars are allowed)

	@Column(name = "SPP_NO_DAYS_EXPMSG")
	private String numDaysNotifyBeforeExp;

	@Column(name = "SPP_BLNK_SPACE_YN")
	private String blankSpaceAllowed; // YN

	@Column(name = "SPP_DICT_WORD_YN")
	private String dictWordAllowed;

	@Column(name = "SPP_DESC")
	private String profileDescription;

	@Column(name = "SPP_DEFLT_YN")
	private String defaultYN;

	@Column(name = "SPP_MAX_LENGTH")
	private Integer maxLength;

	@Column(name = "SPP_MIN_LCASE_CHARS")
	private Integer minLcaseChars;

	@Column(name = "SPP_MIN_UCASE_CHARS")
	private Integer minUCaseChars;

	@Column(name = "SPP_MIN_SPL_CHARS")
	private Integer minSplChars;

	@Column(name = "SPP_MIN_NUM_CHARS")
	private Integer minNumChars;

	@Column(name = "SPP_CRD")
	private LocalDateTime createdDate;

	@Column(name = "SPP_CRU")
	private String createdUser;

	@Column(name = "SPP_CONTAIN_USERNAME")
	private String userNameYN;

	@Column(name = "SPP_DICT_WORDS")
	private String dictionaryWords;

}
