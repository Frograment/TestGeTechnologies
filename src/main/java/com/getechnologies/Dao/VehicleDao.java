package com.getechnologies.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.getechnologies.Entities.Vehicle;

public interface VehicleDao extends CrudRepository<Vehicle, String>{

}