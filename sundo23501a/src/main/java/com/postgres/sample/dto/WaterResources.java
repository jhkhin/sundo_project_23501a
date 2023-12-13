package com.postgres.sample.dto;

import java.util.Date;

import lombok.Data;

@Data
public class WaterResources {
	private String 	facility_code;
	private String 	facility_category;
	private String	facility_addr;
	private String	facility_type;

	private String 	org_code;
	private Double 	latitude;
	private Double 	longitude;
	private Date 	create_datetime;
	private Date 	modify_datetime;

	//	조회용
	private String org_name;
	private String first_area;
	private String second_area;
	

	// 페이징 작업
	private String rn;
	private String search;   	private String keyword;
	private String pageNum;		private int total;
	private int start; 		 	private int end;
}
