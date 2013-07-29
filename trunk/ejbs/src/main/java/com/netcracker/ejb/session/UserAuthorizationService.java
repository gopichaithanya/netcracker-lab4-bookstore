package com.netcracker.ejb.session;


import com.netcracker.exceptions.DataAccessException;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface UserAuthorizationService extends EJBObject{

    public boolean isUserAuthorized(String nick, String pass, String status) throws DataAccessException, RemoteException ;

}
