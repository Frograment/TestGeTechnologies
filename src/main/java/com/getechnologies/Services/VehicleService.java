package com.getechnologies.Services;

import java.util.List;

import com.getechnologies.Entities.Vehicle;

public interface VehicleService {
	List<Vehicle> findAll();
	
	Vehicle save(Vehicle vehicle);
	
	Vehicle update(Vehicle vehicle);
	
	Vehicle delete(String id);
	
	Boolean existsById(String id);
	
	Vehicle findById(String id);
}
