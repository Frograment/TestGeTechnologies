package com.getechnologies.Objects;

public class LicensePlateRequest {
    private String licensePlate;

	public LicensePlateRequest(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	public LicensePlateRequest() {}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

}
