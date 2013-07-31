package com.netcracker.ejb.entity;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Contains business methods for Author.
 *
 */
public interface Author extends EJBObject {

    public String getFirstName() throws RemoteException;

    public String getLastName() throws RemoteException;

    public int getAuthorId() throws RemoteException;


}
