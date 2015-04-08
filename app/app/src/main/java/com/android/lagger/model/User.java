package com.android.lagger.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Ewelina Klisowska on 2015-03-19.
 */
 /*
  In database table user:
    <Column Name="ID_User" Type="System.Int32" DbType="Int NOT NULL" IsPrimaryKey="true" CanBeNull="false" />
    <Column Name="Login" Type="System.String" DbType="NVarChar(100) NOT NULL" CanBeNull="false" />
    <Column Name="Password" Type="System.String" DbType="NVarChar(100) NOT NULL" CanBeNull="false" />
    <Column Name="Email" Type="System.String" DbType="NVarChar(100)" CanBeNull="true" />
    <Column Name="Activated" Type="System.Boolean" DbType="Bit NOT NULL" CanBeNull="false" />
    <Column Name="CreationDate" Type="System.DateTime" DbType="DateTime NOT NULL" CanBeNull="false" />
    <Column Name="LastEditDate" Type="System.DateTime" DbType="DateTime NOT NULL" CanBeNull="false" />
    <Column Name="Blocked" Type="System.Boolean" DbType="Bit NOT NULL" CanBeNull="false" />
*/
public class User {

    private Integer id;
    private String login;
    private String password;
    private String email;
    private String phone;

    public User(){
    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public JsonObject createLoginJson(){
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("login", this.login);
         jsonObject.addProperty("password", this.password);
        return jsonObject;
    }


}
