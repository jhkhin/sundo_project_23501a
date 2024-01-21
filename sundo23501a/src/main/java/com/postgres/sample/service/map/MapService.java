package com.postgres.sample.service.map;

import com.postgres.sample.dto.Observation;

import java.util.List;

public interface MapService {


    Observation CntObservationAll();

    List<Observation> findObservationAll(Observation ov);
}
