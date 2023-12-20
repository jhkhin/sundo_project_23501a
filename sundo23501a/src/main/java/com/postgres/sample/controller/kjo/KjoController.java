package com.postgres.sample.controller.kjo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.postgres.sample.dto.*;
import com.postgres.sample.service.kjo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


//@RestController
@Controller
@RequiredArgsConstructor
public class KjoController {
	private static final Logger Logger = LoggerFactory.getLogger(KjoController.class);
	
	
    private final KjoService kjoService;

    

    @GetMapping("/water_resourcesList")
    public String waterResoucesList(@RequestParam(defaultValue = "1") String currentPage, Model model) {
    	System.out.println("water_resourcesList");
    	WaterResources wr = new WaterResources();
    	//	페이징 위한 수자원시설물 개수
    	wr.setTotal(kjoService.cntWaterResource().getTotal());
    	Paging page = new Paging(wr.getTotal(), currentPage,10);
    	wr.setStart(page.getStart());
    	wr.setEnd(page.getEnd());
    	
    	List<WaterResources> wrctgList = kjoService.findFacilityCategory();
    	List<OrgArea> orgList = kjoService.findAllOrgArea();
    	List<WaterResources> wrList = kjoService.PaingWaterResourceLists(wr);

    	model.addAttribute("wrctgList",wrctgList);
    	model.addAttribute("orgList",orgList);
    	model.addAttribute("wrList",wrList);
    	model.addAttribute("page",page);
    	return "/system3/kjo/water_resources/water_resourcesList";
    }

    @ResponseBody
    @GetMapping("/facility_code_List")
    public List<WaterResources> facilityCodeList(@RequestParam String facility_category) {
    	
        WaterResources wr = new WaterResources();
        wr.setFacility_category(facility_category);
        List<WaterResources> wrList = kjoService.findFacilityAddrbyCategory(wr);
        System.out.println(wrList);
        return wrList;
    }
    
    @ResponseBody
    @GetMapping("/searchWaterResources")
    public KjoResponse searchWaterResources(@RequestParam(defaultValue = "1") String currentPage, WaterResources wr) {
        wr.setTotal(kjoService.searchCnt(wr).getTotal());
        Paging page = new Paging(wr.getTotal(), currentPage,10);
        wr.setStart(page.getStart());
        wr.setEnd(page.getEnd());

        List<WaterResources> wrList = kjoService.searchWaterResources(wr);
        System.out.println(wrList);
        KjoResponse response = new KjoResponse();
        response.setObj(page);
        response.setObjList(wrList);

//        KjoResponse response = KjoResponse.builder()
//                .objList(wrList)
//                .obj(page)
//                .build();

        return response;

    }
    

    @GetMapping("/checkresultform")
    public String checkresult(WaterResources wr, Model model) throws Exception {
    	System.out.println("checkresult");
        wr= kjoService.findWaterResourcesById(wr);


        model.addAttribute("WaterResources",wr);
    	
        return "/system3/kjo/check/checkresultform";
    }
    
    @PostMapping("/checkresultSave")
    public String checkresultSave(KJO_CheckReport cr, RedirectAttributes redirectAttributes) {
        //  입력된 사용자
        UserInfo ui = new UserInfo();
        ui.setUser_name(cr.getUser_name());
        ui.setUser_position(cr.getUser_position());
        ui.setUser_department(cr.getUser_department());

        WaterResources wr = new WaterResources();
        //  수자원정보
        wr.setFacility_code(cr.getFacility_code());
        UserInfo uid = (kjoService.findUserId(ui));
        if (uid.equals(null)) {
            int error = 0;

            redirectAttributes.addAttribute("WaterResources",wr);
            return "redirect:/checkresultform";
        }

        CheckReport checkReport = new CheckReport();
        checkReport.setTotal(kjoService.cntAllReport().getTotal());

        checkReport.setDoc_no(checkReport.getTotal()+1);      //글번호
        checkReport.setFacility_code(cr.getFacility_code());    //시설물코드
        checkReport.setWeather(cr.getWeather());        //날씨
        checkReport.setCheck_date(cr.getCheck_date());  //  점검일자

        String userId = uid.getUser_id();               //사용자ID
        checkReport.setUser_id(userId);
        checkReport.setCheck_result(cr.getCheck_result());  //점검결과
        checkReport.setSpec_memo(cr.getSpec_memo());        //특이항
// 다른 필드 설정
        checkReport.setCreate_datetime(String.valueOf(LocalDate.now()));

        int result = kjoService.inputChkReport(checkReport);
/*              chkList저장                                                */
        String notes = cr.getNote();
        String grades = cr.getCheck_grade();
        String[] noteArray = notes.split("\\|",-1);
        String[] gradeArray = grades.split("\\|",-1);
        List<CheckItemInfo> chkItemInfoList = kjoService.findAllCheckItemInfo();



        CheckList chkList = new CheckList();
        int chklistresult = 0;

        chkList.setDoc_no(checkReport.getDoc_no());
        chkList.setFacility_code(checkReport.getFacility_code());
        chkList.setChecklist_no(kjoService.cntChkListByFacilityCode(chkList).getTotal());

        for (int i = 0; i < chkItemInfoList.size(); i++) {
            chkList.setCheck_category(chkItemInfoList.get(i).getCheck_category());
            chkList.setCheck_item(chkItemInfoList.get(i).getCheck_item());
            chkList.setCheck_grade(gradeArray[i]);
            chkList.setNote(noteArray[i]);

            chkList.setChecklist_no(chkList.getChecklist_no()+1);

            chklistresult += kjoService.inputChkList(chkList);
        }


        return "redirect:/selectCheckReportList";
    }

