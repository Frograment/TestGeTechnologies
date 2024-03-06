package com.getechnologies.Entities;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vehicle_stay")
public class VehicleStay {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	String license_plate;
	Date entry;
	Date egress;
	
	
	
	public VehicleStay() {
		super();
	}
	public VehicleStay(String license_plate, Date entry, Date egress) {

		this.license_plate = license_plate;
		this.entry = entry;
		this.egress = egress;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLicense_plate() {
		return license_plate;
	}
	public void setLicense_plate(String license_plate) {
		this.license_plate = license_plate;
	}
	public Date getEntry() {
		return entry;
	}
	public void setEntry(Date entry) {
		this.entry = entry;
	}
	public Date getEgress() {
		return egress;
	}
	public void setEgress(Date egress) {
		this.egress = egress;
	}
	
}
