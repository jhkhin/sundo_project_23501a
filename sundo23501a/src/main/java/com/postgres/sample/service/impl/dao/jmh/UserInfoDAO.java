package com.postgres.sample.service.impl.dao.jmh;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import com.postgres.sample.dto.AccessLog;
import com.postgres.sample.dto.Code;
import com.postgres.sample.dto.LoginLog;
import com.postgres.sample.dto.Organization;
import com.postgres.sample.dto.UserInfo;

@Mapper
public interface UserInfoDAO {
	public UserInfo 			JmhUserLoginCheck(UserInfo userInfo); 	//로그인체크
	public UserInfo 			JmhUserLoginSystemCheck(UserInfo userInfo); //로그인체크+권한체크
	public UserInfo 			JmhUserLoginUseFlagCheck(UserInfo userInfo); //로그인체크+권한체크+사용여부Y체크
	
	public List<Code> 			JmhCodeList(Code code); 				//시스템(권한)
	public List<Organization> 	JmhOrgList();							//기간코드/기관명 정보
	public UserInfo 			JmhIdConfirm(String user_id); 			//ID 중복체크
	public int 					JmhInsertUserInfo(UserInfo userInfo); 	//회원정보 가입하기
	
	public UserInfo 			userFindId(UserInfo userInfo); 			//userFindId
	public int 					updatePw(Map<String, String> map); 		//pwUpdate
	public Organization 		selectOrg(String user_id); 				//selectUserClass
	public int 					JmhUpdateUser(UserInfo userInfo); 			//userInfoUpdate\
	
	public int 					JmhTotalCount(UserInfo userInfo);		//총문서 수
	public int 					JmhSearchCount(UserInfo userInfo);		//검색결과 수
	public List<UserInfo> 		JmhSearchList(UserInfo userInfo);		//검색결과 목록
	public List<UserInfo> 		JmhBoardList(UserInfo userInfo);		//목록
	public int 					JmhInsertBoard(UserInfo userInfo);		//등록
	public UserInfo 			JmhSelectBoard(UserInfo userInfo);		//조회
	public int 					JmhUpdateBoard(UserInfo userInfo);		//수정
	public int 					JmhDeleteBoard(UserInfo userInfo);		//삭제
	public int 					JmhReadCount(UserInfo userInfo); 		//조회수

	public int 					JmhInsertLoginLog(LoginLog loginLog);	//로그인이력
	public int 					JmhInsertAccessLog(AccessLog accessLog);//접속이력

}
