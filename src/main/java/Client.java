import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
  private int id;
  private String name;
  private int availability;
  private int services_requested;
  private String phone;
  private int stylist_id;

  public Client (String name, int availability, int services_requested, String phone) {
    this.name = name;
    this.availability = availability;
    this.services_requested = services_requested;
    this.phone = phone;
  }


  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getAvailability() {
    return availability;
  }

  public int getServices() {
    return services_requested;
  }

  public String getPhone() {
    return phone;
  }

  public int getStylistId() {
    return stylist_id;
  }

  @Override
  public boolean equals(Object otherClient){
    if (!(otherClient instanceof Client)) {
      return false;
    } else {
      Client newClient = (Client) otherClient;
      return this.getName().equals(newClient.getName()) &&
        this.getId() == newClient.getId() &&
        this.getAvailability() == newClient.getAvailability() &&
        this.getServices() == newClient.getServices() &&
        this.getPhone().equals(newClient.getPhone());
    }
  }

  //CREATE
  public void save() {
    try (Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO clients(name, availability, services_requested, phone) VALUES (:name, :availability, :services_requested, :phone)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .addParameter("availability", availability)
      .addParameter("services_requested", services_requested)
      .addParameter("phone", phone)
      .executeUpdate()
      .getKey();
    }
  }

  //READ
  public static List<Client> all() {
    String sql = "SELECT * FROM clients";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Client.class);
    }
  }

  public static Client find(int id) {
    String sql = "SELECT * FROM clients WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Client.class);
    }
  }

  public ArrayList<Integer> getAvailabilityArray() {
    int counter = 8192;
    int availScore = availability;
    ArrayList<Integer> availabilities = new ArrayList<Integer>();
    while (availScore > 0) {
      if(availScore >= counter) {
        availabilities.add(counter);
        availScore -= counter;
      }
      counter /=2;
    }
    return availabilities;
  }

  public ArrayList<Integer> getAvailabilityMatches(int idOfStylist) {
    Stylist assignedStylist = Stylist.find(idOfStylist);
    ArrayList<Integer> matches = new ArrayList<Integer>();
    for (int clientOpening : this.getAvailabilityArray()) {
      for (int stylistOpening : assignedStylist.getAvailabilityArray()) {
        if (clientOpening == stylistOpening) {
          matches.add(clientOpening);
        }
      }
    }
    return matches;
  }

  public ArrayList<Integer> getServiceArray() {
    int counter = 4;
    int serviceScore = services_requested;
    ArrayList<Integer> allServices = new ArrayList<Integer>();
    while (serviceScore > 0) {
      if(serviceScore >= counter) {
        allServices.add(counter);
        serviceScore -= counter;
      }
      counter /=2;
    }
    return allServices;
  }

  public ArrayList<Integer> getServiceMatches(int idOfStylist) {
    Stylist assignedStylist = Stylist.find(idOfStylist);
    ArrayList<Integer> matches = new ArrayList<Integer>();
    for (int clientRequest : this.getServiceArray()) {
      for (int stylistService : assignedStylist.getServiceArray()) {
        if (clientRequest == stylistService) {
          matches.add(clientRequest);
        }
      }
    }
    return matches;
  }

  public ArrayList<Stylist> getStylistMatches() {
    ArrayList<Stylist> matches = new ArrayList<Stylist>();
    for (Stylist stylist : Stylist.all()) {
      if (this.getServiceMatches(stylist.getId()).size() > 0 && this.getAvailabilityMatches(stylist.getId()).size() > 0) {
        matches.add(stylist);
      }
    }
    return matches;
  }

  public String availabilityPrint() {
    ArrayList<Integer> availabilities = this.getAvailabilityArray();
    String result = "";
    HashMap<Integer, String> availValues = new HashMap<Integer, String>();
    availValues.put(1, "Monday Daytime");
    availValues.put(2, "Monday Evening");
    availValues.put(4, "Tuesday Daytime");
    availValues.put(8, "Tuesday Evening");
    availValues.put(16, "Wednesday Daytime");
    availValues.put(32, "Wednesday Evening");
    availValues.put(64, "Thursday Daytime");
    availValues.put(128, "Thursday Evening");
    availValues.put(256, "Friday Daytime");
    availValues.put(512, "Friday Evening");
    availValues.put(1024, "Saturday Daytime");
    availValues.put(2048, "Saturday Evening");
    availValues.put(4096, "Sunday Daytime");
    availValues.put(8192, "Sunday Evening");

    for (int avail : availabilities) {
      if (result == "") {
        result = availValues.get(avail);
      } else {
        result = availValues.get(avail) + ", " + result;
      }
    }
    return result;
  }

  public String availabilityMatchPrint(int idOfStylist) {
    ArrayList<Integer> availabilities = this.getAvailabilityMatches(idOfStylist);
    String result = "";
    HashMap<Integer, String> availValues = new HashMap<Integer, String>();
    availValues.put(1, "Monday Daytime");
    availValues.put(2, "Monday Evening");
    availValues.put(4, "Tuesday Daytime");
    availValues.put(8, "Tuesday Evening");
    availValues.put(16, "Wednesday Daytime");
    availValues.put(32, "Wednesday Evening");
    availValues.put(64, "Thursday Daytime");
    availValues.put(128, "Thursday Evening");
    availValues.put(256, "Friday Daytime");
    availValues.put(512, "Friday Evening");
    availValues.put(1024, "Saturday Daytime");
    availValues.put(2048, "Saturday Evening");
    availValues.put(4096, "Sunday Daytime");
    availValues.put(8192, "Sunday Evening");

    for (int avail : availabilities) {
      if (result == "") {
        result = availValues.get(avail);
      } else {
        result = availValues.get(avail) + ", " + result;
      }
    }
    return result;
  }

  public String servicePrint() {
    ArrayList<Integer> serviceIds = this.getServiceArray();
    String result = "";
    HashMap<Integer, String> serviceValues = new HashMap<Integer, String>();
    serviceValues.put(1, "Haircut");
    serviceValues.put(2, "Coloring");
    serviceValues.put(4, "Perm");

    for (int serviceId : serviceIds) {
      if (result == "") {
        result = serviceValues.get(serviceId);
      } else {
        result = serviceValues.get(serviceId) + ", " + result;
      }
    }
    return result;
  }

  public String serviceMatchPrint(int idOfStylist) {
    ArrayList<Integer> serviceIds = this.getServiceMatches(idOfStylist);
    String result = "";
    HashMap<Integer, String> serviceValues = new HashMap<Integer, String>();
    serviceValues.put(1, "Haircut");
    serviceValues.put(2, "Coloring");
    serviceValues.put(4, "Perm");

    for (int serviceId : serviceIds) {
      if (result == "") {
        result = serviceValues.get(serviceId);
      } else {
        result = serviceValues.get(serviceId) + ", " + result;
      }
    }
    return result;
  }

  //UPDATE
  public void updateName(String newName) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET name = :newName WHERE id = :id";
      con.createQuery(sql)
        .addParameter("newName", newName)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateAvailability(int newAvailability) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET availability = :newAvailability WHERE id = :id";
      con.createQuery(sql)
        .addParameter("newAvailability", newAvailability)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateServices(int newServices) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET services_requested = :newServices WHERE id = :id";
      con.createQuery(sql)
        .addParameter("newServices", newServices)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updatePhone(String newPhone) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET phone = :newPhone WHERE id = :id";
      con.createQuery(sql)
        .addParameter("newPhone", newPhone)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void addStylist(int stylist_id) {
    try (Connection con = DB.sql2o.open()) {
    String sql = "UPDATE clients SET stylist_id = :stylist_id WHERE id = :id";
    con.createQuery(sql)
      .addParameter("stylist_id", stylist_id)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void clearStylist() {
    try (Connection con = DB.sql2o.open()) {
    String sql = "UPDATE clients SET stylist_id = NULL WHERE id = :id";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  //DELETE
  public void delete() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public static void deleteAll() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients *";
      con.createQuery(sql).executeUpdate();
    }
  }

}
