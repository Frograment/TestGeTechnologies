package com.getechnologies.Controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import com.getechnologies.Services.OfficialListService;
import com.getechnologies.Services.ResidentListService;
import com.getechnologies.Services.VehicleService;
import com.getechnologies.Services.VehicleStayService;
import com.getechnologies.Services.Impl.VehicleServiceImpl;

@RestController
@RequestMapping()
public class VehicleController {
	
	final int MILISECONDS = 1000;
	final int MINUTES = 60;

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
		
		if (vehicleStays.size() == 0)
			return null;
		return vehicleStays.get(vehicleStays.size() - 1);
	}

	@PostMapping("/createEntry")
	public String createEntry(@RequestBody LicensePlateRequest request) {
		Vehicle vehicle = new Vehicle();
		if (!vehicleService.existsById(request.getLicensePlate())) {
			vehicle = new Vehicle(request.getLicensePlate(), "No-Resident", 0.0, 0);
			if (officialListService.existsById(request.getLicensePlate())) {
				vehicle.setType("Official");
			} else if (residentListService.existsById(request.getLicensePlate())) {
				vehicle.setType("Resident");
			}
			vehicleService.save(vehicle);
		}

		VehicleStay current = getLastEntry(request.getLicensePlate());
		if (current == null || current.getEgress() != null) {
			LocalDateTime localDateTime = LocalDateTime.now();
			Date date = java.sql.Timestamp.valueOf(localDateTime);
			VehicleStay vehicleStay = new VehicleStay(request.getLicensePlate(), date, null);
			vehicleStayService.save(vehicleStay);
		} else {
			return "El vehiculo ya ha ingresado.";
		}
		return "Entrada creada.";
	}

	@PostMapping("/createEgress")
	public String createEgress(@RequestBody LicensePlateRequest request) {
		String message = "Se ha registrado la entrada y salida.";
		Vehicle vehicle = new Vehicle();
		if (!vehicleService.existsById(request.getLicensePlate())) {
			message = "La placa del coche no esiste";
		} else {
			vehicle = vehicleService.findById(request.getLicensePlate());

			VehicleStay current = getLastEntry(request.getLicensePlate());
			if (current.getEgress() == null) {
				LocalDateTime localDateTime = LocalDateTime.now();
				Date date = java.sql.Timestamp.valueOf(localDateTime);
				current.setEgress(date);
				vehicleStayService.update(current);
			

			switch (vehicle.getType()) {

			case "Resident":
				vehicle.setTotalTime(vehicle.getTotalTime()
						+ (int) ((current.getEgress().getTime() - current.getEntry().getTime()) / (MILISECONDS*MINUTES)));
				vehicle.setAmount(vehicle.getTotalTime() * 0.05);
				vehicleService.update(vehicle);
				message = "Los minutos del residente han sido actualizados";
				break;
			case "No-Resident":

				// vehicle.setTotalTime(vehicle.getTotalTime()+(int)((current.getEgress().getTime()-current.getEntry().getTime())/3600));
				// vehicle.setAmount(vehicle.getTotalTime()*0.5);
				// vehicleService.update(vehicle);
				int minutes = vehicle.getTotalTime()
						+ (int) ((current.getEgress().getTime() - current.getEntry().getTime()) / (MILISECONDS*MINUTES));
				double amount = minutes * 0.5;
				message = "La cantidad a pagar es de: " + amount + " MX";
				vehicle.setAmount(0.0);
				vehicle.setTotalTime(0);
				vehicleService.update(vehicle);
				break;
			}
			} else {
				message =  "El vehiculo aun no ha entrado";
			}
		}
		return message;
	}

	@PostMapping("/addOfficialVehicle")
	public OfficialList addOfficialVehicle(@RequestBody LicensePlateRequest licensePlate) {
		if (vehicleService.existsById(licensePlate.getLicensePlate())) {
			Vehicle vehicle = vehicleService.findById(licensePlate.getLicensePlate());
			vehicle.setType("Official");
		}
		return officialListService.save(new OfficialList(licensePlate.getLicensePlate()));
	}

	@PostMapping("/addResidentVehicle")
	public ResidentList addResidentVehicle(@RequestBody LicensePlateRequest licensePlate) {
		if (vehicleService.existsById(licensePlate.getLicensePlate())) {
			Vehicle vehicle = vehicleService.findById(licensePlate.getLicensePlate());
			vehicle.setType("Resident");
		}
		return residentListService.save(new ResidentList(licensePlate.getLicensePlate()));
	}

	@PostMapping("/beginMonth")
	public void beginMonth() {
		List<String> officialVehicles = vehicleService.findAll()
				.stream()
				.map(vehi -> {
					if(vehi.getType().equals("Official")) {
						return vehi.getLicensePlate();
					}
					return "";
				})
				.collect(Collectors.toList());
		
		vehicleService.findAll()
				.stream()
				.forEach(vehi -> {
					if(vehi.getType().equals("Resident")) {
						vehi.setAmount(0);
						vehi.setTotalTime(0);
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
    public void generateFile(@RequestBody FileNameRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\""+request.getFile_name()+"\"");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            writer.write("Núm. placa	Tiempo estacionado (min.)	Cantidad a pagar");
            writer.newLine();
        	vehicleService.findAll().forEach(vehi -> {
        		if(vehi.getType().equals("Resident")) {
        			try {
						writer.write(vehi.toString());
						writer.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	});
            
            
        }
    }




}
