package com.postgres.sample.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CheckReport {
	private Integer	 	doc_no;
	private String 		facility_code;
	private String 		check_date;
	private String 		weather;	
	private String 		user_id;	
	private String 		check_result;
	
	private String 		spec_memo;
	private Date 		create_datetime;	
	private Date 		modify_datetime;	


	//조회용
	private Integer 	total;


}
