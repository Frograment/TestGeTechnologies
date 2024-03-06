package com.getechnologies.Services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.getechnologies.Dao.VehicleStayDao;
import com.getechnologies.Entities.Vehicle;
import com.getechnologies.Entities.VehicleStay;
import com.getechnologies.Services.VehicleStayService;

@Service
public class VehicleStayServiceImpl implements VehicleStayService{

	
	
	@Autowired
	private VehicleStayDao dao;
	
	
	
	@Override
	public List<VehicleStay> findAll() {
		
		return (List<VehicleStay>) dao.findAll();
	}

	@Override
	public VehicleStay save(VehicleStay vehicle) {
		
		return dao.save(vehicle);
	}

	@Override
	public VehicleStay update(VehicleStay vehicle) {
		
		return dao.save(vehicle);
	}

	@Override
	public VehicleStay delete(int id) {
		VehicleStay vehicle = dao.findById(id).orElse(new VehicleStay());

		dao.deleteById(id);
		
		return vehicle;
	}

}