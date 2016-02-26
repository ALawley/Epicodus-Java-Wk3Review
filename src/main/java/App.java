import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;

public class App {
  public static void main(String[] args) {
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int availabilityValue = 0;
      int servicesValue = 0;
      String[] availabilities = request.queryParamsValues("availability");
      for (String availability : availabilities) {
        availabilityValue += Integer.parseInt(availability);
      }
      String[] services = request.queryParamsValues("service");
      for (String service : services) {
        servicesValue += Integer.parseInt(service);
      }
      String stylistName = request.queryParams("name");
      Stylist newStylist = new Stylist(stylistName, availabilityValue, servicesValue);
      newStylist.save();
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Stylist newStylist = Stylist.find(Integer.parseInt(request.params(":id")));
      List<Client> clients = newStylist.getClients();

      model.put("stylist", newStylist);
      model.put("clients", clients);
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/clients", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("clients", Client.all());
      model.put("template", "templates/clients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int availabilityValue = 0;
      int servicesValue = 0;
      String[] availabilities = request.queryParamsValues("availability");
      for (String availability : availabilities) {
        availabilityValue += Integer.parseInt(availability);
      }
      String[] services = request.queryParamsValues("service");
      for (String service : services) {
        servicesValue += Integer.parseInt(service);
      }
      String clientName = request.queryParams("name");
      String clientPhone = request.queryParams("phone");
      Client newClient = new Client(clientName, availabilityValue, servicesValue, clientPhone);
      newClient.save();
      model.put("clients", Client.all());
      model.put("template", "templates/clients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/clients/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Client newClient = Client.find(Integer.parseInt(request.params(":id")));

      model.put("client", newClient);
      model.put("matchedstylists", newClient.getStylistMatches());
      model.put("template", "templates/client.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clear", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Client.deleteAll();
      Stylist.deleteAll();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
