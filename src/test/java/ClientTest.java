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
  public void getAvailabilityArray_returnsArrayofAvailabilityValues() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    ArrayList<Integer> testAvailabilities = new ArrayList<Integer>();
    testAvailabilities.add(64);
    testAvailabilities.add(8);
    testAvailabilities.add(4);
    testAvailabilities.add(2);
    testAvailabilities.add(1);
    assertEquals(testAvailabilities, testClient.getAvailabilityArray());
  }

  @Test
  public void getAvailabilityMatches_returnsAvailabilitesSharedWithStylist() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testClient.save();
    testStylist.save();
    ArrayList<Integer> testAvailabilities = new ArrayList<Integer>();
    testAvailabilities.add(2);
    assertEquals(testAvailabilities, testClient.getAvailabilityMatches(testStylist.getId()));
  }

  @Test
  public void getServiceArray_returnsArrayofServiceValues() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    ArrayList<Integer> testServices = new ArrayList<Integer>();
    testServices.add(2);
    assertEquals(testServices, testClient.getServiceArray());
  }

  @Test
  public void getServiceMatches_returnsServicesSharedWithStylist() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testClient.save();
    testStylist.save();
    ArrayList<Integer> testServices = new ArrayList<Integer>();
    testServices.add(2);
    assertEquals(testServices, testClient.getServiceMatches(testStylist.getId()));
  }

  @Test
  public void availabilityPrint_convertsAvailabilityNumbersToString() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    assertEquals("Monday Evening, Wednesday Daytime", testStylist.availabilityPrint());
  }

  @Test
  public void servicePrint_convertsServiceNumbersToString() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    assertEquals("Haircut, Coloring", testStylist.servicePrint());
  }

  @Test
  public void availabilityMatchPrint_printsAllAvailabilitiesMatchedBetweenClientAndStylist() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testClient.save();
    testStylist.save();
    assertEquals("Monday Evening", testClient.availabilityMatchPrint(testStylist.getId()));
  }

  @Test
  public void serviceMatchPrint_printsAllServicesMatchedBetweenClientAndStylist() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testClient.save();
    testStylist.save();
    assertEquals("Coloring", testClient.serviceMatchPrint(testStylist.getId()));
  }

  @Test
  public void getStylistMatches_returnsAllStylistsWithMatches() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Stylist testStylist = new Stylist("Erika", 18, 3);
    Stylist testStylist2 = new Stylist("April", 32, 4);
    testClient.save();
    testStylist.save();
    testStylist2.save();
    assertTrue(testClient.getStylistMatches().contains(testStylist));
    assertFalse(testClient.getStylistMatches().contains(testStylist2));
  }
}
