package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService {
    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }
    
    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }
    
    public Optional<CurvePoint> findById(Integer id) {
        return curvePointRepository.findById(id);
    }
    
    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }
    
    public void deleteById(Integer id) {
        curvePointRepository.deleteById(id);
    }
}
