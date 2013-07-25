package com.netcracker.ejb.entity;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Contains business methods for Author.
 *
 */
public interface AuthorRemote extends EJBObject {

    public String getAuthorFirstName() throws RemoteException;

    public String getAuthorLastName() throws RemoteException;

    public int getAuthorId() throws RemoteException;

   // public void remove() throws RemoteException;


}
