package com.getechnologies.Services;

import java.util.List;

import com.getechnologies.Entities.VehicleStay;

public interface VehicleStayService {
	List<VehicleStay> findAll();
		
	VehicleStay save(VehicleStay vehicle);
		
	VehicleStay update(VehicleStay vehicle);
		
	VehicleStay delete(int id);
}
