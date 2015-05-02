package com.android.lagger.model.navigation;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class UserPosition {
    private Integer arrivalTime;
    private Integer idUser;
    private List<Position> positions;

    public UserPosition(Integer arrivalTime, Integer idUser, List<Position> positions) {
        this.arrivalTime = arrivalTime;
        this.idUser = idUser;
        this.positions = positions;
    }
}
