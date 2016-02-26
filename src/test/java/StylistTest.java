import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;
import java.util.ArrayList;

public class StylistTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Stylist.all().size(), 0);
  }

  @Test
  public void save_addsStylistToList() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    assertEquals(1, Stylist.all().size());
  }

  @Test
  public void updateName_changesStylistName() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    testStylist.updateName("Jonathan");
    Stylist savedStylist = Stylist.find(testStylist.getId());
    assertEquals("Jonathan", savedStylist.getName());
  }

  @Test
  public void updateAvailability_changesStylistAvailability() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    testStylist.updateAvailability(19);
    Stylist savedStylist = Stylist.find(testStylist.getId());
    assertEquals(19, savedStylist.getAvailability());
  }

  @Test
  public void updateServices_changesStylistServices() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    testStylist.updateServices(2);
    Stylist savedStylist = Stylist.find(testStylist.getId());
    assertEquals(2, savedStylist.getServices());
  }

  @Test
  public void delete_deleteStylist() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    testStylist.delete();
    assertEquals(0, Stylist.all().size());
  }

  @Test
  public void find_getStylistById() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    assertEquals(Stylist.find(testStylist.getId()), testStylist);
  }

  @Test
  public void getAvailabilityArray_returnsArrayofAvailabilityValues() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    ArrayList<Integer> testAvailabilities = new ArrayList<Integer>();
    testAvailabilities.add(16);
    testAvailabilities.add(2);
    assertEquals(testAvailabilities, testStylist.getAvailabilityArray());
  }

  @Test
  public void availabilityPrint_convertsAvailabilityNumbersToString() {
    ArrayList<Integer> testAvailabilities = new ArrayList<Integer>();
    testAvailabilities.add(16);
    testAvailabilities.add(2);
    assertEquals("Monday Evening, Wednesday Daytime", Stylist.availabilityPrint(testAvailabilities));
  }

  @Test
  public void servicePrint_convertsServiceNumbersToString() {
    ArrayList<Integer> testServices = new ArrayList<Integer>();
    testServices.add(2);
    testServices.add(1);
    assertEquals("Haircut, Coloring", Stylist.servicePrint(testServices));
  }

  @Test
  public void getServiceArray_returnsArrayofServiceValues() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    ArrayList<Integer> testServices = new ArrayList<Integer>();
    testServices.add(2);
    testServices.add(1);
    assertEquals(testServices, testStylist.getServiceArray());
  }

  @Test
  public void getClients_returnsAllClientsAssignedToStylist() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    Client testClient2 = new Client("Sarah", 8134, 5, "(555) 555-5555");
    testClient.save();
    testClient2.save();
    testClient.addStylist(testStylist.getId());
    testClient2.addStylist(testStylist.getId());
    assertEquals(2, testStylist.getClients().size());
  }
}
