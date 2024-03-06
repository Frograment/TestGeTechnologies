package com.getechnologies.Services;

import java.util.List;

import com.getechnologies.Entities.OfficialList;
import com.getechnologies.Entities.ResidentList;
public interface ResidentListService {
List<ResidentList> findAll();
	
ResidentList save(ResidentList vehicle);
	
ResidentList update(ResidentList vehicle);
	
ResidentList delete(String id);
Boolean existsById(String id);
}
