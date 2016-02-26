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
}