    @GetMapping("/selectCheckReportList")
    public String selectCheckReportList(@RequestParam(defaultValue = "1") String currentPage, Model model){
        List<WaterResources> wrctgList = kjoService.findFacilityCategory();
        List<OrgArea> orgList = kjoService.findAllOrgArea();
        List<Organization> organizationList = kjoService.findAllOrgList();
//        CheckReport crt = kjoService.cntAllReport();
//        Paging page = new Paging(crt.getTotal(), currentPage, 10);
//        crt.setStart(page.getStart());
//        crt.setEnd(page.getEnd());
//
//        List<CheckReport> crList = kjoService.pageChkReport(crt);

        WaterResources wr = new WaterResources();
        //	페이징 위한 수자원시설물 개수
        wr.setTotal(kjoService.cntWaterResource().getTotal());
        Paging page = new Paging(wr.getTotal(), currentPage,10);
        wr.setStart(page.getStart());
        wr.setEnd(page.getEnd());


        List<WaterResources> wrList = kjoService.PaingWaterResourceAndCheckReport(wr);

//        for ( CheckReport cr: crList){
//            cr.setModify_datetime(cr.getModify_datetime().substring(0,10));
//
//        }


        model.addAttribute("wrctgList",wrctgList);
        model.addAttribute("wrList",wrList);
        model.addAttribute("organizationList",organizationList);
        model.addAttribute("orgList",orgList);
//        model.addAttribute("crList",crList);
        model.addAttribute("page",page);
        return "/system3/kjo/check/selectCheckReportList";
    }

    @ResponseBody
    @GetMapping("/getcheckresultform")
    public KjoResponse getcheckresultform(WaterResources wr,  Model model){
        System.out.println("checkresult");
//        if (cr.get)
        if (wr.getFacility_category().equals("전체")) {
            wr.setFacility_category("");
        }
        if (wr.getOrg_name().equals("전체")) {
            wr.setOrg_name("");
        }
        if (wr.getOrg_area().equals("전체")) {
            wr.setOrg_area("");
        }

        wr.setTotal(kjoService.searchCntWRAndCR(wr).getTotal());

        Paging page = new Paging(wr.getTotal(), wr.getCurrentPage(),10);
        wr.setStart(page.getStart());
        wr.setEnd(page.getEnd());

        List<WaterResources> CRList =  kjoService.searchWRAndCR(wr);
        KjoResponse response = new KjoResponse();
        response.setObj(page);
        response.setObjList(CRList);

        return response;
    }

    @GetMapping("/selectcheckReportlist2")
    public String selectcheckReportlist2(CheckReport checkReport,Model model) {
        checkReport.setTotal(kjoService.cntCheckReportByFcCode(checkReport).getTotal());
        Paging page = new Paging(checkReport.getTotal(), checkReport.getCurrentPage(),10);
        checkReport.setStart(page.getStart());
        checkReport.setEnd(page.getEnd());
        WaterResources wr = new WaterResources();
        wr.setFacility_code(checkReport.getFacility_code());

        checkReport.setCate_name( kjoService.findWaterResourcesById(wr).getCate_name());
        List<CheckReport> CRList = kjoService.findCheckReportByFcCode(checkReport);

        model.addAttribute("CRList", CRList);
        model.addAttribute("facility_code", checkReport.getFacility_code());
        model.addAttribute("cate_name", checkReport.getFacility_code());
        model.addAttribute("page",page);

        return "system3/kjo/check/selectCheckReportList2";
    }



    @GetMapping("/getcheckresult")
    public String getcheckresult(CheckReport cr,Model model) {
        CheckReport checkReport = kjoService.findCheckReportByDocNo(cr);
        CheckList cl = new CheckList();
        cl.setDoc_no(cr.getDoc_no());
        List<CheckList> CLList = kjoService.findCheckListAndCodeByDocNo(cl);

        List<CheckList> FloorList = CLList.stream().filter(checkList -> "마루".equals(checkList.getCheck_category())).collect(Collectors.toList());
        List<CheckList> TopCLList = CLList.stream().filter(checkList -> "상류면".equals(checkList.getCheck_category())).collect(Collectors.toList());
        List<CheckList> BotList = CLList.stream().filter(checkList -> "하류면".equals(checkList.getCheck_category())).collect(Collectors.toList());
        List<CheckList> IpqList = CLList.stream().filter(checkList -> "검사량".equals(checkList.getCheck_category())).collect(Collectors.toList());


        model.addAttribute("CheckReport", checkReport);
        model.addAttribute("CheckList", CLList);

        model.addAttribute("FloorList", FloorList);
        model.addAttribute("TopCLList", TopCLList);
        model.addAttribute("BotList", BotList);
        model.addAttribute("IpqList", IpqList);
        return "system3/kjo/check/getcheckresult";
    }



    
    
	
}