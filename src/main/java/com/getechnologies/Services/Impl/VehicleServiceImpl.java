package com.getechnologies.Services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.getechnologies.Dao.VehicleDao;
import com.getechnologies.Entities.ResidentList;
import com.getechnologies.Entities.Vehicle;
import com.getechnologies.Repository.VehicleRepository;
import com.getechnologies.Services.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService{

	
	
	@Autowired
	private VehicleDao dao;
	
	
	
	@Override
	public List<Vehicle> findAll() {
		
		return (List<Vehicle>) dao.findAll();
	}

	@Override
	public Vehicle save(Vehicle vehicle) {
		
		return dao.save(vehicle);
	}

	@Override
	public Vehicle update(Vehicle vehicle) {
		
		return dao.save(vehicle);
	}

	@Override
	public Vehicle delete(String id) {
		Vehicle vehicle = dao.findById(id).orElse(new Vehicle());

		dao.deleteById(id);
		return vehicle;
	}
	
	@Override
	public Boolean existsById(String id) {
		
		return dao.existsById(id);
	}
	
	@Override
	public Vehicle findById(String id) {
		
		return dao.findById(id).orElse(new Vehicle());
	}

}
