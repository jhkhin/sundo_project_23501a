package com.postgres.sample.controller.jmh;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.postgres.sample.dto.AccessLog;
import com.postgres.sample.dto.BoardNotice;
import com.postgres.sample.dto.Code;
import com.postgres.sample.dto.JmhDaysDataVO;
import com.postgres.sample.dto.LoginLog;
import com.postgres.sample.dto.OrgArea;
import com.postgres.sample.dto.Organization;
import com.postgres.sample.dto.Paging;
import com.postgres.sample.dto.UserInfo;
import com.postgres.sample.service.jmh.JmhAccessLogService;
import com.postgres.sample.service.jmh.JmhLoginLogService;
import com.postgres.sample.service.jmh.JmhOrgService;
import com.postgres.sample.service.jmh.JmhService;
import com.postgres.sample.service.jmh.JmhUserInfoService;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class JmhController {
	
	private static final Logger Logger = LoggerFactory.getLogger(JmhController.class);
	
	private final JmhUserInfoService uiService;		//사용자 관리
	private final JmhOrgService orgService;			//기관 관리
	private final JmhService jmhService;			//공지사항
	private final JmhAccessLogService alogService; 	//접속 이력
	private final JmhLoginLogService llogService; 	//로그인 이력
	
	// SMTP(Send Mail Transport protocol) 메일 전송 객체
	private final  JavaMailSender mailSender;

	// 메인 화면
	@RequestMapping(value = "/main")
	public String main(HttpServletRequest request, Model model) {
		System.out.println("JmhController main Start..");
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/main";
	}
	
	// 일반사용자 기관소개 페이지
	@RequestMapping(value = "/org_intro")
	public String orgIntro(HttpServletRequest request, Model model) {
		
		return "/board_notice/org_intro";
	}
	
	@RequestMapping(value = "/main_header")
	public String mainHeaderPage(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/main_header";
	}

	@RequestMapping(value = "/main_footer")
	public String mainFooterPage(Model model) {
		return "/main_footer";
	}

	//-------------------------------------------------------------------------------
	// 관리자 페이지로 이동
	@RequestMapping(value = "/adminpage_main")
	public String adminPageMain(HttpServletRequest request, Model model) {
		/*System.out.println("JmhController adminPageMain Start..");		// 세션값 받음
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		System.out.println("userInfo.getUser_id() -> " + userInfo.getUser_id());		
		//-----------------------------------------------------------------
		UserInfo userInfoDto = uiService.idConfirm(userInfo.getUser_id());
		//-----------------------------------------------------------------
		model.addAttribute("userInfoDto", userInfoDto);
		insertAccessLog(request, model);
		return "/system1/adminpage_main";*/
		return "redirect:/admin_userinfo_list"; //사용자 관리 바로 이동
	}
	@RequestMapping(value = "/main_header_1")
	public String mainHeader1Page(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/system1/main_header_1";
	}
	@RequestMapping(value = "/adminpage_menu")
	public String mainMenuPage(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		System.out.println("오늘월->" + df.format(date));
		String year_month = df.format(date);
		model.addAttribute("year_month", year_month);
		
		return "/system1/adminpage_menu";
	}
	
	//-------------------------------------------------------------------------------
	// 실시간 수문정보 관리시스템 페이지로 이동
	@RequestMapping(value = "/observation_main")
	public String observationMain(HttpServletRequest request, Model model) {
		/*System.out.println("JmhController adminPageMain Start..");		// 세션값 받음
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		System.out.println("userInfo.getUser_id() -> " + userInfo.getUser_id());		
		//-----------------------------------------------------------------
		UserInfo userInfoDto = uiService.idConfirm(userInfo.getUser_id());
		//-----------------------------------------------------------------
		model.addAttribute("userInfoDto", userInfoDto);		
		insertAccessLog(request, model);
		return "/system2/observation_main";*/
		return "redirect:/realTimeSystem/layer"; // 지도페이지로 바로 이동
	}
	@RequestMapping(value = "/main_header_2")
	public String mainHeader2Page(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/system2/main_header_2";
	}

	@RequestMapping(value = "/main_header_21")
	public String mainHeader21Page(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/system2/main_header_21";
	}


	@RequestMapping(value = "/main_menu_2")
	public String mainMenu2Page(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/system2/main_menu_2";
	}

	//-------------------------------------------------------------------------------
	// 수자원 시설물 관리시스템 페이지로 이동
	@RequestMapping(value = "/water_main")
	public String waterMain(HttpServletRequest request, Model model) {
		/*System.out.println("JmhController adminPageMain Start..");		// 세션값 받음
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		System.out.println("userInfo.getUser_id() -> " + userInfo.getUser_id());		
		//-----------------------------------------------------------------
		UserInfo userInfoDto = uiService.idConfirm(userInfo.getUser_id());
		//-----------------------------------------------------------------
		model.addAttribute("userInfoDto", userInfoDto);		
		insertAccessLog(request, model);
		return "/system3/water_main";*/
		return "redirect:/water_resourcesList"; //점검결과작성 바로 이동
	}
	@RequestMapping(value = "/main_header_3")
	public String mainHeader3Page(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/system3/main_header_3";
	}
	@RequestMapping(value = "/main_menu_3")
	public String mainMenu3Page(HttpServletRequest request, Model model) {
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfo", userInfoDTO);
		return "/system2/main_menu_3";
	}

	
	//#####################
	// 로그인 & 로그아웃
	//#####################
	// 로그인 화면
	@RequestMapping(value = "user_login")
	public String userLogin(Model model) {
		System.out.println("JmhController userLogin Start..");
	
		return "user/user_login";
	}
	//--------------------------------------------------------------------------------------		
	// 인터셉터 체크(preHandler -> Controller)
	@PostMapping(value = "user_login_check")
	public String interCeptor(UserInfo userInfo, HttpServletRequest request, HttpSession session, Model model) {
		System.out.println("JmhController userLoginCheck Start..");
		System.out.println("JmhController userLoginCheck userInfo.getUser_id()->"+userInfo.getUser_id());
		System.out.println("JmhController userLoginCheck userInfo.getUser_pw()->"+userInfo.getUser_pw());
		System.out.println("JmhController userLoginCheck userInfo.getSystem_category()->"+userInfo.getSystem_category());
		// Login 검증
		//---------------------------------------------------------
		UserInfo userInfoDTO = uiService.userLoginCheck(userInfo);
		//---------------------------------------------------------------
		UserInfo userInfoDTO2 = uiService.userLoginSystemCheck(userInfo);
		//---------------------------------------------------------------
		UserInfo userInfoDTO3 = uiService.userLoginUseFlagCheck(userInfo);
		//---------------------------------------------------------------
		
		if(userInfo.getUser_id().equals("") && userInfo.getUser_pw().equals("")) {
			System.out.println("ID, PW 공백");
			model.addAttribute("idMsgBox", "아이디를 입력해주세요.");
			model.addAttribute("pwMsgBox", "비밀번호를 입력해주세요.");
			return "forward:/user_login";
		} else if (userInfo.getUser_id().equals("")) {
			System.out.println("ID 공백");
			model.addAttribute("idMsgBox", "아이디를 입력해주세요.");
			return "forward:/user_login";
		} else if (userInfo.getUser_pw().equals("")) {
			System.out.println("PW 공백");
			model.addAttribute("user_id", userInfo.getUser_id());
			model.addAttribute("pwMsgBox", "비밀번호를 입력해주세요.");
			return "forward:/user_login";
		} else if (userInfoDTO == null) {
			System.out.println("ID or PW 틀림");
			model.addAttribute("pwMsgBox", "아이디 또는 비밀번호를 확인해주세요.");
			return "forward:/user_login";
		} else if (userInfoDTO != null && userInfoDTO2 == null) {
			System.out.println("시스템 권한이 맞지 않음");
			model.addAttribute("pwMsgBox", "권한을 확인해주세요.");
			return "forward:/user_login";
		} else if (userInfoDTO != null && userInfoDTO2 != null && userInfoDTO3 == null) {
			System.out.println("사용여부가 Y가 아님");
			model.addAttribute("pwMsgBox", "사용승인이 필요합니다. 관리자에게 사용여부를 확인해주세요.");
			return "forward:/user_login";
		} else {
			System.out.println("user_login_check userInfo exists");
			
			session.setMaxInactiveInterval(3600); //세션아웃 (1시간)
			session.setAttribute("userInfo", userInfoDTO); // 세션 생성 + 저장
			
			//-------------------------------------------
			//★로그인 이력 남기기
			String ip = request.getRemoteAddr();
			System.out.println("ip:"+ip);
			LoginLog loginLog = new LoginLog();
			loginLog.setIp(ip);
			loginLog.setUser_id(userInfoDTO.getUser_id());
			System.out.println("userInfoDTO.getUser_id():"+userInfoDTO.getUser_id());
			int result = uiService.InsertLoginLog(loginLog);
			System.out.println("result_c:"+result);
			//-------------------------------------------
			
			System.out.println("session.getAttribute(userInfo)->"+session.getAttribute("userInfo"));
			return "redirect:/main";
		}
		
	}
	
	//접속이력 남기는 메소드
	public void insertAccessLog(HttpServletRequest request, Model model) {
		System.out.println("JmhController insertAccessLog Start..");
		//접속이력
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		String requestURI = request.getRequestURI();        //  요청받은 페이지
		String ip = request.getRemoteAddr();
		String user_id = userInfoDTO.getUser_id();
		
		AccessLog accessLog = new AccessLog();
		accessLog.setProgram_url(requestURI);
		accessLog.setIp(ip);
		accessLog.setUser_id(user_id);
		//-------------------------------------------------
		int result = uiService.InsertAccessLog(accessLog);
		//-------------------------------------------------		
		System.out.println("JmhController insertAccessLog End..");
	}
	
	//--------------------------------------------------------------------------------------			
	// 로그아웃
	@RequestMapping(value = "/user_logout")
	public String userLogout(HttpSession session) {
		// 세션 정보 삭제
		session.invalidate();
		return "redirect:/user_login";
	}
	//--------------------------------------------------------------------------------------
	// 아이디 찾기
	@RequestMapping(value = "user_find_id")
	public String userFindId() {
		System.out.println("JmhController userFindId Start..");
		
		return "user/user_find_id";
	}
	//--------------------------------------------------------------------------------------	
	// 아이디 찾기 결과
	@RequestMapping(value = "user_find_id_result")
	public String userFindIdResult(UserInfo userInfo, Model model) {
		System.out.println("JmhController userFindIdResult Start..");
		System.out.println("userInfo.getUser_name()->"+userInfo.getUser_name());
		//System.out.println("userInfo.getUser_number()->"+userInfo.getUser_number());
  		//-----------------------------------------------------
		// user_name과 user_number로 UserInfo 모든 정보 Select
		UserInfo userInfoDto = uiService.userFindId(userInfo);
  		//-----------------------------------------------------

		if(userInfoDto == null) {
			System.out.println("이름과 번호가 다름");
			model.addAttribute("msg", "가입되어 있지 않습니다");
			return "forward:/user_find_id";
		}
		System.out.println("이름과 번호가 같음");
		model.addAttribute("userInfoDto", userInfoDto);
		return "user/user_find_id_result";
		
	}
	//--------------------------------------------------------------------------------------	
	// 비밀번호 찾기
	@RequestMapping(value = "user_find_pw")
	public String userFindPw() {
		System.out.println("JmhController userFindPw Start..");
		
		return "user/user_find_pw";
	}
	//--------------------------------------------------------------------------------------	
	// 비밀번호 찾기 인증
	@ResponseBody
	@RequestMapping(value = "user_find_pw_auth")
	public String userFindPwAuth(String user_id, String auth_email) {
		System.out.println("JmhController userFindPwAuth Start..");
		
		System.out.println("userFindPwAuth userId->"+user_id);
		System.out.println("auth_email->"+auth_email);
		
		//-----------------------------------------------------
		// user_id로 UserInfo 모든 정보 Select (email 가져오기 위해)
		UserInfo userInfo = uiService.idConfirm(user_id);
		//-----------------------------------------------------
		// 입력한 ID가 가입할 때 E-mail과 맞는지 확인		
		if (userInfo != null) {
			System.out.println("ID 존재");
			if(auth_email.equals(userInfo.getUser_email())) {
				System.out.println("이메일 주소가 같음");
				return "1";
			} else if (auth_email.equals("")) {
				System.out.println("이메일 주소 없음");
				return "3";
			} else {
				return "0";
			}
		} else {
			System.out.println("아이디가 존재X");
			return "2";
		}
	}
	//--------------------------------------------------------------------------------------	
	// 새로운 비밀번호 만들기 페이지
	@RequestMapping(value = "user_find_pw_new")
	public String userFindPwNew(String user_id, Model model) {
		System.out.println("JmhController userFindPwNew Start..");
		
		System.out.println("user_id ->"+user_id);
		model.addAttribute("user_id", user_id);
		
		return "user/user_find_pw_new";
	}
	//--------------------------------------------------------------------------------------	
	// 비밀번호 업데이트
	@PostMapping(value = "user_find_pw_update")
	@ResponseBody
	public String userFindPwUpdate(String user_pw, String user_id) {
		System.out.println("JmhController userFindPwNewUpdate Start..");
		System.out.println("user_id ->"+user_id);
		System.out.println("changing PW ->"+user_pw);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", user_id);
		map.put("user_pw", user_pw);
		
		//-----------------------------------------------------
		// user_id와 user_pw 업데이트
		int result = uiService.updatePw(map);
		System.out.println("result->"+result);
		//-----------------------------------------------------

		if(result > 0) {
			return "success"; //ajax호출한 곳에서 분기처리 user_login
		}else {
			return "fail";
		}
	}	
	
	//#####################
	// 회원가입
	//#####################
	// 회원가입 동의 페이지로 이동
	@RequestMapping(value = "user_join_agree")
	public String userJoinAgree() {
		System.out.println("JmhController userJoinAgree Start..");
		return "user/user_join_agree";
	}
	//--------------------------------------------------------------------------------------	
	// 회원가입 페이지로 이동
	@RequestMapping(value = "user_join_write")
	public String userJoinWrite(Model model) {
		System.out.println("JmhController userJoinWrite Start..");
		
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController user_join_write codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);
		
		//-------------------------------------------------
		// organization 모든 정보 가져옴
		List<Organization> orgList = uiService.orgList();
		//-------------------------------------------------
		System.out.println("JmhController user_join_write orgList.size->"+orgList.size());
		model.addAttribute("orgList", orgList);
		
		return "user/user_join_write";
	}
	
	//--------------------------------------------------------------------------------------	
	// 회원가입 ID 중복확인
	@ResponseBody
	@GetMapping(value = "id_confirm")
	public String idConfirm(String user_id, Model model) {
		System.out.println("userId->"+user_id);
		//---------------------------------------------------------
		// 회원가입 ID 중복확인 (PK를 주고 모든 정보 SELECT)
		UserInfo userInfo = uiService.idConfirm(user_id);
		//---------------------------------------------------------

		if (userInfo != null) {
			System.out.println("중복된 ID..");
			return "1";
		} else {
			System.out.println("JmhController confirm 사용 가능한 사번..");
			// 입력한 사번을 중복 확인하고 view로 보내주기 위해 model 사용
			model.addAttribute("user_id", user_id);
			return "2";
		}
	}

	//--------------------------------------------------------------------------------------	
	// 이메일 인증 : 이메일 값 가져옴 + 이메일 전송
	@ResponseBody
	@PostMapping(value = "send_save_mail")
	public String mailCheck(Model model, String auth_email) {
		System.out.println("JmhController mailCheck Start..");
		
		String toMail = auth_email;
		System.out.println("auth_email->"+toMail);
		String setfrom = "jinijuniwa@gmail.com";
		String title = "[ChoongAng] 인증번호 입니다";
		String authNumber = (int) (Math.random() * 999999) + 1 + "";
		try {
			MimeMessage message = mailSender.createMimeMessage();
															// true는 멀티파트 메세지를 사용하겠다는 의미
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(setfrom);
			messageHelper.setTo(toMail);
			messageHelper.setSubject(title); // 생략 가능
			
			messageHelper.setText("이메일 인증번호 입니다: " + authNumber);
			System.out.println("이메일 인증번호 입니다: " + authNumber);
			
			// 첨부파일 보내기 위한 로직
		//	DataSource dataSource = new FileDataSource("c:\\\\log\\\\hwa.png");
		//	messageHelper.addAttachment(MimeUtility.encodeText("ReName.png", "UTF-8", "B"), dataSource);
			mailSender.send(message);
			model.addAttribute("check", 1);  // 정상 전달
			
		} catch (Exception e) {
			System.out.println("mailTransport e.getMessage()->"+e.getMessage());
			model.addAttribute("check", 2);  // 전달 실패
		}  
		return authNumber; 	// 인증번호 돌려줌
	}
	//--------------------------------------------------------------------------------------	
	// 회원가입 정보 insert
	@PostMapping(value = "write_user_info")
	public String writeUserInfo(HttpServletRequest request
							  , @RequestParam(value = "file1", required = false)MultipartFile file1
							  , @ModelAttribute("userInfo") @Valid UserInfo userInfo
							  , BindingResult bindingResult, Model model
							  //, String sample6_postcode		, String sample6_address
							  //, String sample6_detailAddress, String sample6_extraAddress
							   )
								throws IOException {
		System.out.println("JmhController writeUserInfo Start...");
		// Validation 오류시 결과
		if(bindingResult.hasErrors()) {
			System.out.println("JmhController writeUserInfo hasErrors...");
			// 오류 메세지를 띄어주기 위해 forward
			return "forward:user_join_write";
		} else {
			model.addAttribute("userInfo", userInfo);
		}
		/*
		System.out.println("sample6_postcode->"+sample6_postcode);
		System.out.println("sample6_detailAddress->"+sample6_detailAddress);
		// 주소
		if(sample6_postcode.isEmpty() || sample6_address.isEmpty()) {
			System.out.println("주소값 NULL");
			userInfo.setUser_address("");
		} else {
			String user_address = sample6_postcode +"~"+ sample6_address +"~"+ sample6_detailAddress + sample6_extraAddress;
			System.out.println("user_address->"+ user_address);
			userInfo.setUser_address(user_address);
		}
	
		// 이미지파일 업로드
		String attach_path = "upload";	// 파일경로
		// userInfo에 디폴트 이미지와 파일경로 세팅
		userInfo.setAttach_name("user_default.png");
		userInfo.setAttach_path(attach_path);
		*/
		System.out.println("userinfo: user_id="+userInfo.getUser_id());
		System.out.println("userinfo: user_pw="+userInfo.getUser_pw());
		System.out.println("userinfo: system_category="+userInfo.getSystem_category());
		System.out.println("userinfo: user_name="+userInfo.getUser_name());
		System.out.println("userinfo: user_email="+userInfo.getUser_email());
		System.out.println("userinfo: user_tel="+userInfo.getUser_tel());
		System.out.println("userinfo: org_code="+userInfo.getOrg_code());
		System.out.println("userinfo: org_department="+userInfo.getUser_department());
		System.out.println("userinfo: org_position="+userInfo.getUser_position());
		//-------------------------------------------------
		// 회원가입 정보 Insert
		int result = uiService.insertUserInfo(userInfo);
		//-------------------------------------------------

		if(result > 0) {
			System.out.println("가입완료");
			return "/user/user_login";
		} else {
			System.out.println("가입실패");
			return "redirect:user_join_write";
		}
	}

	
	//#####################
	// 사용자 관리 페이지
	//#####################
	// 사용자 관리
	///admin_userinfo_list
	@GetMapping("/admin_userinfo_list")
	public String adminUserInfoList(UserInfo userInfo, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----사용자 관리 목록(/admin_userinfo_list) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		// 사용자 관리 목록에 표시되는 문서 건수 Count
		//-------------------------------------------------
		int totalCount = uiService.totalCount(userInfo);
		//-------------------------------------------------
		
		//Page 작업
		Paging 	page = new Paging(totalCount, currentPage);
		
		//Parameter emp --> Page만 추가 Setting
		userInfo.setStart(page.getStart()); 	//시작시 1
		userInfo.setEnd(page.getEnd());		//시작시 15
		
		//--------------------------------------------------------------
		List<UserInfo> boardList = uiService.boardList(userInfo);
		//--------------------------------------------------------------
		
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController admin_userinfo_list codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);
				

		model.addAttribute("boardList", boardList); 
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("page", page);
		model.addAttribute("system_category", userInfo.getSystem_category()); 	//권한
		model.addAttribute("user_id", userInfo.getUser_id()); 					//아이디
		model.addAttribute("user_name", userInfo.getUser_name()); 				//성명
		
		System.out.println("----사용자 관리 목록(admin_userinfo_list) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_userinfo/admin_userinfo_list";
	}
	
	//사용자 관리 - 작성화면
	@RequestMapping(value = "admin_userinfo_write")
	public String adminUserInfoWrite(Model model, HttpServletRequest request) {
		System.out.println("-----사용자 관리 작성(admin_userinfo_write) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		//오늘날짜
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("오늘날짜->" + df.format(date));
		String todayDate = df.format(date);
				
		model.addAttribute("subject", "");	
		model.addAttribute("todayDate", todayDate); //작성일
		model.addAttribute("userInfoDTO", userInfoDTO); //로그인사용자 정보
		
		System.out.println("-----사용자 관리 작성(admin_userinfo_write) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_userinfo/admin_userinfo_write";
	}
			
	//사용자 관리 - 조회화면
	@RequestMapping(value = "admin_userinfo_read")
	public String adminUserInfoRead(UserInfo userInfo, HttpServletRequest request, Model model) {
		System.out.println("-----사용자 관리 조회(admin_userinfo_read) START-----");

		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");

		System.out.println("user_id->"+userInfo.getUser_id());
	
		//------------------------------------------------------------
		UserInfo selectUserInfo = uiService.selectBoard(userInfo);
		//------------------------------------------------------------
		System.out.println("user_name->"+selectUserInfo.getUser_name());		

	
		model.addAttribute("userInfoDTO", userInfoDTO); //로그인사용자 정보
		model.addAttribute("board", selectUserInfo);
		
		System.out.println("-----사용자 관리 조회(admin_userinfo_read) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_userinfo/admin_userinfo_read";
	}

	//수정화면 열기
	@RequestMapping(value = "admin_userinfo_edit")
	public String adminUserInfoEdit(UserInfo userInfo, HttpServletRequest request, Model model) {
		System.out.println("-----사용자 관리 수정화면 열기(admin_userinfo_edit) START-----");
		
		System.out.println("user_id->"+userInfo.getUser_id());
		
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController admin_userinfo_edit codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);
		
		//-------------------------------------------------
		// organization 모든 정보 가져옴
		List<Organization> orgList = uiService.orgList();
		//-------------------------------------------------
		System.out.println("JmhController admin_userinfo_edit orgList.size->"+orgList.size());
		model.addAttribute("orgList", orgList);
				
		//------------------------------------------------------------
		UserInfo selectUserInfo = uiService.selectBoard(userInfo);
		//------------------------------------------------------------
		System.out.println("user_name->"+selectUserInfo.getUser_name());	
		
		model.addAttribute("userInfo", selectUserInfo);
		
		System.out.println("-----사용자 관리 수정화면 열기(admin_userinfo_edit) END-----");
		insertAccessLog(request, model);

		return "/system1/admin_userinfo/admin_userinfo_edit";
	}
	
	//수정 저장
	@RequestMapping(value = "admin_userinfo_update", method = RequestMethod.POST)
	public String adminUserInfoUpdate(@ModelAttribute("userInfo") @Valid UserInfo userInfo
									, BindingResult bindingResult
									, HttpServletRequest request
									, Model model) throws IOException {
		
		System.out.println("-----사용자 관리 수정(admin_userinfo_update) START-----");

		System.out.println("JmhController adminUserInfoUpdate Start..");
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");

		if(bindingResult.hasErrors()) {
			System.out.println("validation 에러 발생"+bindingResult.getFieldErrorCount()+bindingResult.toString());
			
			model.addAttribute("msg", "fail");
						
			return "/system1/admin_userinfo/admin_userinfo_edit";
		}
		
		//--------------------------------------------------
		int resultCount = uiService.updateBoard(userInfo);
		//--------------------------------------------------
		if(resultCount > 0) {			
			model.addAttribute("action", "update"); //수행(수정)
			model.addAttribute("status", "success"); //상태(성공)
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("redirect", "admin_userinfo_list"); //목록으로 이동
		}else {
			model.addAttribute("action", "update"); //수행(수정)
			model.addAttribute("status", "error"); //수행실패
		}

		System.out.println("-----사용자 관리 수정(admin_userinfo_update) END-----");
		insertAccessLog(request, model);
		
		return "forward:/submit_control";
	}
	
	//문서 삭제
	@ResponseBody
	@RequestMapping(value = "admin_userinfo_delete")
	public String adminUserInfoDelete(UserInfo userInfo, HttpServletRequest request, Model model) throws IOException, Exception {
		
		System.out.println("-----사용자 관리 삭제(admin_userinfo_delete) START-----");
		
		//--------------------------------------------------
		int resultCount = uiService.deleteBoard(userInfo);
		//--------------------------------------------------
		System.out.println("JmhController resultCount->"+resultCount);
		if(resultCount > 0) {
		}else {
			return "error";
		}
		
		System.out.println("-----사용자 관리 삭제(admin_userinfo_delete) END-----");
		insertAccessLog(request, model);
		
		return "success";
	}
	
	//==============================================================
	// 기관 관리
	///admin_org_list
	@GetMapping("/admin_org_list")
	public String adminOrgList(Organization orgInfo, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----기관 관리 목록(/admin_org_list) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		// 기관 관리 목록에 표시되는 문서 건수 Count
		//-------------------------------------------------
		int totalCount = orgService.totalCount(orgInfo);
		//-------------------------------------------------
		
		//Page 작업
		Paging 	page = new Paging(totalCount, currentPage);
		
		//Parameter emp --> Page만 추가 Setting
		orgInfo.setStart(page.getStart()); 	//시작시 1
		orgInfo.setEnd(page.getEnd());		//시작시 15
		
		//-----------------------------------------------------------
		List<Organization> boardList = orgService.boardList(orgInfo);
		//-----------------------------------------------------------
		
		//---------------------------------------------------
		// 행정구역 정보 가져옴
		List<OrgArea> orgAreaList = orgService.orgAreaList();
		//---------------------------------------------------
		System.out.println("JmhController admin_org_list orgAreaList.size->"+orgAreaList.size());
		model.addAttribute("orgAreaList", orgAreaList);

		model.addAttribute("boardList", boardList); 
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("page", page);
		model.addAttribute("org_area", orgInfo.getOrg_area()); 	//행정구역
		model.addAttribute("org_name", orgInfo.getOrg_name()); 	//기관 명칭
		
		System.out.println("----기관 관리 목록(admin_org_list) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_org/admin_org_list";
	}
	
	//기관 관리 - 작성화면
	@RequestMapping(value = "admin_org_write")
	public String adminOrgWrite(Model model, HttpServletRequest request) {
		System.out.println("-----기관 관리 작성(admin_org_write) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		//---------------------------------------------------
		// 행정구역 정보 가져옴
		List<OrgArea> orgAreaList = orgService.orgAreaList();
		//---------------------------------------------------
		System.out.println("JmhController admin_org_list orgAreaList.size->"+orgAreaList.size());
		model.addAttribute("orgAreaList", orgAreaList);
		
		

		model.addAttribute("userInfoDTO", userInfoDTO); //로그인사용자 정보
		
		System.out.println("-----기관 관리 작성(admin_org_write) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_org/admin_org_write";
	}
	
	//신규등록
	@RequestMapping(value = "admin_org_insert", method = RequestMethod.POST)
	public String adminOrgInsert(Organization orgInfo, HttpServletRequest request, Model model)	throws IOException, Exception {
		
		System.out.println("-----기관 관리 등록(admin_org_insert) START-----");
				
		//----------------------------------------------------
		int resultCount = orgService.insertBoard(orgInfo);
		//----------------------------------------------------
		
		model.addAttribute("action", "insert"); //수행(작성)
		model.addAttribute("status", "success"); //상태(성공)
		model.addAttribute("orgInfo", orgInfo);
		model.addAttribute("redirect", "admin_org_list"); //목록으로 이동
 
		System.out.println("-----기관 관리 등록(admin_org_insert) END-----");
		insertAccessLog(request, model);
		
		return "forward:/submit_control";
	}
	
	//기관 관리 - 조회화면
	@RequestMapping(value = "admin_org_read")
	public String adminOrgRead(Organization orgInfo, HttpServletRequest request, Model model) {
		System.out.println("-----기관 관리 조회(admin_org_read) START-----");

		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");

		System.out.println("org_code->"+orgInfo.getOrg_code());
	
		//------------------------------------------------------------
		Organization selectOrganization = orgService.selectBoard(orgInfo);
		//------------------------------------------------------------
		System.out.println("org_name->"+selectOrganization.getOrg_name());		

	
		model.addAttribute("userInfo", userInfoDTO); //로그인사용자 정보
		model.addAttribute("board", selectOrganization);
		
		System.out.println("-----기관 관리 조회(admin_org_read) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_org/admin_org_read";
	}

	//수정화면 열기
	@RequestMapping(value = "admin_org_edit")
	public String adminOrgEdit(Organization orgInfo, HttpServletRequest request, Model model) {
		System.out.println("-----기관 관리 수정화면 열기(admin_org_edit) START-----");
		
		System.out.println("org_code->"+orgInfo.getOrg_code());
		
		//---------------------------------------------------
		// 행정구역 정보 가져옴
		List<OrgArea> orgAreaList = orgService.orgAreaList();
		//---------------------------------------------------
		System.out.println("JmhController admin_org_list orgAreaList.size->"+orgAreaList.size());
		model.addAttribute("orgAreaList", orgAreaList);
		
		//----------------------------------------------------------------
		Organization selectOrganization = orgService.selectBoard(orgInfo);
		//----------------------------------------------------------------
		System.out.println("org_name->"+selectOrganization.getOrg_name());	
		
		model.addAttribute("board", selectOrganization);
		
		System.out.println("-----기관 관리 수정화면 열기(admin_org_edit) END-----");
		insertAccessLog(request, model);

		return "/system1/admin_org/admin_org_edit";
	}
	
	//수정 저장
	@RequestMapping(value = "admin_org_update", method = RequestMethod.POST)
	public String adminOrgUpdate(@ModelAttribute("orgInfo") @Valid Organization orgInfo
									, BindingResult bindingResult
									, HttpServletRequest request
									, Model model) throws IOException {
		
		System.out.println("-----기관 관리 수정(admin_org_update) START-----");

		System.out.println("JmhController adminOrgUpdate Start..");
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");

		if(bindingResult.hasErrors()) {
			System.out.println("validation 에러 발생"+bindingResult.getFieldErrorCount()+bindingResult.toString());
			
			model.addAttribute("msg", "fail");
						
			return "/system1/admin_userinfo/admin_org_edit";
		}
		
		//--------------------------------------------------
		int resultCount = orgService.updateBoard(orgInfo);
		//--------------------------------------------------
		if(resultCount > 0) {			
			model.addAttribute("action", "update"); //수행(수정)
			model.addAttribute("status", "success"); //상태(성공)
			model.addAttribute("userInfo", orgInfo);
			model.addAttribute("redirect", "admin_org_list"); //목록으로 이동
		}else {
			model.addAttribute("action", "update"); //수행(수정)
			model.addAttribute("status", "error"); //수행실패
		}

		System.out.println("-----기관 관리 수정(admin_org_update) END-----");
		insertAccessLog(request, model);
		
		return "forward:/submit_control";
	}
	
	//문서 삭제
	@ResponseBody
	@RequestMapping(value = "admin_org_delete")
	public String adminOrgDelete(Organization orgInfo, HttpServletRequest request, Model model) throws IOException, Exception {
		
		System.out.println("-----기관 관리 삭제(admin_org_delete) START-----");
		
		System.out.println("org_code->"+orgInfo.getOrg_code());
		//--------------------------------------------------
		int resultCount = orgService.deleteBoard(orgInfo);
		//--------------------------------------------------
		System.out.println("JmhController resultCount->"+resultCount);
		if(resultCount > 0) {
		}else {
			return "error";
		}
		
		System.out.println("-----기관 관리 삭제(admin_org_delete) END-----");
		insertAccessLog(request, model);
		
		return "success";
	}
	
	

	
	//#####################
	// 일반사용자 공지사항 페이지
	//#####################
	//board_notice_list
	@GetMapping("/board_notice_list")
	public String boardNoticeList(BoardNotice boardNotice, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----공지사항 목록(/admin_notice) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		// 공지사항 목록에 표시되는 문서 건수 Count
		//-------------------------------------------------
		int totalCount = jmhService.totalCount(boardNotice);
		//-------------------------------------------------
		
		//Page 작업
		Paging 	page = new Paging(totalCount, currentPage);
		
		//Parameter emp --> Page만 추가 Setting
		boardNotice.setStart(page.getStart()); 	//시작시 1
		boardNotice.setEnd(page.getEnd());		//시작시 15
		
		//--------------------------------------------------------------
		List<BoardNotice> boardList = jmhService.boardList(boardNotice);
		//--------------------------------------------------------------
		
		//검색 분류코드 가져오기
		Code code = new Code();
		code.setField_name("search_category");
		//-----------------------------------------------------
		List<Code> search_codelist = jmhService.codeList(code);
		//-----------------------------------------------------

		model.addAttribute("boardList", boardList); 
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("page", page);
		model.addAttribute("search", boardNotice.getSearch()); 		//검색필드
		model.addAttribute("keyword", boardNotice.getKeyword()); 	//검색어
		model.addAttribute("search_codelist", search_codelist); 	//검색 분류
		
		System.out.println("----공지사항 목록(admin_notice_list) END-----");
		
		return "/board_notice/board_notice_list";
	}
	//일반사용자 공지사항 게시판 - 조회화면
	@RequestMapping(value = "board_notice_read")
	public String boardNoticeRead(BoardNotice boardNotice, HttpServletRequest request, Model model) {
		System.out.println("-----공지사항 게시판 조회(board_notice_read) START-----");

		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");

		System.out.println("doc_no->"+boardNotice.getDoc_no());

		//-----------------------------------------------------------
		int resultCount = jmhService.readCount(boardNotice); //조회수 증가
		//-----------------------------------------------------------
				
		//------------------------------------------------------------
		BoardNotice selectBoardNotice = jmhService.selectBoard(boardNotice);
		//------------------------------------------------------------
		System.out.println("subject->"+selectBoardNotice.getSubject());		

	
		model.addAttribute("userInfoDTO", userInfoDTO); //로그인사용자 정보
		model.addAttribute("board", selectBoardNotice);
		
		System.out.println("-----공지사항 게시판 조회(board_notice_read) END-----");
		
		return "/board_notice/board_notice_read";
	}
	
	//#####################
	// 관리자 공지사항 페이지
	//#####################
	///admin_notice_list
	@GetMapping("/admin_notice_list")
	public String adminBoardNoticeList(BoardNotice boardNotice, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----공지사항 목록(/admin_notice) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		// 공지사항 목록에 표시되는 문서 건수 Count
		//-------------------------------------------------
		int totalCount = jmhService.totalCount(boardNotice);
		//-------------------------------------------------
		
		//Page 작업
		Paging 	page = new Paging(totalCount, currentPage);
		
		//Parameter emp --> Page만 추가 Setting
		boardNotice.setStart(page.getStart()); 	//시작시 1
		boardNotice.setEnd(page.getEnd());		//시작시 15
		
		//--------------------------------------------------------------
		List<BoardNotice> boardList = jmhService.boardList(boardNotice);
		//--------------------------------------------------------------
		
		//검색 분류코드 가져오기
		Code code = new Code();
		code.setField_name("search_category");
		//-----------------------------------------------------
		List<Code> search_codelist = jmhService.codeList(code);
		//-----------------------------------------------------

		model.addAttribute("boardList", boardList); 
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("page", page);
		model.addAttribute("search", boardNotice.getSearch()); 		//검색필드
		model.addAttribute("keyword", boardNotice.getKeyword()); 	//검색어
		model.addAttribute("search_codelist", search_codelist); 	//검색 분류
		
		System.out.println("----공지사항 목록(admin_notice_list) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_notice/admin_notice_list";
	}
	
	//공지사항 - 작성화면
	@RequestMapping(value = "admin_notice_write")
	public String adminBoardNoticeWrite(Model model, HttpServletRequest request) {
		System.out.println("-----공지사항 게시판 작성(admin_notice_write) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		//오늘날짜
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("오늘날짜->" + df.format(date));
		String todayDate = df.format(date);
				
		model.addAttribute("subject", "");	
		model.addAttribute("todayDate", todayDate); //작성일
		model.addAttribute("userInfoDTO", userInfoDTO); //로그인사용자 정보
		
		System.out.println("-----공지사항 게시판 작성(admin_notice_write) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_notice/admin_notice_write";
	}
	
	//신규등록
	@RequestMapping(value = "admin_notice_insert", method = RequestMethod.POST)
	public String adminBoardNoticeInsert(BoardNotice boardNotice, HttpServletRequest request, MultipartFile file1, Model model)	throws IOException, Exception {
		
		System.out.println("-----공지사항 게시판 등록(admin_notice_insert) START-----");
		
		if(file1.getSize() > 0) {
			//Servlet 상속 받지 못했을 때 realPath 불러오는 방법		
			String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
			
			log.info("uploadPath: " 	+ uploadPath);
			log.info("originalName:"	+ file1.getOriginalFilename());
			log.info("size: " 			+ file1.getSize());
			log.info("contentType: " 	+ file1.getContentType());
			
			//---------------------------------------------------------------------------------------
			String savedName = uploadFile(file1.getOriginalFilename(), file1.getBytes(), uploadPath);
			log.info("savedName: " 	+ savedName);
			//---------------------------------------------------------------------------------------
			
			// Service --> DB CRUD
			boardNotice.setTbl_name("BOARD_NOTICE");
			boardNotice.setAttach_name(file1.getOriginalFilename()); 
			boardNotice.setAttach_path("/upload");
			boardNotice.setAttach_saved_name(savedName);			
		}
		
		//----------------------------------------------------
		int resultCount = jmhService.insertBoard(boardNotice);
		//----------------------------------------------------
		System.out.println("$$$$"+resultCount);
		if(resultCount > 0) {
			if(file1.getSize() > 0) {
				//don_no받아서 파일첨부 등록
				boardNotice.setDoc_no(resultCount);
				//-----------------------------------------------------------
				int resultAttachCount = jmhService.insertAttach(boardNotice);
				//-----------------------------------------------------------
			}
		}
		model.addAttribute("action", "insert"); //수행(작성)
		model.addAttribute("status", "success"); //상태(성공)
		model.addAttribute("boardNotice", boardNotice);
		model.addAttribute("redirect", "admin_notice_list"); //목록으로 이동
 
		System.out.println("-----공지사항 게시판 등록(admin_notice_insert) END-----");
		insertAccessLog(request, model);
		
		return "forward:/submit_control";
	}

	//내부 메소드 private으로
	private String uploadFile(String originalName, byte[] fileData, String uploadPath) throws IOException {
		
		// Universally Unique Identified (UUID) 유일한 식별자
		UUID uid = UUID.randomUUID();
		
		// requestPath = requestPath + "/resources/image";
		System.out.println("uploadPath->"+uploadPath);
		
		//Directory생성
		File fileDirectory = new File(uploadPath);
		
		if(!fileDirectory.exists()) {
			//신규 폴더(Directory) 생성 : 폴더가 없으면 새로 자동 생성해줌.
			fileDirectory.mkdirs();
			System.out.println("업로드용 폴더 생성: "+uploadPath);
		}
		
		String savedName = uid.toString() + "_" + originalName;
		
		log.info("savedName: "+savedName);
		
		File target = new File(uploadPath, savedName);
		
		// File target = new File(requestPath, savedName);
		// File UpLoad ----> uploadPath / UUID+_+originalName
		
		//실제 업로드 순간
		FileCopyUtils.copy(fileData, target); //org.springframework.util.FileCopyUtils
			
		return savedName;
	}
	
	@RequestMapping(value="uploadFileDelete", method = RequestMethod.GET)
	public String uploadFileDelete(String attach_path, HttpServletRequest request, Model model) 
			throws Exception {
		
		String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
		String deleteFile = uploadPath + attach_path;
		
		log.info("deleteFile: " + deleteFile);
		System.out.println("uploadFileDelete GET Start");
		
		int delResult = upFileDelete(deleteFile);
		
		log.info("deleteFile result-> "+delResult);
		
		model.addAttribute("deleteFile", deleteFile);
		model.addAttribute("delResult", delResult);
		
		return "uploadResult";
		
	}

	private int upFileDelete(String deleteFileName) throws Exception {
		int result = 0;
		
		log.info("upFileDelete result-> " + deleteFileName);
		
		File file = new File(deleteFileName);
		
		if(file.exists()) {
			
			if(file.delete()) {
				
				System.out.println("파일삭제 성공");
				result = 1;
			}
			else {
				System.out.println("파일삭제 실패");
				result = 0;
			}
		}
		else {
			System.out.println("삭제할 파일이 존재하지 않습니다.");
			result = -1;
		}
		return result;
	}
		
	//공지사항 게시판 - 조회화면
	@RequestMapping(value = "admin_notice_read")
	public String adminBoardNoticeRead(BoardNotice boardNotice, HttpServletRequest request, Model model) {
		System.out.println("-----공지사항 게시판 조회(admin_notice_read) START-----");

		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");

		System.out.println("doc_no->"+boardNotice.getDoc_no());

		//-------------------------------------------------------------
		int resultCount = jmhService.readCount(boardNotice); //조회수 증가
		//-------------------------------------------------------------
				
		//------------------------------------------------------------
		BoardNotice selectBoardNotice = jmhService.selectBoard(boardNotice);
		//------------------------------------------------------------
		System.out.println("subject->"+selectBoardNotice.getSubject());		

	
		model.addAttribute("userInfoDTO", userInfoDTO); //로그인사용자 정보
		model.addAttribute("board", selectBoardNotice);
		
		System.out.println("-----공지사항 게시판 조회(admin_notice_read) END-----");
		insertAccessLog(request, model);
		
		return "/system1/admin_notice/admin_notice_read";
	}

	//수정화면 열기
	@RequestMapping(value = "admin_notice_edit")
	public String adminBoardNoticeEdit(BoardNotice boardNotice, HttpServletRequest request, Model model) {
		System.out.println("-----공지사항 게시판 수정화면 열기(admin_notice_edit) START-----");
		
		System.out.println("doc_no->"+boardNotice.getDoc_no());
		
		//------------------------------------------------------------------
		BoardNotice selectBoardNotice = jmhService.selectBoard(boardNotice);
		//------------------------------------------------------------------
		System.out.println("subject->"+selectBoardNotice.getSubject());
		
		model.addAttribute("board", selectBoardNotice);
		
		System.out.println("-----공지사항 게시판 수정화면 열기(admin_notice_edit) END-----");
		insertAccessLog(request, model);

		return "/system1/admin_notice/admin_notice_edit";
	}
	
	//수정 저장
	@RequestMapping(value = "admin_notice_update", method = RequestMethod.POST)
	public String adminBoardNoticeUpdate(BoardNotice boardNotice, HttpServletRequest request, MultipartFile file1, Model model)	throws IOException, Exception {
		
		System.out.println("-----공지사항 게시판 수정(admin_notice_update) START-----");
		
		//Servlet 상속 받지 못했을 때 realPath 불러오는 방법		
		String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");

		String before_attach_delete_flag = boardNotice.getAttach_delete_flag();
		String before_attach_path = boardNotice.getAttach_path();
		String before_attach_saved_name = boardNotice.getAttach_saved_name();
		
		System.out.println("before_attach_delete_flag->"+before_attach_delete_flag);
		System.out.println("before_attach_path->"+before_attach_path);
		System.out.println("before_attach_saved_name->"+before_attach_saved_name);
		
		if(before_attach_delete_flag.equals("D") && !before_attach_path.isEmpty()) {
			//기존 첨부파일이 있고 삭제플래그가 D인 경우 삭제하기 -> 문서정보 업데이트 후 삭제
			boardNotice.setAttach_name("");
			boardNotice.setAttach_path("");
			boardNotice.setAttach_saved_name("");
		}
		
		if(file1.getSize() > 0) {			
			
			System.out.println("FileUpload START...");
			log.info("uploadPath: " 	+ uploadPath);
			log.info("originalName:"	+ file1.getOriginalFilename());
			log.info("size: " 			+ file1.getSize());
			log.info("contentType: " 	+ file1.getContentType());
			
			//---------------------------------------------------------------------------------------
			String savedName = uploadFile(file1.getOriginalFilename(), file1.getBytes(), uploadPath);
			log.info("savedName: " 	+ savedName);
			//---------------------------------------------------------------------------------------
			
			// Service --> DB CRUD
			boardNotice.setTbl_name("BOARD_NOTICE");
			boardNotice.setAttach_name(file1.getOriginalFilename()); 
			boardNotice.setAttach_path("/upload");
			boardNotice.setAttach_saved_name(savedName);	
		}
		
		//--------------------------------------------------
		int resultCount = jmhService.updateBoard(boardNotice);
		//--------------------------------------------------
		if(resultCount > 0) {
			if(before_attach_delete_flag.equals("D") && !before_attach_saved_name.isEmpty()) {
			
				//수정이 정상수행 되었을때 기존파일 삭제처리
				String deleteFile = uploadPath + before_attach_saved_name;
				
				log.info("deleteFile: " + deleteFile);
				
				//---------------------------------------
				int delResult = upFileDelete(deleteFile);
				//---------------------------------------
				//-----------------------------------------------------------
				int resultAttachCount = jmhService.deleteAttach(boardNotice);
				//-----------------------------------------------------------
			}
			if(file1.getSize() > 0) {
				//don_no받아서 파일첨부 등록
				//-----------------------------------------------------------
				int resultAttachCount = jmhService.insertAttach(boardNotice);
				//-----------------------------------------------------------
			}
			model.addAttribute("action", "update"); //수행(수정)
			model.addAttribute("status", "success"); //상태(성공)
			model.addAttribute("boardNotice", boardNotice);
			model.addAttribute("redirect", "admin_notice_list"); //목록으로 이동
		}else {
			model.addAttribute("action", "update"); //수행(수정)
			model.addAttribute("status", "error"); //수행실패
		}
		
		System.out.println("-----공지사항 게시판 수정(admin_notice_update) END-----");
		insertAccessLog(request, model);
		
		return "forward:/submit_control";
	}
	
	//Submit후 VIEW 조건부 제어
	@RequestMapping(value = "/submit_control")
	public String mainPage(Model model) {
		return "submit_control";
	}
	
	//문서 삭제
	@ResponseBody
	@RequestMapping(value = "admin_notice_delete")
	public String adminBoardNoticeDelete(BoardNotice boardNotice, HttpServletRequest request, Model model) throws IOException, Exception {
		
		System.out.println("-----공지사항 게시판 삭제(admin_notice_delete) START-----");
		
		//Servlet 상속 받지 못했을 때 realPath 불러오는 방법		
		String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
		String before_attach_path = boardNotice.getAttach_path();
		String before_attach_saved_name = boardNotice.getAttach_saved_name();
		
		boardNotice.setTbl_name("BOARD_NOTICE");
		
		//--------------------------------------------------
		int resultCount = jmhService.deleteBoard(boardNotice);
		//--------------------------------------------------
		System.out.println("JmhController resultCount->"+resultCount);
		if(resultCount > 0) {
			if(!before_attach_saved_name.isEmpty()) {
				//문서삭제가 정상수행 되었을때 첨부파일 삭제처리
				String deleteFile = uploadPath + before_attach_saved_name;
				
				log.info("deleteFile: " + deleteFile);
				
				//---------------------------------------
				int delResult = upFileDelete(deleteFile);
				//---------------------------------------
				//-----------------------------------------------------------
				int resultAttachCount = jmhService.deleteAttach(boardNotice);
				//-----------------------------------------------------------
			}
		}else {
			return "error";
		}
		
		System.out.println("-----공지사항 게시판 삭제(admin_notice_delete) END-----");
		insertAccessLog(request, model);
		
		return "success";
	}
	
	
	//#####################
	// 시스템 관리 페이지
	//#####################
	//1. 접속 통계
	@ResponseBody
	@GetMapping("/system_log_list")
	public ModelAndView systemLogList(AccessLog accessLog, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----접속 통계 목록(/systemLogList) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController systemLogList codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);
		
		List<JmhDaysDataVO> boardList = null;
		String year_month = accessLog.getYear_month();
		System.out.println("year_month:"+year_month);
		if(year_month != null) {
			//---------------------------------------------------
			boardList = alogService.searchMonthList(year_month);
			//---------------------------------------------------
			System.out.println("접속통계 boardList.size="+boardList.size());
		}else {
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			System.out.println("오늘월->" + df.format(date));
			year_month = df.format(date);
			accessLog.setYear_month(year_month);
		}
		/*
		model.addAttribute("year_month", accessLog.getYear_month());
		model.addAttribute("boardList", boardList);
		System.out.println("----접속 통계 목록(systemLogList) END-----");
		insertAccessLog(request, model);
		
		return "/system1/log/system_log/system_log_list";
		*/
		
		ModelAndView mv = new ModelAndView();
	    mv.setViewName("/system1/log/system_log/system_log_list"); // 뷰의 이름
	    mv.addObject("year_month", accessLog.getYear_month()); //
	    mv.addObject("boardList", boardList); //
	    
		System.out.println("----접속 통계 목록(systemLogList) END-----");
		insertAccessLog(request, model);

		return mv;
		
	}
	
	//1-2. 접속 통계(검색)
	@ResponseBody
	@GetMapping("/system_log_search")
	public List<JmhDaysDataVO> systemLogSearch(AccessLog accessLog, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----접속 통계 검색(/systemLogSearch) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController systemLogList codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);
		
		List<JmhDaysDataVO> boardList = null;
		String year_month = accessLog.getYear_month();
		System.out.println("year_month:"+year_month);
		if(year_month != null) {
			//---------------------------------------------------
			boardList = alogService.searchMonthList(year_month);
			//---------------------------------------------------
			System.out.println("접속통계 boardList.size="+boardList.size());
		}else {
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			System.out.println("오늘월->" + df.format(date));
			year_month = df.format(date);
			accessLog.setYear_month(year_month);
		}
		
		System.out.println("----접속 통계 검색(systemLogSearch) END-----");
		insertAccessLog(request, model);

		return boardList;
		
	}
	
	//2. 접속 이력
	@GetMapping("/access_log_list")
	public String accessLogList(AccessLog accessLog, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----접속 이력 목록(/accessLogList) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		// 로그인 이력 목록에 표시되는 문서 건수 Count
		//-------------------------------------------------
		int totalCount = alogService.totalCount(accessLog);
		//-------------------------------------------------
		
		//Page 작업
		Paging 	page = new Paging(totalCount, currentPage);
		
		//Parameter --> Page만 추가 Setting
		accessLog.setStart(page.getStart());
		accessLog.setEnd(page.getEnd());
		
		//--------------------------------------------------------------
		List<AccessLog> boardList = alogService.boardList(accessLog);
		//--------------------------------------------------------------
		
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController loginLogList codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);

		model.addAttribute("boardList", boardList); 
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("page", page);

		model.addAttribute("from_date", accessLog.getFrom_date()); 	//기간from
		model.addAttribute("to_date", accessLog.getTo_date()); 		//기간to
		model.addAttribute("user_name", accessLog.getUser_name()); 	//사용자
		model.addAttribute("system_category", accessLog.getSystem_category()); //권한(시스템)
		model.addAttribute("ip", accessLog.getIp()); //ip
		
		System.out.println("----접속 이력 목록(accessLogList) END-----");
		insertAccessLog(request, model);
		
		return "/system1/log/access_log/access_log_list";
	}
	
	//3. 로그인 이력
	@GetMapping("/login_log_list")
	public String loginLogList(LoginLog loginLog, String currentPage, HttpServletRequest request, Model model) {
		System.out.println("-----로그인 이력 목록(/loginLogList) START-----");
		
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		// 로그인 이력 목록에 표시되는 문서 건수 Count
		//-------------------------------------------------
		int totalCount = llogService.totalCount(loginLog);
		//-------------------------------------------------
		
		//Page 작업
		Paging 	page = new Paging(totalCount, currentPage);
		
		//Parameter --> Page만 추가 Setting
		loginLog.setStart(page.getStart());
		loginLog.setEnd(page.getEnd());	
		
		//--------------------------------------------------------------
		List<LoginLog> boardList = llogService.boardList(loginLog);
		//--------------------------------------------------------------
		
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController loginLogList codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);

		model.addAttribute("boardList", boardList); 
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("page", page);
		
		model.addAttribute("from_date", loginLog.getFrom_date()); 	//기간from
		model.addAttribute("to_date", loginLog.getTo_date()); 		//기간to
		model.addAttribute("user_name", loginLog.getUser_name()); 	//사용자
		model.addAttribute("system_category", loginLog.getSystem_category()); //권한(시스템)
		model.addAttribute("ip", loginLog.getIp()); //ip
		
		System.out.println("----로그인 이력 목록(loginLogList) END-----");
		insertAccessLog(request, model);
		
		return "/system1/log/login_log/login_log_list";
	}
			
	
	//#####################
	// 마이 페이지
	//#####################
	// 마이페이지 수정으로 이동
	@RequestMapping(value = "mypage_main")
	public String mypageMain(HttpServletRequest request, Model model) {
		System.out.println("JmhController mypageMain Start..");
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		
		// 세션값 받음
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		System.out.println("userInfo.getUser_id() -> " + userInfo.getUser_id());
		
		//-------------------------------------------------
		// user_id 마이페이지
		UserInfo userInfoDto = uiService.idConfirm(userInfo.getUser_id());
		//-------------------------------------------------
		model.addAttribute("userInfoDto", userInfoDto);
		insertAccessLog(request, model);
		
		return "mypage/mypage_main";
	}
	//--------------------------------------------------------------------------------------	
	// 개인정보 수정용 비밀번호 확인 페이지
	@RequestMapping(value = "mypage_check_pw")
	public String mypageCheckPw() {
		System.out.println("JmhController mypageCheckPw Start..");
	
		return "mypage/mypage_check_pw";
	}
	//--------------------------------------------------------------------------------------	
	// 개인정보 수정 페이지로 이동
	@RequestMapping(value = "mypage_update")
	public String mypageUpdate(HttpServletRequest request, Model model, UserInfo userInfo, HttpSession session) {
		System.out.println("JmhController mypage_update_view Start..");
		System.out.println("session.userInfo->"+request.getSession().getAttribute("userInfo"));
		
		// 세션에 담긴 값들
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		model.addAttribute("userInfoDto", userInfoDTO);
		System.out.println("userInfoDTO.getUser_id()->"+userInfoDTO.getUser_id());
		System.out.println("userInfoDTO.getUser_pw()->"+userInfoDTO.getUser_pw());
		//System.out.println("userInfoDTO.getUser_birth()->"+userInfoDTO.getUser_birth());
			
		//-------------------------------------------------
		// 권한 정보 가져옴
		Code code = new Code();
		code.setField_name("system_category");
		List<Code> codeList = uiService.codeList(code);
		//-------------------------------------------------
		System.out.println("JmhController mypage_update codeList.size->"+codeList.size());
		model.addAttribute("CodeList_system_category", codeList);
	
		//-------------------------------------------------
		// 기간정보 출력
		List<Organization> orgList = uiService.orgList();
		//-------------------------------------------------
		System.out.println("orgList.size()-->"+orgList.size());

		// validation 에러처리 하기 위해 modelAttribute="userInfo"로 보내기 위해 new
		UserInfo userinfo = new UserInfo();
		model.addAttribute("userInfo", userinfo);
		model.addAttribute("orgList", orgList);
		
		// 수정 페이지에서 받은 값들
		System.out.println("user_id->"+userInfo.getUser_id());
		System.out.println("user_pw->"+userInfo.getUser_pw());
		
		insertAccessLog(request, model);
		
		// 유효성 검사
		if(!userInfo.getUser_id().equals(userInfoDTO.getUser_id()) || 
		   !userInfo.getUser_pw().equals(userInfoDTO.getUser_pw())) {
			System.out.println("아이디와 비밀번호 재확인 인증 실패");
			model.addAttribute("msg", "ID와 PW를 다시 확인해주세요.");
			//return "forward:/mypage_check_pw";
			return "forward:/mypage_main"; //비밀번호 재확인 modal창 모드
		} else {
			//-------------------------------------------------
			// 재확인 인증 성공시 로그인 로직으로 검증
			userInfoDTO = uiService.userLoginCheck(userInfo);
			//-------------------------------------------------
			if(userInfoDTO == null) {
				System.out.println("인증 실패");
				model.addAttribute("msg", "ID와 PW를 다시 확인해주세요.");
				//return "forward:/mypage_check_pw";
				return "forward:/mypage_main"; //비밀번호 재확인 modal창 모드
			} else {
				System.out.println("인증 성공");
				session.setAttribute("userInfoDTO", userInfoDTO);
				return "mypage/mypage_update";
			}
		}
	}
	//--------------------------------------------------------------------------------------	
	// 수정페이지 이미지첨부 + update
	@PostMapping(value = "mypage_update_result")
	public String mypageUpdateResult (@ModelAttribute("userInfo") @Valid UserInfo userInfo
									, BindingResult bindingResult
									, HttpServletRequest request
									, Model model) throws IOException {
		System.out.println("JmhController mypageUpdateResult Start..");
		UserInfo userInfoDTO = (UserInfo) request.getSession().getAttribute("userInfo");
		
		if(bindingResult.hasErrors()) {
			System.out.println("validation 에러 발생");			
			model.addAttribute("msg", "fail");
			return "mypage/mypage_update";
		}
	
		//-------------------------------------------------
		// 모든 회원정보 userInfo에 set하고 회원정보수정 로직 실행
		int result = uiService.updateUser(userInfo);
		//-------------------------------------------------
		System.out.println("result->"+result);
		
		if(result == 1) {
			System.out.println("수정성공");
			// 이미지 update가 성공하면 세션정보 최신화
			// kjo 서비스 내에 findID메소드를 사용
		////	UserInfo us = uis.findbyuserId(userInfo);
			// 세션정보 최신화
		////	request.getSession().setAttribute("userInfo", us);
			model.addAttribute("msg", "success");
			return "forward:/mypage_main"; //msg전달 redirect->forward
		} else {
			System.out.println("수정실패");
			model.addAttribute("msg", "fail");
			return "forward:/mypage_update"; //msg전달 redirect->forward
		}
	}	
		
}
