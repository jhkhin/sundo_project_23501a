package com.postgres.sample.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Observation{
//	관측소
//	관측소코드(PK)		N/A	INTEGER
//	표준코드(PK)(FK)	N/A	VARCHAR(30)
//	관측소명			N/A	VARCHAR(200)
//	관측유형			N/A	VARCHAR(10)
//	위도				N/A	VARCHAR(30)
//	경도				N/A	VARCHAR(30)
//	관측방식			N/A	VARCHAR(10)
//	운영기관코드(FK)	N/A	VARCHAR(30)
//	등록일시			N/A	DATE
//	수정일시			N/A	DATE
	//변경했다.
	private Integer 	observe_code;	//	N/A	INTEGER
	private String		river_code;
	private String		observe_post;
	private String		observe_type;

	private String		observe_method;
	private String		org_code;
	private String  	org_area;

	private Double		latitude;
	private Double		longitude;
	
	private Date		create_datetime;
	private Date		modify_datetime;

	// 조회용
	private String	observe_method_name;
	private String  org_name;
	private String  river_name;
	private String  observe_type_name;
	private String	currentPage;

	//페이징작업
	private String rn;
	private String search;   	private String keyword;
	private String pageNum;		private int total;
	private int start; 		 	private int end;
}
