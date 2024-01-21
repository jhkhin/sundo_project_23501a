package com.postgres.sample.service.impl.jmh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import com.postgres.sample.dto.AccessLog;
import com.postgres.sample.dto.Code;
import com.postgres.sample.dto.LoginLog;
import com.postgres.sample.dto.Organization;
import com.postgres.sample.dto.UserInfo;
import com.postgres.sample.service.impl.dao.jmh.BoardUserInfoDAO;
import com.postgres.sample.service.impl.dao.jmh.UserInfoDAO;
import com.postgres.sample.service.jmh.UserInfoService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserInfoServiceImpl extends EgovAbstractServiceImpl implements UserInfoService {// implements UserInfoService

	private final UserInfoDAO 		userInfoDAO;
	private final BoardUserInfoDAO 	boardUserInfoDAO;

	//--------------------------------------------------------------------------------------
	//---------로그
	//--------------------------------------------------------------------------------------
	@Override
	public int InsertLoginLog(LoginLog loginLog) {
		System.out.println("UserInfoServiceImpl InsertLoginLog Start..");
		int result = userInfoDAO.JmhInsertLoginLog(loginLog);
		System.out.println("result:"+result);
		return result;
	}

	@Override
	public int InsertAccessLog(AccessLog accessLog) {
		System.out.println("UserInfoServiceImpl InsertAccessLog Start..");
		int result = userInfoDAO.JmhInsertAccessLog(accessLog);
		System.out.println("result:"+result);
		return result;
	}
  
	//--------------------------------------------------------------------------------------	
	//사용자 로그인 체크 1 (ID/PW)
	@Override
	public UserInfo userLoginCheck(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl userLoginCheck Start..");
		//-------------------------------------------------------------
		UserInfo userInfoDTO = userInfoDAO.JmhUserLoginCheck(userInfo);
		//-------------------------------------------------------------
		return userInfoDTO;
	}
	//--------------------------------------------------------------------------------------	
	//사용자 로그인 체크 2 (권한체크)
	@Override
	public UserInfo userLoginSystemCheck(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl userLoginSystemCheck Start..");
		//-------------------------------------------------------------
		UserInfo userInfoDTO = userInfoDAO.JmhUserLoginSystemCheck(userInfo);
		//-------------------------------------------------------------
		return userInfoDTO;
	}
	//--------------------------------------------------------------------------------------	
	//사용자 로그인 체크 3 (사용여부)
	@Override
	public UserInfo userLoginUseFlagCheck(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl userLoginUseFlagCheck Start..");
		//-------------------------------------------------------------
		UserInfo userInfoDTO = userInfoDAO.JmhUserLoginUseFlagCheck(userInfo);
		//-------------------------------------------------------------
		return userInfoDTO;
	}
	//--------------------------------------------------------------------------------------
	//분류 가져오기
	@Override
	public List<Code> codeList(Code code) {
		List<Code> codeList = null;
		System.out.println("UserInfoServiceImpl codeList Start...");
		//-------------------------------------------------
		codeList = userInfoDAO.JmhCodeList(code);
		//-------------------------------------------------
		System.out.println("UserInfoServiceImpl codeList.size()->" +codeList.size());
		
		return codeList;
	}
	//--------------------------------------------------------------------------------------
	//기관명 가져오기
	@Override
	public List<Organization> orgList() {
		List<Organization> orgList = null;
		System.out.println("UserInfoServiceImpl orgList Start...");
		//-------------------------------------------------
		orgList = userInfoDAO.JmhOrgList();
		//-------------------------------------------------
		System.out.println("UserInfoServiceImpl orgList.size()->" +orgList.size());
		
		return orgList;
	}
	//--------------------------------------------------------------------------------------	
	//ID중복체크
	@Override
	public UserInfo idConfirm(String user_id) {
		System.out.println("UserInfoServiceImpl idConfirm Start...");
		//--------------------------------------------------------------
		UserInfo userInfo = userInfoDAO.JmhIdConfirm(user_id);
		//--------------------------------------------------------------		
		return userInfo;
	}
	//--------------------------------------------------------------------------------------
	//회원가입 - 가입하기
	@Override
	public int insertUserInfo(UserInfo userInfo) {
		int result = 0;
		System.out.println("UserInfoServiceImpl insert Start...");
		//-------------------------------------------------
		result = userInfoDAO.JmhInsertUserInfo(userInfo);
		//-------------------------------------------------
		return result;
	}	
	//--------------------------------------------------------------------------------------
	@Override
	public UserInfo userFindId(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl userFindId Start...");
		//------------------------------------------
		UserInfo userInfoDTO = userInfoDAO.userFindId(userInfo);
		//------------------------------------------
		return userInfoDTO;
	}
	//--------------------------------------------------------------------------------------
	@Override
	public int updatePw(Map<String, String> map) {
		System.out.println("UserInfoServiceImpl updatePw Start...");
		//------------------------------------------
		int result = userInfoDAO.updatePw(map);
		//------------------------------------------

		return result;
	}
	//--------------------------------------------------------------------------------------
	@Override
	public int updateUser(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl updateUser Start...");
		//-------------------------------------------------	
		int result = userInfoDAO.JmhUpdateUser(userInfo);
		//-------------------------------------------------	
		return result;
	}
			
	//##################
	// 사용자 관리
	//##################	
	@Override
	public int totalCount(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl totalCount START...");

		int totalCnt = 0;
		
		if(userInfo.getSystem_category() != null) {
			System.out.println("totalCount ★검색 권한---->"+userInfo.getSystem_category());
			if(!userInfo.getSystem_category().equals("")) {
				System.out.println("totalCount ★검색 아이디---->"+userInfo.getUser_id());
				System.out.println("totalCount ★검색 성명---->"+userInfo.getUser_name());
				//검색 건수 가져오기
				//-------------------------------------------------------------------
				totalCnt = boardUserInfoDAO.JmhSearchCount(userInfo);
				//-------------------------------------------------------------------
				System.out.println("UserInfoServiceImpl totalCount totalCnt->" + totalCnt);
				System.out.println("UserInfoServiceImpl totalCount END...");
				return totalCnt;
			}
		}
		//-------------------------------------------------------------------
		totalCnt = boardUserInfoDAO.JmhTotalCount(userInfo);
		//-------------------------------------------------------------------

		System.out.println("UserInfoServiceImpl totalCount totalCnt->" + totalCnt);
		System.out.println("UserInfoServiceImpl totalCount END...");
		return totalCnt;
	}

	//--------------------------------------------------------------------------------------
	@Override
	public List<UserInfo> boardList(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl boardList START...");
		
		List<UserInfo> boardList = null;
		
		if(userInfo.getSystem_category() != null) {
			System.out.println("boardList ★검색 권한---->"+userInfo.getSystem_category());
			if(!userInfo.getSystem_category().equals("")) {
				System.out.println("boardList ★검색 아이디---->"+userInfo.getUser_id());
				System.out.println("boardList ★검색 성명---->"+userInfo.getUser_name());
				//----------------------------------------------------
				boardList = boardUserInfoDAO.JmhSearchList(userInfo);
				//----------------------------------------------------
				System.out.println("UserInfoServiceImpl boardList > searchList END...");
				return boardList;
			}
		}
		System.out.println("userInfo user_id: "+userInfo.getUser_id());
		System.out.println("userInfo start: "+userInfo.getStart());
		System.out.println("userInfo end: "+userInfo.getEnd());
		//---------------------------------------------------
		boardList = boardUserInfoDAO.JmhBoardList(userInfo);
		//---------------------------------------------------
		System.out.println("boardList.size()->"+boardList.size());
		System.out.println("UserInfoServiceImpl boardList END...");
		return boardList;
	}

	//등록
	@Override
	public int insertBoard(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl insertBoard START...");
		int resultCount = 0;		
		Date sysdate = new Date();
		userInfo.setCreate_datetime(sysdate);		
		//----------------------------------------------
		resultCount = boardUserInfoDAO.JmhInsertBoard(userInfo);
		//----------------------------------------------
		System.out.println("UserInfoServiceImpl insertBoard resultCount->"+resultCount);
		System.out.println("UserInfoServiceImpl insertBoard END...");
		return resultCount;
	}

	//조회
	@Override
	public UserInfo selectBoard(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl selectBoard START...");
		UserInfo selectuserInfo = null;		
		//--------------------------------------------------
		selectuserInfo = boardUserInfoDAO.JmhSelectBoard(userInfo);
		//--------------------------------------------------
		System.out.println("UserInfoServiceImpl selectBoard END...");
		return selectuserInfo;
	}

	//수정
	@Override
	public int updateBoard(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl updateBoard START...");
		int resultCount = 0;				
		//----------------------------------------------
		resultCount = boardUserInfoDAO.JmhUpdateBoard(userInfo);
		//----------------------------------------------
		System.out.println("UserInfoServiceImpl updateBoard resultCount->"+resultCount);
		System.out.println("UserInfoServiceImpl updateBoard END...");
		return resultCount;
	}

	//삭제
	@Override
	public int deleteBoard(UserInfo userInfo) {
		System.out.println("UserInfoServiceImpl deleteBoard START...");
		int resultCount = 0;
		//----------------------------------------------
		resultCount = boardUserInfoDAO.JmhDeleteBoard(userInfo);
		//----------------------------------------------
		if(resultCount > 0) {System.out.println("문서 삭제완료:"+resultCount);}		
		System.out.println("UserInfoServiceImpl deleteBoard resultCount->"+resultCount);
		System.out.println("UserInfoServiceImpl deleteBoard END...");
		return resultCount;
	}

}
