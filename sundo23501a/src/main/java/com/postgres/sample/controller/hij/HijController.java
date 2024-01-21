package com.postgres.sample.controller.hij;

import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.postgres.sample.dto.AccessLog;
import com.postgres.sample.dto.CategoryVO;
import com.postgres.sample.dto.Code;
import com.postgres.sample.dto.Flow;
import com.postgres.sample.dto.HijResponse;
import com.postgres.sample.dto.Observation;
import com.postgres.sample.dto.Organization;
import com.postgres.sample.dto.Paging;
import com.postgres.sample.dto.RainFall;
import com.postgres.sample.dto.UserInfo;
import com.postgres.sample.dto.WaterGate;
import com.postgres.sample.dto.WaterLevel;
import com.postgres.sample.service.hij.HijService;
import com.postgres.sample.service.jmh.JmhUserInfoService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HijController {
    private static final Logger Logger = LoggerFactory.getLogger(HijController.class);

    private final HijService hs;

    private final JmhUserInfoService uiService;		//사용자 관리
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
    // 1. 관측소 - 목록
    //--------------------------------------------------------------------------------------
    // 관측소 목록
    @GetMapping("/observation_find")
    public String observation_find(Observation observation, String currentPage, HttpServletRequest request, Model model) {
        System.out.println("HijController observation_find START");
        int  totalCount = hs.totalCount();	// 관측소 목록 갯수


        Paging page = new Paging(totalCount, currentPage, 10);
        observation.setStart(page.getStart());
        observation.setEnd(page.getEnd());


        List<Observation> observationList = hs.observationList(observation); //관측소 목록 리스트
        System.out.println("observationList size : " + observationList.size());


        List<Code> codeListM = hs.getCodeList("observe_method"); //관측방식리스트
        List<Organization> orgList = hs.getOrgList(); //운영기관 리스트
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setTbl_name("organization");
        categoryVO.setField_name("org_area");
        List<Organization> categoryList = hs.getCategoryList(categoryVO); //행정구역 리스트

        System.out.println("size:" + categoryList.size());

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("observationList", observationList);
        model.addAttribute("observation" ,observation);
        model.addAttribute("CodeObserveMethod",codeListM);
        model.addAttribute("OrgList", orgList);
        model.addAttribute("categoryList", categoryList);
        
        insertAccessLog(request, model); //접속이력
        
        return "/system2/observation_sys/observation_find";
    }

    // 관측소 등록 페이지
    @GetMapping("/observation_create")
    public String observation_create(Observation observation, HttpServletRequest request, Model model) {
        System.out.println("HijController observation_create START");
        List<Code> codeListM = hs.getCodeList("observe_method"); //관측방식리스트
        List<Code> codeListT = hs.getCodeList("observe_type"); //관측유형리스트
        List<Organization> orgList = hs.getOrgList(); //운영기관 리스트

        List<WaterGate> categoryList = hs.waterGateList(); //표준하천이름 가져오기

        model.addAttribute("CodeObserveMethod",codeListM);
        model.addAttribute("CodeObserveType", codeListT);
        model.addAttribute("OrgList", orgList);
        model.addAttribute("categoryList", categoryList);
        
        insertAccessLog(request, model); //접속이력
        
        return "/system2/observation_sys/observation_create";
    }

    // 관측소 등록 시행
    @PostMapping("ob_create")
    public String obCreate(Observation observation, HttpServletRequest request, Model model) {
        System.out.println("HijController obCreate START");


        System.out.println("observe_post : "+ observation.getObserve_post());
        System.out.println(" river_code: "+ observation.getRiver_code());
        System.out.println(" observe_type: "+ observation.getObserve_type());
        System.out.println("latitude : "+ observation.getLatitude());
        System.out.println(" longitude: "+ observation.getLongitude());
        System.out.println(" observe_method: "+ observation.getObserve_method());
        System.out.println(" org_code: "+ observation.getOrg_code());
        System.out.println(" org_area: "+ observation.getOrg_area());

        int createResult = hs.obCreate(observation);
        
        insertAccessLog(request, model); //접속이력
        
        return "redirect:/observation_find";
    }

    // 관측소 상세페이지 조회
    @GetMapping("/observation_detail")
    public String observationDetail (Observation observation, HttpServletRequest request, Model model) {
        System.out.println("HijController observationDetail START");

        Observation getObservation = hs.getObservation(observation);

        model.addAttribute("observation", getObservation);
        
        insertAccessLog(request, model); //접속이력
        
        return "/system2/observation_sys/observation_detail";
    }

    // 관측소 수정 조회 페이지
    @GetMapping("observation_edit")
    public String observationEdit (Observation observation, HttpServletRequest request, Model model) {
        System.out.println("HijController observationEdit START");

        Observation getObservation = hs.getObservation(observation);
        List<Code> codeListM = hs.getCodeList("observe_method"); //관측방식리스트
        List<Code> codeListT = hs.getCodeList("observe_type"); //관측유형리스트
        List<Organization> orgList = hs.getOrgList(); //운영기관 리스트

        List<WaterGate> categoryList = hs.waterGateList(); //표준하천이름 가져오기

        model.addAttribute("observation", getObservation);
        model.addAttribute("CodeObserveMethod",codeListM);
        model.addAttribute("CodeObserveType", codeListT);
        model.addAttribute("OrgList", orgList);
        model.addAttribute("categoryList", categoryList);

        insertAccessLog(request, model); //접속이력
        
        return "/system2/observation_sys/observation_edit";
    }

    // 관측소 수정 시행
    @PostMapping("ob_edit")
    public String obEdit(Observation observation, HttpServletRequest request, Model model) {
        System.out.println("HijController obEdit START");

        int editResult = hs.obEdit(observation);
        System.out.println("Observe_code : "+ observation.getObserve_code());

        model.addAttribute("observation", observation);

        insertAccessLog(request, model); //접속이력
        
        return "redirect:/observation_detail?observe_code="+observation.getObserve_code();
    }

    // 관측소 삭제 시행
    @ResponseBody
    @RequestMapping(value = "/ob_delete")
    public int obDelete(Observation observation, HttpServletRequest request, Model model) {
        System.out.println("HijController obDelete START");
        int deleteResult = 0;

        deleteResult = hs.obDelete(observation);

        insertAccessLog(request, model); //접속이력
        
        return deleteResult;
    }
    
    
    // 관측소 검색
    @ResponseBody
    @RequestMapping(value="/searchObservation")
    public HijResponse searchObservation(Observation observation, String currentPage, HttpServletRequest request, Model model) {
    	
    	int totalCount = hs.searchTotalO(observation);	//검색 갯수
    	
    	Paging page = new Paging(totalCount, currentPage, 10);
    	observation.setStart(page.getStart());
    	observation.setEnd(page.getEnd());
    	
    	List<Observation> searchO = hs.searchO(observation);
    	
    	HijResponse hijResponse = new HijResponse();
    	hijResponse.setObj(page);
    	hijResponse.setList(searchO);
    	
    	System.out.println("totalCountO : "+ totalCount);

    	insertAccessLog(request, model); //접속이력
        
    	return hijResponse;
    }
    
//--------------------------------------------------------------------------------------
// 2. 관측소 - 시자료 - 수위
//--------------------------------------------------------------------------------------
    // 수위 목록
    @GetMapping("/time_find")
    public String time_find(WaterLevel waterLevel, String currentPage, HttpServletRequest request, Model model) {
        System.out.println("HijController time_find START");


        int totalCount= hs.waterLevelTotal();

        Paging page = new Paging(totalCount, currentPage, 10);
        waterLevel.setStart(page.getStart());
        waterLevel.setEnd(page.getEnd());

        List<WaterLevel> waterLevelList = hs.waterLevelList(waterLevel);	// waterLevel 리스트

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("waterLevelList", waterLevelList);
        model.addAttribute("waterLevelListSize", waterLevelList.size());
        
        System.out.println("time_find  totalcount :" +totalCount);
        
    	insertAccessLog(request, model); //접속이력

    	return "/system2/observation_sys/time_find";
        
    }
    // 수위 수정 조회
    @GetMapping("/time_edit")
    public String time_edit(String river_code, Date observe_date, HttpServletRequest request, Model model) {
        System.out.println("HijController time_edit START");
        WaterLevel waterLevel = new WaterLevel();
        waterLevel.setRiver_code(river_code);
        waterLevel.setObserve_date(observe_date);
        WaterLevel waterLevelT = hs.getWaterLevel(waterLevel);
        
        model.addAttribute("waterLevel", waterLevelT);
        System.out.println("river_code : " +waterLevelT.getRiver_code());
        System.out.println("Observe_date : " +waterLevelT.getObserve_date());
    
    	insertAccessLog(request, model); //접속이력
       
        return "/system2/observation_sys/time_edit";
    }
    
    // 시자료 수정
    @PostMapping("/t_edit")
    public String tEdit(WaterLevel waterLevel, HttpServletRequest request, Model model) {
    	System.out.println("HijController t_edit START");
    	
    	int editResult = hs.tEdit(waterLevel);
       
        model.addAttribute("waterLevel", waterLevel);
        System.out.println("rivercode1: "+waterLevel.getRiver_code());
        System.out.println("Observe_date1: "+waterLevel.getObserve_date());

    	insertAccessLog(request, model); //접속이력
        
    	return "redirect:/time_find?river_code="+waterLevel.getRiver_code();
    }
    
    // 시자료 - 수위 - 검색
    @ResponseBody
    @RequestMapping(value="/searchWaterLevel")
    public HijResponse searchWaterLevel(WaterLevel waterLevel, String currentPage, HttpServletRequest request, Model model) {
    	System.out.println("HijController searchWaterLevel START");
    	
    	int totalCount = hs.searchTotalW(waterLevel);	//검색 갯수
    	System.out.println("totalCountW : "+ totalCount);
    	
    	System.out.println("currentPage : " +currentPage);
		Paging page = new Paging(totalCount, currentPage, 10);
		waterLevel.setStart(page.getStart()); waterLevel.setEnd(page.getEnd());
		
    	System.out.println("river_code : " + waterLevel.getRiver_code() );
    	System.out.println("start_date : " + waterLevel.getStart_date() );
    	System.out.println("end_date : " + waterLevel.getEnd_date() );
    	
    	List<WaterLevel> searchWlist = hs.searchW(waterLevel);
    	
    	System.out.println("searchW 사이즈c :" + searchWlist.size());
    	System.out.println("river_code : " + waterLevel.getRiver_code() );
    	HijResponse hijResponse = new HijResponse();
    	hijResponse.setObj(page);
    	hijResponse.setList(searchWlist);
    	
    	System.out.println("totalCountW : "+ totalCount);
    	
    	insertAccessLog(request, model); //접속이력

    	return hijResponse;
    	

    }
    
 //--------------------------------------------------------------------------------------  
 // 2. 관측소 - 시자료 - 강우량
 //--------------------------------------------------------------------------------------
      // 강우량 목록
      @GetMapping("/time_find_R")
      public String time_find_R(RainFall rainFall, String currentPage, HttpServletRequest request, Model model) {
          System.out.println("HijController time_find_R START");
          
          int totalCount= hs.rainFallTotal();

          Paging page = new Paging(totalCount, currentPage, 10);
          rainFall.setStart(page.getStart());
          rainFall.setEnd(page.getEnd());

          List<RainFall> rainFallList = hs.rainFallList(rainFall);	// waterLevel 리스트

          model.addAttribute("totalCount", totalCount);
          model.addAttribute("page", page);
          model.addAttribute("rainFallList", rainFallList);
          model.addAttribute("rainFallListSize", rainFallList.size());

      	  insertAccessLog(request, model); //접속이력
          
          return "/system2/observation_sys/time_find_R";
      }
      
      // 강우량 수정 조회
      @GetMapping("/time_edit_R")
      public String time_edit_R(String river_code, Date observe_date, HttpServletRequest request, Model model) {
          System.out.println("HijController time_edit_R START");
          RainFall rainFall = new RainFall();
          rainFall.setRiver_code(river_code);
          rainFall.setObserve_date(observe_date);
          RainFall rainFallT = hs.getRainFall(rainFall);
          
          model.addAttribute("rainFall", rainFallT);
          System.out.println("river_code : " +rainFallT.getRiver_code());
          System.out.println("Observe_date : " +rainFallT.getObserve_date());
      
      	  insertAccessLog(request, model); //접속이력
          
          return "/system2/observation_sys/time_edit_R";
      }
      
      // 강우량 수정
      @PostMapping("/t_edit_R")
      public String t_edit_R(RainFall rainFall, HttpServletRequest request, Model model) {
      	System.out.println("HijController t_edit_R START");
      	
      	int editResult = hs.tEditR(rainFall);
         
          model.addAttribute("rainFall", rainFall);
          System.out.println("rivercode1: "+rainFall.getRiver_code());
          System.out.println("Observe_date1: "+rainFall.getObserve_date());
          
      	insertAccessLog(request, model); //접속이력

      	return "redirect:/time_find_R?river_code="+rainFall.getRiver_code();
      }    
    
      // 시자료 - 강우량 - 검색
      @ResponseBody
      @RequestMapping(value="/searchRainFall")
      public HijResponse searchRainFall(RainFall rainFall, String currentPage, HttpServletRequest request, Model model) {
      	
      	int totalCount = hs.searchTotalR(rainFall);	//검색 갯수
      	
      	Paging page = new Paging(totalCount, currentPage, 10);
      	rainFall.setStart(page.getStart());
      	rainFall.setEnd(page.getEnd());
      	
      	List<RainFall> searchR = hs.searchR(rainFall);
      	
      	HijResponse hijResponse = new HijResponse();
      	hijResponse.setObj(page);
      	hijResponse.setList(searchR);
      	
      	System.out.println("totalCountR : "+ totalCount);

    	insertAccessLog(request, model); //접속이력

      	return hijResponse;
      }
  //--------------------------------------------------------------------------------------  
  // 2. 관측소 - 시자료 - 우량
  //--------------------------------------------------------------------------------------
       // 우량 목록
       @GetMapping("/time_find_F")
       public String time_find_F(Flow flow,  String currentPage, HttpServletRequest request, Model model) {
           System.out.println("HijController time_find_F START");
           
           int totalCount= hs.flowTotal();

           Paging page = new Paging(totalCount, currentPage, 10);
           flow.setStart(page.getStart());
           flow.setEnd(page.getEnd());
           List<Flow> flowYearList = hs.flowYearList(); // year 리스트
           flow.setObserve_year(flowYearList.get(0).getObserve_year());
           List<Flow> flowList = hs.flowList(flow);	// waterLevel 리스트
           

           model.addAttribute("totalCount", totalCount);
           model.addAttribute("page", page);
           model.addAttribute("flowList", flowList);
           model.addAttribute("flowListSize", flowList.size());
           model.addAttribute("flowYearList", flowYearList);
           
           System.out.println("년 : " + flowList.get(0).getObserve_year());
           System.out.println("getJanuary 0 : " + flowList.get(0).getJanuary());
           System.out.println("getJanuary 1: " + flowList.get(1).getJanuary());
           
       	   insertAccessLog(request, model); //접속이력

           return "/system2/observation_sys/time_find_F";
       }
       
       // 우량 수정 조회
       @GetMapping("/time_edit_F")
       public String time_edit_F(String river_code, int observe_year, int observe_day, HttpServletRequest request, Model model) {
           System.out.println("HijController time_edit_R START");
           Flow flow = new Flow();
           flow.setRiver_code(river_code);
           flow.setObserve_year(observe_year);
           flow.setObserve_day(observe_day);
           
           Flow flowF = hs.getFlow(flow);
           
           model.addAttribute("flow", flowF);
    
       	   insertAccessLog(request, model); //접속이력
          
           return "/system2/observation_sys/time_edit_F";
       }
       
       // 우량 수정
       @PostMapping("/t_edit_F")
       public String t_edit_F(Flow flow, HttpServletRequest request, Model model) {
       	System.out.println("HijController t_edit_F START");
       	
       	int editResult = hs.tEditF(flow);
          
        model.addAttribute("flow", flow);  
        
        System.out.println("flow : " + flow.getObserve_day());

    	insertAccessLog(request, model); //접속이력

       	return "redirect:/time_find_F?river_code="+flow.getRiver_code()+"&observe_year()=" + flow.getObserve_year();
       }    
       
       // 시자료 - 우량 - 검색
       @ResponseBody
       @RequestMapping(value="/searchFlow")
       public HijResponse searchRainFall(Flow flow, String currentPage, HttpServletRequest request, Model model) {
       	
       	int totalCount = hs.searchTotalF(flow);	//검색 갯수
       	
       	Paging page = new Paging(totalCount, currentPage, 10);
       	flow.setStart(page.getStart());
       	flow.setEnd(page.getEnd());
       	
       	List<Flow> searchF = hs.searchF(flow);
       	
       	HijResponse hijResponse = new HijResponse();
       	hijResponse.setObj(page);
       	hijResponse.setList(searchF);
       	
       	System.out.println("totalCountF : "+ totalCount);
       	
    	insertAccessLog(request, model); //접속이력

       	return hijResponse;
       }
       
       //--------------------------------------------------------------------------------------  
       // 5. 관측소 - 통계 - 수위정보
       //--------------------------------------------------------------------------------------
       @ResponseBody
       @GetMapping("/time_chart_list")
       public ModelAndView waterLevelChartList(WaterLevel waterLevel, HttpServletRequest request, Model model){
    	   System.out.println("---- 관측소 - 통계 - 수위정보 목록(waterLevelChartList) START-----");
			
			if(waterLevel.getStart_date() == null) {
				waterLevel.setStart(1);
				waterLevel.setEnd(1);
				List<WaterLevel> waterLevelList = hs.waterLevelList(waterLevel);   // waterLevel 리스트
				System.out.println("첫번째 관측일:"+waterLevelList.get(0).getObserve_date());
				waterLevel.setStart_date(waterLevelList.get(0).getObserve_date());
			}
			WaterLevel waterLevelStat = hs.waterLevelStat(waterLevel);   // waterLevel 리스트
				
			
			ModelAndView mv = new ModelAndView();
		    mv.setViewName("/system2/observation_sys/time_chart_list"); // 뷰의 이름
		    mv.addObject("start_date", waterLevel.getStart_date());
		    mv.addObject("waterLevelStat", waterLevelStat);
		    
			System.out.println("---- 관측소 - 통계 - 수위정보 목록(waterLevelChartList) END-----");
			//insertAccessLog(request, model);
			
			return mv;
		}

       //검색
       @ResponseBody
       @GetMapping("/time_chart_search")
       public WaterLevel waterLevelChartSearch(WaterLevel waterLevel, HttpServletRequest request, Model model){
    	   System.out.println("---- 관측소 - 통계 - 수위정보 검색(waterLevelChartSearch) START-----");
			
			if(waterLevel.getStart_date() == null) {
				waterLevel.setStart(1);
				waterLevel.setEnd(1);
				List<WaterLevel> waterLevelList = hs.waterLevelList(waterLevel);   // waterLevel 리스트
				System.out.println("첫번째 관측일:"+waterLevelList.get(0).getObserve_date());
				waterLevel.setStart_date(waterLevelList.get(0).getObserve_date());
			}
			WaterLevel waterLevelStat = hs.waterLevelStat(waterLevel);   // waterLevel 리스트
			
	    	System.out.println("---- 관측소 - 통계 - 수위정보 검색(waterLevelChartSearch) END-----");
			insertAccessLog(request, model);
			
			return waterLevelStat;
		}
       
       //--------------------------------------------------------------------------------------  
       // 5. 관측소 - 통계 - 강우량 정보
       //--------------------------------------------------------------------------------------
       @ResponseBody
       @GetMapping("/time_chart_list_R")
       public ModelAndView rainFallChartListR(RainFall rainFall, HttpServletRequest request, Model model){
    	   System.out.println("---- 관측소 - 통계 - 강우량 정보 목록(rainFallChartListR) START-----");
			
			if(rainFall.getStart_date() == null) {
				System.out.println("1.조회기간:null");
				System.out.println("1.river_code:"+rainFall.getRiver_code());
				rainFall.setStart(1);
				rainFall.setEnd(1);
				List<RainFall> rainFallList = hs.rainFallList(rainFall);   // rainFall 리스트
				System.out.println("첫번째 관측일:"+rainFallList.get(0).getObserve_date());
				rainFall.setStart_date(rainFallList.get(0).getObserve_date());
			}else {
				System.out.println("2.조회기간:"+rainFall.getStart_date());
				System.out.println("2.river_code:"+rainFall.getRiver_code());
			}
			RainFall rainFallStat = hs.rainFallStat(rainFall);   // rainFall 리스트
			
			System.out.println("rainFallStat 01시:"+rainFallStat.getHour_01());
			System.out.println("rainFallStat 20시:"+rainFallStat.getHour_20());
			//model.addAttribute("rainFallList", rainFallStat);				
			//return "/system2/observation_sys/timeChart";			
			
			ModelAndView mv = new ModelAndView();
		    mv.setViewName("/system2/observation_sys/time_chart_list_R"); // 뷰의 이름
		    mv.addObject("start_date", rainFall.getStart_date());
		    mv.addObject("rainFallStat", rainFallStat);
		    
			System.out.println("---- 관측소 - 통계 - 강우량 정보 목록(rainFallChartListR) END-----");
			//insertAccessLog(request, model);
			
			return mv;
		}

       //검색
       @ResponseBody
       @GetMapping("/time_chart_search_R")
       public RainFall rainFallChartSearchR(RainFall rainFall, HttpServletRequest request, Model model){
    	   System.out.println("---- 관측소 - 통계 - 강수량 정보 검색(rainFallChartSearchR) START-----");
			
			if(rainFall.getStart_date() == null) {
				System.out.println("1조회기간:null");
				System.out.println("1river_code:"+rainFall.getRiver_code());
				rainFall.setStart(1);
				rainFall.setEnd(1);
				List<RainFall> rainFallList = hs.rainFallList(rainFall);   // rainFall 리스트
				System.out.println("첫번째 관측일:"+rainFallList.get(0).getObserve_date());
				rainFall.setStart_date(rainFallList.get(0).getObserve_date());
			}else {
				System.out.println("2조회기간:"+rainFall.getStart_date());
				System.out.println("2river_code:"+rainFall.getRiver_code());
			}
			RainFall rainFallStat = hs.rainFallStat(rainFall);   // rainFall 리스트
			
	    
			System.out.println("---- 관측소 - 통계 - 강수량 정보 검색(rainFallChartSearchR) END-----");
			insertAccessLog(request, model);
			
			return rainFallStat;
		}
       
}

