package com.getechnologies.Entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Vehicle")
public class Vehicle {
	
	@Id
	String license_plate;
	String type;
	double amount;
	int total_time;
	
	public Vehicle() {
	}

	public Vehicle(String license_plate, String type, double amount, int total_time) {
		super();
		this.license_plate = license_plate;
		this.type = type;
		this.amount = amount;
		this.total_time = total_time;
	}

	public String getLicensePlate() {
		return license_plate;
	}

	public void setLicensePlate(String licensePlate) {
		this.license_plate = licensePlate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTotalTime() {
		return total_time;
	}

	public void setTotalTime(int totalTime) {
		this.total_time = totalTime;
	}

	@Override
	public String toString() {
		final String WHITE_SPACE = "	";
		return license_plate + WHITE_SPACE + WHITE_SPACE + WHITE_SPACE + 
			   total_time + WHITE_SPACE + WHITE_SPACE + WHITE_SPACE + WHITE_SPACE + WHITE_SPACE + WHITE_SPACE + WHITE_SPACE + 
			   amount;
	}
	
	
	
	
}
