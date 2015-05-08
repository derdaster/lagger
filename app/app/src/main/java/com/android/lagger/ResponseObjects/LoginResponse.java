package com.android.lagger.responseObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class LoginResponse extends ResponseObject {
    private Integer idUser;
    //FIXME change to enum?
    private Integer status;

    private LoginResponse(Integer idUser, Integer status){
        this.idUser = idUser;
        this.status = status;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "{" +
                "idUser=" + idUser +
                ", status=" + status +
                '}';
    }
}
