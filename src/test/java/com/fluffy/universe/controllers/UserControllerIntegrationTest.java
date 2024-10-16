package com.fluffy.universe.controllers;

import com.fluffy.universe.models.User;
import com.fluffy.universe.services.UserService;
import com.fluffy.universe.utils.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.sql2o.Connection;

import java.io.File;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserControllerIntegrationTest {
    private static Connection connection;
    private static UserController userController;
    private static Javalin app;
    private String testUserEmail;

    @BeforeEach
    public void setup() {
        Configuration.load(new File("application.properties"));

        // Create SQLite connection
        try {
            connection = DataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Create the User table if it does not exist
        connection.createQuery("CREATE TABLE IF NOT EXISTS User (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "RoleID INTEGER, " +
                        "FirstName TEXT, " +
                        "LastName TEXT, " +
                        "Email TEXT UNIQUE, " +
                        "Password TEXT, " +
                        "Gender TEXT, " +
                        "Birthday TEXT, " +
                        "Address TEXT, " +
                        "Website TEXT, " +
                        "ResetPasswordToken TEXT)")
                .executeUpdate();

        // Initialize a minimal Javalin application for testing
        app = Javalin.create().start(7001); // or any port, won't actually be used

        // Create UserController instance with the app
        userController = new UserController(app);
    }

    @AfterEach
    public void tearDown() {
        // Delete the test user from the database if it was created
        if (testUserEmail!=null) {
            deleteUserFromDatabase(testUserEmail);
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testUserRegistrationAndDatabase() {
        // Create a mock context
        Context context = Mockito.mock(Context.class);

        // Prepare the mock context behavior
        when(context.formParam("first-name")).thenReturn("Jane");
        when(context.formParam("last-name")).thenReturn("Doe");
        when(context.formParam("email")).thenReturn("jane.doe@example.com");
        when(context.formParam("password")).thenReturn("Password123!");
        when(context.formParam("confirm-password")).thenReturn("Password123!");

        testUserEmail=context.formParam("email");

        // Set up a new ServerData instance in the context session
        ServerData serverData = new ServerData();
        when(context.sessionAttribute(SessionKey.SERVER_DATA)).thenReturn(serverData);

        // Sign up the user
        userController.signUp(context);

        // Retrieve user from the database
        User registeredUser = UserService.getUserByEmail("jane.doe@example.com");

        // Verify the user was added
        assertNotNull(registeredUser);
        assertEquals("Jane", registeredUser.getFirstName());
        assertEquals("Doe", registeredUser.getLastName());
        assertEquals("jane.doe@example.com", registeredUser.getEmail());

        // Verify that the redirect occurred
        verify(context).redirect("/");
    }

    public static void deleteUserFromDatabase(String testUserEmail) {
        // Delete the test user directly from the database
        String deleteSQL = "DELETE FROM User WHERE EMAIL = :email";
        try (Connection conn = DataSource.getConnection()) {
           conn.createQuery(deleteSQL)
                    .addParameter("email", testUserEmail)
                    .executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
