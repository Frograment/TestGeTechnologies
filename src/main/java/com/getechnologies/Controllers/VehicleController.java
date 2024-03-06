package com.getechnologies.Controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.getechnologies.Entities.OfficialList;
import com.getechnologies.Entities.ResidentList;
import com.getechnologies.Entities.Vehicle;
import com.getechnologies.Entities.VehicleStay;
import com.getechnologies.Objects.FileNameRequest;
import com.getechnologies.Objects.LicensePlateRequest;
import com.getechnologies.Objects.Response;
import com.getechnologies.Services.OfficialListService;
import com.getechnologies.Services.ResidentListService;
import com.getechnologies.Services.VehicleService;
import com.getechnologies.Services.VehicleStayService;

@RestController
@RequestMapping()
public class VehicleController {
	
	final int MILISECONDS = 1000;
	final int MINUTES = 60;
	final int ONE = 1;
	final int ZERO = 0;
	
	final String ATTACHMENT_AND_FILENAME =  "attachment; filename=\"";
	final String COLUMNS_TTILES_FILE_TEXT = "Núm. placa	Tiempo estacionado (min.)	Cantidad a pagar";
	final String CONTENT_DISPOSITION = "Content-Disposition";
	final String CONTENT_TYPE = "text/plain";
	final String CURRENCY = "MX";
	final String EMPTY_STRING = "";
	final String ENTRY_CREATED = "Entrada creada.";
	final String NOT_ENTRY_YET_MESSAGE = "El vehiculo aun no ha entrado";
	final String NO_RESIDENT = "No-Resident";
	final String NO_RESIDENT_MESSAGE = "La cantidad a pagar es de: ";
	final String OFFICIAL = "Official";
	final String OFFICIAL_MESSAGE = "Se ha registrado la entrada y salida del vehiculo oficial.";
	final String RESIDENT = "Resident";
	final String RESIDENT_MESSAGE = "Los minutos del residente han sido actualizados";
	final String SLASH = "\"";
	final String VEHICLE_ALREADY_INSIDE = "El vehiculo ya ha ingresado.";
	final String VEHICLE_NOT_FOUND_MESSAGE = "La placa del coche no eXiste";
	final String WHITE_SPACE = " ";
	
	final double RESIDENT_COST = 0.05;
	final double NO_RESIDENT_COST = 0.5;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private OfficialListService officialListService;

	@Autowired
	private ResidentListService residentListService;

	@Autowired
	private VehicleStayService vehicleStayService;

	@GetMapping("/vehicles")
	public List<Vehicle> getVehicles() {
		return vehicleService.findAll();
	}

	private VehicleStay getLastEntry(String licensePlate) {
		List<VehicleStay> vehicleStays = vehicleStayService.findAll().stream()
				.filter(veh -> veh.getLicense_plate().equals(licensePlate)).toList();
		
		if (vehicleStays.size() == ZERO)
			return null;
		return vehicleStays.get(vehicleStays.size() - ONE);
	}

	private Date getCurrentDate(){
		LocalDateTime localDateTime = LocalDateTime.now();
		return Timestamp.valueOf(localDateTime);
	}

	@PostMapping("/createEntry")
	public Response createEntry(@RequestBody LicensePlateRequest request) {
		Response response = new Response();
		Vehicle vehicle = new Vehicle();
		if (!vehicleService.existsById(request.getLicensePlate())) {
			vehicle = new Vehicle(request.getLicensePlate(), NO_RESIDENT, ZERO, ZERO);
			if (officialListService.existsById(request.getLicensePlate())) {
				vehicle.setType(OFFICIAL);
			} else if (residentListService.existsById(request.getLicensePlate())) {
				vehicle.setType(RESIDENT);
			}
			vehicleService.save(vehicle);
		}

		VehicleStay current = getLastEntry(request.getLicensePlate());
		if (current == null || current.getEgress() != null) {
			Date date = getCurrentDate();
			VehicleStay vehicleStay = new VehicleStay(request.getLicensePlate(), date, null);
			vehicleStayService.save(vehicleStay);
		} else {
			response.setMessage(NOT_ENTRY_YET_MESSAGE);
			return response;
		}
		response.setMessage(ENTRY_CREATED);
		return response;
	}

