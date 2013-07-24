package com.netcracker.ejb.entity;

import java.io.Serializable;

public class GenrePk implements Serializable {
    public int genreId;

    public GenrePk() {
    }

    public GenrePk(int genreId) {
        this.genreId = genreId;
    }

    @Override
    public String toString() {
        return String.valueOf(genreId);
    }

    @Override
    public int hashCode() {
        return 31*genreId;
    }
}
