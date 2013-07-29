package com.netcracker.ejb.entity;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface Genre extends EJBObject {
    public int getGenreId() throws RemoteException;
    public String getGenreName() throws RemoteException;
}
