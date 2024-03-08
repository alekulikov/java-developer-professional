package ru.otus.servlet;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

@SuppressWarnings({"squid:S1948"})
public class ClientsApiServlet extends HttpServlet {
    private final DBServiceClient serviceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient serviceClient, Gson gson) {
        this.serviceClient = serviceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (var body = req.getReader()) {
            serviceClient.saveClient(gson.fromJson(body, Client.class));
            resp.setStatus(SC_OK);
        }
    }
}
