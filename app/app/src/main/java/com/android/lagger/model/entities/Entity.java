package com.android.lagger.model.entities;

import com.android.lagger.helpers.interfaces.IEntity;

/**
 * Created by Kubaa on 2015-04-01.
 */
public abstract class Entity implements IEntity {

    private int id;

    protected Entity(int inID){
        id = inID;
    }

    public int getID(){
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return other != null && this.getID() == ((Entity) other).getID();
    }



}
