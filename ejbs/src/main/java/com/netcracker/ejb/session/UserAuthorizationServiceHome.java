package com.netcracker.ejb.session;


import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface UserAuthorizationServiceHome {

    public UserAuthorizationService create() throws CreateException, RemoteException;
}
