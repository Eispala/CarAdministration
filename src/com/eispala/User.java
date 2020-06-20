package com.eispala;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class User
{
    public User(int id, String firstName, String lastName, Date birthDate, String loginEmailAddress, String communicationEmailAddress,
                String passwordHash)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName =lastName;
        this.birthDate = birthDate;
        this.loginEmailAddress = loginEmailAddress;
        this.communicationEmailAddress = communicationEmailAddress;
        this.passwordHash = passwordHash;
    }

    public User(ResultSet result) throws SQLException
    {
        id = result.getInt("id");
        firstName = result.getString("firstName");
        lastName =result.getString("lastName");
        birthDate = result.getDate("birthDate");
        loginEmailAddress = result.getString("loginEmailAddress");
        communicationEmailAddress = result.getString("communicationEmailAddress");
        passwordHash = result.getString("passwordHash");

    }

    public int id;
    public String firstName;
    public String lastName;
    public Date birthDate;
    public String loginEmailAddress;
    public String communicationEmailAddress;
    public String passwordHash;

}
