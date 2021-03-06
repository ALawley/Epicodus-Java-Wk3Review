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
      model.put("id", id);
      model.put("stylist", newStylist);
      model.put("clients", clients);
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Stylist newStylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("id", id);
      model.put("stylist", newStylist);
      model.put("template", "templates/stylist-update.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
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
      Stylist newStylist = Stylist.find(Integer.parseInt(request.params(":id")));
      newStylist.updateName(stylistName);
      newStylist.updateAvailability(availabilityValue);
      newStylist.updateServices(servicesValue);
      List<Client> clients = newStylist.getClients();
      model.put("id", id);
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
      if (newClient.getStylistId() > 0) {
        Stylist bookedStylist = Stylist.find(newClient.getStylistId());
        model.put("bookedStylist", bookedStylist);
      }
      model.put("id", id);
      model.put("client", newClient);
      model.put("matchedstylists", newClient.getStylistMatches());
      model.put("template", "templates/client.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients/:id/book/:id2", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int clientId = Integer.parseInt(request.params(":id"));
      int stylistId = Integer.parseInt(request.params(":id2"));
      Client newClient = Client.find(Integer.parseInt(request.params(":id")));
      newClient.addStylist(stylistId);
      if (newClient.getStylistId() > 0) {
        Stylist bookedStylist = Stylist.find(newClient.getStylistId());
        model.put("bookedStylist", bookedStylist);
      }
      model.put("id", clientId);
      model.put("client", newClient);
      model.put("matchedstylists", newClient.getStylistMatches());
      model.put("template", "templates/client.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/clients/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Client newClient = Client.find(Integer.parseInt(request.params(":id")));
      model.put("id", id);
      model.put("client", newClient);
      model.put("template", "templates/client-update.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients/:id/update", (request, response) -> {
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
      Client newClient = Client.find(Integer.parseInt(request.params(":id")));
      newClient.updateName(clientName);
      newClient.updateAvailability(availabilityValue);
      newClient.updateServices(servicesValue);
      newClient.updatePhone(clientPhone);
      if (newClient.getStylistId() > 0) {
        Stylist bookedStylist = Stylist.find(newClient.getStylistId());
        model.put("bookedStylist", bookedStylist);
      }
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
