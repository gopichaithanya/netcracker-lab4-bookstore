package com.netcracker.ejb.entity;


import java.io.Serializable;

public class AuthorPk implements Serializable {

    public int authorId;

    public AuthorPk(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return String.valueOf(authorId);
    }

    @Override
    public int hashCode() {
        return 31*authorId;
    }
}
