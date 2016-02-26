import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Stylist {
  private int id;
  private String name;
  private int availability;
  private int services;

  public Stylist (String name, int availability, int services) {
    this.name = name;
    this.availability = availability;
    this.services = services;
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
    return services;
  }

  @Override
  public boolean equals(Object otherStylist){
    if (!(otherStylist instanceof Stylist)) {
      return false;
    } else {
      Stylist newStylist = (Stylist) otherStylist;
      return this.getName().equals(newStylist.getName()) &&
        this.getId() == newStylist.getId() &&
        this.getAvailability() == newStylist.getAvailability() &&
        this.getServices() == newStylist.getServices();
    }
  }

  //CREATE
  public void save() {
    try (Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO stylists(name, availability, services) VALUES (:name, :availability, :services)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .addParameter("availability", availability)
      .addParameter("services", services)
      .executeUpdate()
      .getKey();
    }
  }

  //READ
  public static List<Stylist> all() {
    String sql = "SELECT * FROM stylists";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  public static Stylist find(int id) {
    String sql = "SELECT * FROM stylists WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Stylist.class);
    }
  }

  public List<Client> getClients() {
    String sql = "SELECT * FROM clients WHERE stylist_id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Client.class);
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

  public ArrayList<Integer> getServiceArray() {
    int counter = 4;
    int serviceScore = services;
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

  //UPDATE
  public void updateName(String newName) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stylists SET name = :newName WHERE id = :id";
      con.createQuery(sql)
        .addParameter("newName", newName)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateAvailability(int newAvailability) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stylists SET availability = :newAvailability WHERE id = :id";
      con.createQuery(sql)
        .addParameter("newAvailability", newAvailability)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateServices(int newServices) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stylists SET services = :newServices WHERE id = :id";
      con.createQuery(sql)
        .addParameter("newServices", newServices)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
  //DELETE
  public void delete() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM stylists WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
}
