package com.netcracker.ejb.entity;


import java.io.Serializable;

public class PublisherPk implements Serializable{

    public int publishId;

    public PublisherPk(int publishId) {
        this.publishId = publishId;
    }

    @Override
    public String toString() {
        return String.valueOf(publishId);
    }

    @Override
    public int hashCode() {
        return 31*publishId;
    }
}
