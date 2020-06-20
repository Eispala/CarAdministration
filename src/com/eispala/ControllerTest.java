package com.eispala;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest
{

    @Test
    void insertUser() throws SQLException
    {
        Controller controller = new Controller();
        assertNull(controller.InsertUser("first", "last", new Date(), "login@example.org",
                "communication@example.org", "password"));

    }

    @Test
    void loginUser() throws SQLException, ParseException
    {
        Controller controller = new Controller();
        assertNotNull(controller.LoginUser("login@example.org", "password"));
        assertNull(controller.LoginUser("login@example.org", ""));

        Date d = new Date();

        User testUser = new User(1, "first", "last", d,
                "login@example.org", "communication@example.org",
                "$2a$10$wy95urXKTMslLsnq65BmFuYEo.e9iTGnJGoO0m85/JqpbimYb8a5.");

        User realUser = controller.LoginUser("login@example.org", "password");
        realUser.birthDate = d;

        assertEquals(testUser.id, realUser.id);
        assertEquals(testUser.firstName, realUser.firstName);
        assertEquals(testUser.lastName, realUser.lastName);
        assertEquals(testUser.birthDate, realUser.birthDate);
        assertEquals(testUser.loginEmailAddress, realUser.loginEmailAddress);
        assertEquals(testUser.communicationEmailAddress, realUser.communicationEmailAddress);
        assertEquals(testUser.passwordHash, realUser.passwordHash);

    }

    @Test
    void userExists() throws SQLException
    {
        Controller controller = new Controller();

        assertTrue(controller.UserExists("login@example.org"));
        assertFalse(controller.UserExists(""));
        assertFalse(controller.UserExists("saskfj@example.org"));
    }
}