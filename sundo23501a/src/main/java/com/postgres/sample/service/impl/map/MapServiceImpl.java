package com.postgres.sample.service.impl.map;

import com.postgres.sample.dto.Observation;
import com.postgres.sample.service.impl.dao.map.MapDao;
import com.postgres.sample.service.lkh.WaterResourcesService;
import com.postgres.sample.service.map.MapService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl extends EgovAbstractServiceImpl implements MapService {
    private final MapDao mapDao;

    @Override
    public Observation CntObservationAll() {
        return mapDao.CntObservationAll();
    }

    @Override
    public List<Observation> findObservationAll(Observation ov) {
        return mapDao.findObservationAll(ov);
    }
}
