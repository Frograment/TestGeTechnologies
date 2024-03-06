package com.getechnologies.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Official")
public class OfficialList {

	@Id
	String license_plate;

	public String getLicense_plate() {
		return license_plate;
	}

	public void setLicense_plate(String license_plate) {
		this.license_plate = license_plate;
	}

	public OfficialList(String license_plate) {
		this.license_plate = license_plate;
	}
	public OfficialList() {
		}
	
}
