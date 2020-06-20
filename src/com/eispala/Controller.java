package com.eispala;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import at.favre.lib.bytes.Bytes;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class Controller
{
    Connection _connection;

    public Controller() throws SQLException
    {
        _connection = DriverManager.getConnection("jdbc:postgresql://207.154.234.136:5432/1920-Automarkt",
                "1920-Automarkt", "caccfc046d179b6f792f841568dbb013");

    }

    public User InsertUser(String firstName, String lastName, Date birthDate, String loginEmailAddress,
                           String communicationEmailAddress, String password) throws SQLException
    {
        if (UserExists(loginEmailAddress))
        {
            return null;
        }

        String birthDateIso = GetBirthDateIso(birthDate);
        String passwordHash = GetPasswordHash(password);
        String query = GetInsertQuery(firstName, lastName, birthDateIso, loginEmailAddress, communicationEmailAddress, passwordHash);

        Logger.DebugLog("Query: " + query);

        Statement insertUser = _connection.createStatement();
        ResultSet insertResult = insertUser.executeQuery(query);
        insertResult.next();
        return new User(insertResult.getInt(1), firstName, lastName, birthDate, loginEmailAddress, communicationEmailAddress, passwordHash);
    }

    public User LoginUser(String loginEmailAddress, String password) throws SQLException
    {
        if (!UserExists(loginEmailAddress))
        {
            Logger.DebugLog("Login: Mail not found");
            return null;
        }

        String query = GetAuthenticationQuery(loginEmailAddress);

        Logger.DebugLog(String.format("Login Query: %s", query));

        Statement userPasswordHash = _connection.createStatement();
        ResultSet userData = userPasswordHash.executeQuery(query);
        userData.next();

        Logger.DebugLog(String.format("Found password Hash: %s", userData.getString("passwordhash")));

        if (VerifyPassword(password, userData.getString("passwordhash")))
        {
            return new User(userData);
        } else
        {
            return null;
        }
    }

    private String GetAuthenticationQuery(String loginEmailAddress)
    {
        return String.format("select * from users where loginEmailAddress = \'%s\';", loginEmailAddress);
    }

    private String GetInsertQuery(String firstName, String lastName, String birthDateIso, String loginEmailAddress,
                                  String communicationEmailAddress, String passwordHash)
    {
        return String.format("insert into users (firstName, lastName, birthDate, loginEmailAddress, " +
                        "communicationEmailAddress, passwordHash) values (\'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\') returning " +
                        "id;",
                firstName, lastName, birthDateIso, loginEmailAddress, communicationEmailAddress, passwordHash);
    }

    private String GetBirthDateIso(Date birthDate)
    {
        TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");
        DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        isoDateFormat.setTimeZone(timeZoneUTC);

        return isoDateFormat.format(birthDate);

    }

    public boolean UserExists(User user) throws SQLException
    {
        return UserExists(user.loginEmailAddress);

    }

    public boolean UserExists(String loginEmailAddress) throws SQLException
    {
        Statement userExists = _connection.createStatement();
        userExists.execute(String.format("select id from users where loginEmailAddress = \'%s\';", loginEmailAddress));

        return userExists.getResultSet().next();
    }

    private String GetPasswordHash(String password)
    {
        return new String(BCrypt.withDefaults().hash(10, Bytes.random(16).array(), password.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
    }

    private boolean VerifyPassword(String inputPassword, String realPasswordHash)
    {
        return BCrypt.verifyer().verify(inputPassword.getBytes(StandardCharsets.UTF_8),
                realPasswordHash.getBytes(StandardCharsets.UTF_8)).verified;
    }

}


