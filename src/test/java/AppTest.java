import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.*;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();
  public WebDriver getDefaultDriver() {
      return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Are you a stylist or a client?");
  }

  @Test
  public void stylistDisplayTest() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    goTo("http://localhost:4567/stylists");
    assertThat(pageSource()).contains("Erika");
  }

  @Test
  public void stylistAddedTest() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", testStylist.getId());
    goTo(stylistPath);
    assertThat(pageSource()).contains("Monday Evening, Wednesday Daytime");
  }

  @Test
  public void stylistDisplaysClientsTest() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    testClient.addStylist(testStylist.getId());
    String stylistPath = String.format("http://localhost:4567/stylists/%d", testStylist.getId());
    goTo(stylistPath);
    assertThat(pageSource()).contains("(555) 555-5555");
  }

  @Test
  public void clientDisplayTest() {
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    goTo("http://localhost:4567/clients");
    assertThat(pageSource()).contains("Max");
  }

  @Test
  public void clientDisplaysStylistsTest() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    Client testClient = new Client("Max", 79, 2, "(555) 555-5555");
    testClient.save();
    testClient.addStylist(testClient.getId());
    String clientPath = String.format("http://localhost:4567/clients/%d", testClient.getId());
    goTo(clientPath);
    assertThat(pageSource()).contains("Erika");
  }

  @Test
  public void deleteAllTest() {
    Stylist testStylist = new Stylist("Erika", 18, 3);
    testStylist.save();
    goTo("http://localhost:4567/");
    submit("#clear");
    goTo("http://localhost:4567/stylists");
    assertThat(pageSource()).doesNotContain("Erika");
  }
}
