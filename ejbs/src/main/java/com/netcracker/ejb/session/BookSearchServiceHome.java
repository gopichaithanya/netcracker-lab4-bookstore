package com.netcracker.ejb.session;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface BookSearchServiceHome extends EJBHome {
    public BookSearchService create() throws CreateException, RemoteException;
}
