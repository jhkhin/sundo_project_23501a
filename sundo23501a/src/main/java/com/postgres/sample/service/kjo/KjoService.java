package com.postgres.sample.service.kjo;

import com.postgres.sample.dto.*;

import java.util.List;

public interface KjoService {
    List<CheckItemInfo> findAllCheckItemInfo();
    int inputChkList(CheckList checkList);

    CheckList cntChkListByFacilityCode(CheckList chkList);
    CheckReport cntByFacilityCode(CheckReport checkReport);

    int inputChkReport(CheckReport checkReport);

    CheckReport cntAllReport();

    List<CheckReport> findChkReport();

    List<CheckReport> pageChkReport(CheckReport crt);
    List<CheckReport> pageSearchChkReport(CheckReport crt);

    CheckReport cntSearchChkReport(CheckReport cr);
    List<?> SelectObsList() throws Exception;
    List<Organization> findAllOrgList();
    List<OrgArea> findAllOrgArea();
    UserInfo findByUserId(UserInfo ui);

    UserInfo findUserId(UserInfo ui);

    WaterResources cntWaterResource();
    List<WaterResources> findFacilityCategory();
    List<WaterResources> findFacilityAddr();
    List<WaterResources> findWaterResourceLists();
    List<WaterResources> PaingWaterResourceLists(WaterResources wr);
    List<WaterResources> findFacilityAddrbyCategory(WaterResources wr);
    List<WaterResources> searchWaterResources(WaterResources wr);
    WaterResources searchCnt(WaterResources wr);
    WaterResources findWaterResourcesById(WaterResources wr);

    CheckReport findCheckReportByDocNo(CheckReport cr);

    List<CheckList> findCheckListByDocNo(CheckList cl);

    List<CheckList> findCheckListAndCodeByDocNo(CheckList cl);

    List<WaterResources> PaingWaterResourceAndCheckReport(WaterResources wr);

    WaterResources searchCntWRAndCR(WaterResources wr);

    List<WaterResources> searchWRAndCR(WaterResources wr);

    List<CheckReport> findCheckReportByFcCode(CheckReport checkReport);

    CheckReport cntCheckReportByFcCode(CheckReport checkReport);

    Observation CntObservationAll();

    List<Observation> findObservationAll(Observation ov);

    WaterResources nullcheck(WaterResources wr);
}
