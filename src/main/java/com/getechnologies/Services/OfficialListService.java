package com.getechnologies.Services;

import java.util.List;

import com.getechnologies.Entities.OfficialList;
public interface OfficialListService {
List<OfficialList> findAll();
	
OfficialList save(OfficialList vehicle);
	
OfficialList update(OfficialList vehicle);
	
OfficialList delete(String id);

Boolean existsById(String id);
}
