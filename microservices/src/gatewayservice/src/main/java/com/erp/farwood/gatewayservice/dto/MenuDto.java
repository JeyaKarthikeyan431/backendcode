package com.erp.farwood.gatewayservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class MenuDto {

	private String menuId;
	private String menuName;
	private String menuCategory;
	private List<SubMenuDto> subMenu;

	public MenuDto() {

	}

	public MenuDto(String menuId, String menuName, List<SubMenuDto> subMenu, String menuCategory) {
		super();
		this.menuId = menuId;
		this.menuName = menuName;
		this.subMenu = subMenu;
		this.menuCategory = menuCategory;
	}

}
