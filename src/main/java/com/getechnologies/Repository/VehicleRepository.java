package com.getechnologies.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.getechnologies.Entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle,String>  {

}
