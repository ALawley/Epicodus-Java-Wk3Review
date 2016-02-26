import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;
import java.util.ArrayList;

public class ClientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Client.all().size(), 0);
  }

  @Test
  public void save_addsClientToList() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    assertEquals(1, Client.all().size());
  }

  @Test
  public void updateName_changesClientName() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    testClient.updateName("Jonathan");
    Client savedClient = Client.find(testClient.getId());
    assertEquals("Jonathan", savedClient.getName());
  }

  @Test
  public void updateAvailability_changesClientAvailability() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    testClient.updateAvailability(19);
    Client savedClient = Client.find(testClient.getId());
    assertEquals(19, savedClient.getAvailability());
  }

  @Test
  public void updateServices_changesClientServices() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    testClient.updateServices(2);
    Client savedClient = Client.find(testClient.getId());
    assertEquals(2, savedClient.getServices());
  }

  @Test
  public void updatePhone_changesClientPhone() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    testClient.updatePhone("(333) 333-3333");
    Client savedClient = Client.find(testClient.getId());
    assertEquals("(333) 333-3333", savedClient.getPhone());
  }

  @Test
  public void delete_deleteClient() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    testClient.delete();
    assertEquals(0, Client.all().size());
  }

  @Test
  public void find_getClientById() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    assertEquals(Client.find(testClient.getId()), testClient);
  }

  @Test
  public void addStylist_assignStylistToClient() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testClient.save();
    testStylist.save();
    testClient.addStylist(testStylist.getId());
    Client savedClient = Client.find(testClient.getId());
    assertEquals(savedClient.getStylistId(), testStylist.getId());
  }

  @Test
  public void clearStylist_removesAssignedStylistFromClient() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testClient.save();
    testStylist.save();
    testClient.addStylist(testStylist.getId());
    testClient.clearStylist();
    Client savedClient = Client.find(testClient.getId());
    assertEquals(0, savedClient.getStylistId());
  }

  @Test
  public void availabilityArray_returnsArrayofAvailabilityValues() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    ArrayList<Integer> testAvailabilities = new ArrayList<Integer>();
    testAvailabilities.add(64);
    testAvailabilities.add(8);
    testAvailabilities.add(4);
    testAvailabilities.add(2);
    testAvailabilities.add(1);
    assertEquals(testAvailabilities, testClient.availabilityArray());
  }

  @Test
  public void availabilityMatches_returnsAvailabilitesSharedWithStylist() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testClient.save();
    testStylist.save();
    testClient.addStylist(testStylist.getId());
    ArrayList<Integer> testAvailabilities = new ArrayList<Integer>();
    testAvailabilities.add(2);
    Client savedClient = Client.find(testClient.getId());
    assertEquals(testAvailabilities, savedClient.availabilityMatches());
  }
}
