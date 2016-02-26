import org.sql2o.*;
import java.util.List;

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
  //DELETE
  public void delete() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
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
}
