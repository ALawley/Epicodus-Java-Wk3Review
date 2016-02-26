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
}
