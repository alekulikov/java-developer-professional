package ru.otus.services;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Properties;

public class AuthServiceImpl implements AuthService {
    private static final String ADMIN_CREDENTIALS = "/credentials.properties";

    @Override
    public boolean authenticate(String login, String password) {
        try (var is = AuthServiceImpl.class.getResourceAsStream(ADMIN_CREDENTIALS)) {
            var credentials = new Properties();
            credentials.load(is);
            return password.equals(credentials.getProperty(login));
        } catch (IOException e) {
            throw new InvalidPathException(ADMIN_CREDENTIALS, "Invalid config file");
        }
    }
}
