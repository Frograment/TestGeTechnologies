package com.getechnologies.Services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.getechnologies.Dao.OfficialListDao;
import com.getechnologies.Dao.VehicleDao;
import com.getechnologies.Entities.OfficialList;
import com.getechnologies.Entities.Vehicle;
import com.getechnologies.Services.OfficialListService;
import com.getechnologies.Services.VehicleService;

@Service
public class OfficialListServiceImpl implements OfficialListService{

	@Autowired
	private OfficialListDao dao;
	
	@Override
	public List<OfficialList> findAll() {
		return (List<OfficialList>) dao.findAll();
	}

	@Override
	public OfficialList save(OfficialList officialList) {
		return dao.save(officialList);
	}

	@Override
	public OfficialList update(OfficialList officialList) {
		return dao.save(officialList);
	}

	@Override
	public OfficialList delete(String id) {
		OfficialList officialList = dao.findById(id).orElse(new OfficialList());
		dao.deleteById(id);
		return officialList;
	}
	@Override
	public Boolean existsById(String id) {
		return dao.existsById(id);
	}
}
