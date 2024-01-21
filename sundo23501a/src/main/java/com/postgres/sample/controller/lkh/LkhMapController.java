package com.postgres.sample.controller.lkh;


import com.postgres.sample.controller.kjo.KjoController;
import com.postgres.sample.dto.AccessLog;
import com.postgres.sample.dto.KjoResponse;
import com.postgres.sample.dto.Observation;
import com.postgres.sample.dto.Paging;
import com.postgres.sample.dto.UserInfo;
import com.postgres.sample.service.jmh.JmhUserInfoService;
import com.postgres.sample.service.map.MapService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/realTimeSystem")
public class LkhMapController {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(KjoController.class);
    private final MapService mapService;

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

    
    @GetMapping("/layer")
    public String waterResourceStatistics(HttpServletRequest request, Model model) {
    	
    	insertAccessLog(request, model); //접속이력

        return "system2/lkh/waterResourceMapSystem/Layer";
    }

    @ResponseBody
    @GetMapping("/obsrvlist")
    public KjoResponse obsrvlist(Observation ov, HttpServletRequest request, Model model) {
        Logger.info("obsrvlist START");

        ov.setTotal(mapService.CntObservationAll().getTotal());
        Paging page = new Paging(ov.getTotal(), ov.getCurrentPage(), 10);
        ov.setStart(page.getStart());
        ov.setEnd(page.getEnd());

        List<Observation> OVList = mapService.findObservationAll(ov);

        KjoResponse response = new KjoResponse();
        response.setObj(page);
        response.setObjList(OVList);
        response.setType("Observation");

        Logger.info("obsrvlist END");
        
		insertAccessLog(request, model); //접속이력

        return response;
    }

    @ResponseBody
    @GetMapping("/layerlist")
    public KjoResponse layerList(HttpServletRequest request, Model model) {
        KjoResponse response = new KjoResponse();
        response.setType("Layer");
        
		insertAccessLog(request, model); //접속이력

        return response;
    }
    @ResponseBody
    @GetMapping("/bookmarklist")
    public KjoResponse obsrvlist(HttpServletRequest request, Model model) {
        KjoResponse response = new KjoResponse();
        response.setType("BookMark");
        
		insertAccessLog(request, model); //접속이력

        return response;
    }
}
