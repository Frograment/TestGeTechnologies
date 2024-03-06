package com.getechnologies.Dao;

import org.springframework.data.repository.CrudRepository;

import com.getechnologies.Entities.Vehicle;

public interface VehicleDao extends CrudRepository<Vehicle, String>{

}