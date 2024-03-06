package com.getechnologies.Services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.getechnologies.Dao.OfficialListDao;
import com.getechnologies.Dao.ResidentListDao;
import com.getechnologies.Dao.VehicleDao;
import com.getechnologies.Entities.OfficialList;
import com.getechnologies.Entities.ResidentList;
import com.getechnologies.Entities.Vehicle;
import com.getechnologies.Repository.VehicleRepository;
import com.getechnologies.Services.OfficialListService;
import com.getechnologies.Services.ResidentListService;
import com.getechnologies.Services.VehicleService;

@Service
public class ResidentListServiceImpl implements ResidentListService{

	
	
	@Autowired
	private ResidentListDao dao;
	
	
	
	@Override
	public List<ResidentList> findAll() {
		
		return (List<ResidentList>) dao.findAll();
	}

	@Override
	public ResidentList save(ResidentList residentList) {
		
		return dao.save(residentList);
	}

	@Override
	public ResidentList update(ResidentList residentList) {
		
		return dao.save(residentList);
	}

	@Override
	public ResidentList delete(String id) {
		ResidentList residentList = dao.findById(id).orElse(new ResidentList());

		dao.deleteById(id);
		return residentList;
	}
	@Override
	public Boolean existsById(String id) {
		
		return dao.existsById(id);
	}

}
