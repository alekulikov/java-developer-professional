package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.helpers.DbConfigurationHelper;
import ru.otus.server.ClientWebServer;
import ru.otus.server.ClientWebServerWithSecurity;
import ru.otus.services.AuthService;
import ru.otus.services.AuthServiceImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

/*
    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/clients

    // REST сервис для POST запросов
    http://localhost:8080/api/client
*/
public class WebServerApplication {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var configuration = DbConfigurationHelper.getConfiguration();
        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        AuthService authService = new AuthServiceImpl();
        ClientWebServer clientWebServer =
                new ClientWebServerWithSecurity(WEB_SERVER_PORT, authService, dbServiceClient, gson, templateProcessor);

        clientWebServer.start();
        clientWebServer.join();
    }
}