	@PostMapping("/createEgress")
	public Response createEgress(@RequestBody LicensePlateRequest request) {
		Response response = new Response();
		Vehicle vehicle = new Vehicle();
		
		if (!vehicleService.existsById(request.getLicensePlate())) {
			response.setMessage(VEHICLE_NOT_FOUND_MESSAGE);
		} else {
			vehicle = vehicleService.findById(request.getLicensePlate());

			VehicleStay current = getLastEntry(request.getLicensePlate());
			if (current.getEgress() == null) {
				Date date = getCurrentDate();
				current.setEgress(date);
				vehicleStayService.update(current);
			

			switch (vehicle.getType()) {
			case OFFICIAL:
				response.setMessage(OFFICIAL_MESSAGE);
				break;
			case RESIDENT:
				vehicle.setTotalTime(vehicle.getTotalTime()
						+ (int) ((current.getEgress().getTime() - current.getEntry().getTime()) / (MILISECONDS*MINUTES)));
				vehicle.setAmount(vehicle.getTotalTime() * RESIDENT_COST);
				vehicleService.update(vehicle);
				response.setMessage(RESIDENT_MESSAGE);
				break;
			case NO_RESIDENT:
				int minutes = vehicle.getTotalTime()
						+ (int) ((current.getEgress().getTime() - current.getEntry().getTime()) / (MILISECONDS*MINUTES));
				double amount = minutes * NO_RESIDENT_COST;
				response.setMessage(NO_RESIDENT_MESSAGE + amount + WHITE_SPACE + CURRENCY);
				vehicle.setAmount(ZERO);
				vehicle.setTotalTime(ZERO);
				vehicleService.update(vehicle);
				break;
			}
			} else {
				response.setMessage(NOT_ENTRY_YET_MESSAGE);
			}
		}
		
		return response;
	}

	private void SetVehicleType(String licensePlate, String type){
		if (vehicleService.existsById(licensePlate)) {
			Vehicle vehicle = vehicleService.findById(licensePlate);
			vehicle.setType(type);
		}
	}


	@PostMapping("/addOfficialVehicle")
	public OfficialList addOfficialVehicle(@RequestBody LicensePlateRequest request) {
		SetVehicleType(request.getLicensePlate(),OFFICIAL);
		return officialListService.save(new OfficialList(request.getLicensePlate()));
	}

	@PostMapping("/addResidentVehicle")
	public ResidentList addResidentVehicle(@RequestBody LicensePlateRequest request) {
		SetVehicleType(request.getLicensePlate(),RESIDENT);
		return residentListService.save(new ResidentList(request.getLicensePlate()));
	}

	@PostMapping("/beginMonth")
	public void beginMonth() {
		List<String> officialVehicles = vehicleService.findAll()
				.stream()
				.map(vehi -> {
					if(vehi.getType().equals(OFFICIAL)) {
						return vehi.getLicensePlate();
					}
					return EMPTY_STRING;
				})
				.collect(Collectors.toList());
		
		vehicleService.findAll()
				.stream()
				.forEach(vehi -> {
					if(vehi.getType().equals(RESIDENT)) {
						vehi.setAmount(ZERO);
						vehi.setTotalTime(ZERO);
						vehicleService.update(vehi);
					}
					
				});
		
		vehicleStayService.findAll()
		.forEach(stay -> {
			if(officialVehicles.contains(stay.getLicense_plate())) {
				vehicleStayService.delete(stay.getId());
			}
		});
	}

	@PostMapping("/resident-payments")
    public void residentPayments(@RequestBody FileNameRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(CONTENT_TYPE);
        response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_AND_FILENAME+request.getFile_name()+SLASH);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            writer.write(COLUMNS_TTILES_FILE_TEXT);
            writer.newLine();
        	vehicleService.findAll().forEach(vehi -> {
        		if(vehi.getType().equals(RESIDENT)) {
        			try {
						writer.write(vehi.toString());
						writer.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	});
            
            
        }
    }




}
