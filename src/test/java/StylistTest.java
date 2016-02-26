import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;

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
}
