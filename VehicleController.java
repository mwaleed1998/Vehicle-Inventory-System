import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VehicleController {
	static VehicleDAO dao = new VehicleDAO();
	static Scanner input = new Scanner(System.in);

	public static void retrieveVehicles() {
		System.out.println("Retrieving all vehicles");
		ArrayList<Vehicle> vehicles;
		try {
			vehicles = dao.getAllVehicles();
			for (int i = 0; i < vehicles.size(); i++) {
				System.out.println("---------------------------");
				System.out.println(vehicles.get(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("---------------------------");
	}

	public static void retrieveVehicle() {
		System.out.println("VIEW VEHICLE");
		System.out.println("---------------------------");
		System.out.println("Enter a vehicle id: ");
		int vehicle_id = input.nextInt();
		Vehicle vehicle = null;
		try {
			vehicle = dao.getVehicle(vehicle_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		System.out.println("---------------------------");
		if (vehicle == null) {
			System.out.printf("No vehicle of id: %d was found\n", vehicle_id);
		} else {
			System.out.println(vehicle);
		}
		System.out.println("---------------------------");

	}

	public static void deleteVehicle() {
		System.out.println("DELETE VEHICLE");
		System.out.println("---------------------------");
		System.out.println("Enter the vehicle id:");
		int vehicle_id = input.nextInt();
		Boolean deleted = false;
		try {
			deleted = dao.deleteVehicle(vehicle_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		System.out.println("---------------------------");
		if (deleted == false) {
			System.out.printf("No vehicle of id: %d was deleted\n", vehicle_id);
		} else {
			System.out.printf("Vehicle of id: %d was deleted \n", vehicle_id);
		}
		System.out.println("---------------------------");
	}

	public static void formatField(String field) {
		if (field == "transmission") {
			System.out.println("Enter vehicle " + field.replace('_', ' ') + " type (manual/automatic):");
		} else if (field == "fuel_type") {
			System.out.println("Enter vehicle " + field.replace('_', ' ') + " (petrol,diesel,hybrid,electric):");
		} else if (field == "engine_size") {
			System.out.println("Enter vehicle " + field.replace('_', ' ') + " (cc):");
		} else if (field == "body_style") {
			System.out.println("Enter vehicle " + field.replace('_', ' ') + " (hatchback, estate, SUV, MVP coupe):");
		} else if (field == "condition") {
			System.out.println("Enter vehicle " + field.replace('_', ' ') + " (e.g. like new, good, fair):");
		} else if (field == "notes") {
			System.out.println("Enter vehicle " + field.replace('_', ' ') + " (special features such as sat nav):");
		} else {
			System.out.println("Enter vehicle " + field.replace('_', ' ') + ":");
		}
	}

	public static Boolean isFieldInt(String field) {
		String[] intFields = new String[] { "year", "price", "number_doors", "mileage", "engine_size" };
		for (int i = 0; i < intFields.length; i++) {
			if (field == intFields[i]) {
				return true;
			}
		}
		return false;
	}

	public static void insertVehicle() {
		Scanner in = new Scanner(System.in);
		System.out.println("INSERT VEHICLE");
		System.out.println("---------------------------");
		Boolean inserted = false;
		String[] fields = new String[] { "make", "model", "year", "price", "license_number", "colour", "number_doors",
				"transmission", "mileage", "fuel_type", "engine_size", "body_style", "condition", "notes" };
		Map<String, String> stringValues = new HashMap<>();
		Map<String, Integer> intValues = new HashMap<>();
		for (int i = 0; i < fields.length; i++) {
			formatField(fields[i]);
			if (isFieldInt(fields[i])) {
				Boolean validInput = false;
				while(!validInput) {
					try {
						intValues.put(fields[i], in.nextInt());
						in.nextLine();
						validInput = true;
					} catch (Exception e) {
						System.out.println("Must be an integer");
						in.nextLine();
						formatField(fields[i]);
					}
				}
				
				
			} else {
				stringValues.put(fields[i], in.nextLine().toUpperCase());
			}
		}
		System.out.println(intValues);
		int latest_vehicle_id = -1;
		try {
			latest_vehicle_id = dao.getLatestVehicle_id();
		} catch (SQLException e) {
			System.out.println(e);
		} 
		Vehicle vehicle = new Vehicle(latest_vehicle_id+1, stringValues.get("make"), stringValues.get("model"),
				intValues.get("year"), intValues.get("price"), stringValues.get("license_number"),
				stringValues.get("colour"), intValues.get("number_doors"), stringValues.get("transmission"),
				intValues.get("mileage"), stringValues.get("fuel_type"), intValues.get("engine_size"),
				stringValues.get("body_style"), stringValues.get("condition"), stringValues.get("notes"));
		try {
			inserted = dao.insertVehicle(vehicle);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public static void updateVehicle() {
		System.out.println("UPDATE VEHICLE");
		System.out.println("---------------------------");
		System.out.println("Enter the vehicle id:");
		int vehicle_id = input.nextInt();
		Boolean updated = false;

	}

	public static void main(String[] args) {
		Boolean exit = false;
		while (!exit) {
			try {
				int n = 0;
				String menu = "Vehicle Inventory System\r\n" + "Choose from these options\r\n"
						+ "-------------------------\r\n" + "1 - Retrieve all vehicles\r\n"
						+ "2 - Search for vehicle\r\n" + "3 - Insert new vehicle into database\r\n"
						+ "4 - Update existing vehicle details\r\n" + "5 - Delete vehicle from database\r\n"
						+ "6 - Exit\r\n" + "Enter choice > ";
				System.out.println(menu);
				n = input.nextInt();

				if (n < 1 || n > 6) {
					System.out.println("Not a valid option");
				}

				if (n == 1) {
					retrieveVehicles();
				} else if (n == 2) {
					retrieveVehicle();

				} else if (n == 3) {
					insertVehicle();

				} else if (n == 4) {
					updateVehicle();

				} else if (n == 5) {
					deleteVehicle();
				} else if (n == 6) {
					exit = true;
				}
			} catch (java.util.InputMismatchException e) {
				System.out.println("Not a valid input or option");
			}
		}

	}
}
