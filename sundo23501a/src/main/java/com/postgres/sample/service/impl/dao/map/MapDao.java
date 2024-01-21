package com.postgres.sample.service.impl.dao.map;

import com.postgres.sample.dto.Observation;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface MapDao {
    Observation CntObservationAll();

    List<Observation> findObservationAll(Observation ov);
}
