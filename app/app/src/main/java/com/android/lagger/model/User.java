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

    private Integer ID_User;
    private String Login;
    private String Password;
    private String email;
    private Boolean activated;
    private Boolean blocked;

    public User(){

    }

    public User(String login, String password){
        this.Login = login;
        this.Password = password;
    }
    public User(Integer ID_User, String login, String password, String email, Boolean activated, Boolean blocked) {
        this(login, password);
        this.ID_User = ID_User;
//        this.Login = login;
//        this.Password = password;
        this.email = email;
        this.activated = activated;
        this.blocked = blocked;
    }

    public Integer getID_User() {
        return ID_User;
    }

    public void setID_User(Integer ID_User) {
        this.ID_User = ID_User;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        this.Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public JsonObject createLoginJson(){
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("Login", this.Login);
         jsonObject.addProperty("Password", this.Password);
        return jsonObject;
    }
}
