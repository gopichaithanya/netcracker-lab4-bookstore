package com.netcracker.ejb.entity;


import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface PublisherRemote extends EJBObject {

    public int getPublisherId() throws RemoteException;

    public String getPublisherName() throws RemoteException;

    public String getPublisherURL() throws RemoteException;
}
