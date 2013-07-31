package com.netcracker.ejb.session;


import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface UserAuthorizationServiceHome extends EJBHome{

    public UserAuthorizationService create() throws CreateException, RemoteException;
}
