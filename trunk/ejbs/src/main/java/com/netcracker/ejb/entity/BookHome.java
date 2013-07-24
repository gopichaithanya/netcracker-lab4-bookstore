//package com.netcracker.ejb.entity;
//
//
//import com.netcracker.exceptions.DataAccessException;
//import com.netcracker.helper.ThinEntityWrapper;
//
//import javax.ejb.CreateException;
//import javax.ejb.EJBHome;
//import javax.ejb.FinderException;
//import java.rmi.RemoteException;
//import java.util.Collection;
//import java.util.List;
//
//public interface BookHome extends EJBHome {
//
//    public BookRemote create(Integer bookId, String title, String lastName)
//            throws CreateException, RemoteException;
//
////    public AuthorRemote remove(Integer authorId)
////            throws FinderException, RemoteException;
//
//    public BookRemote findByPrimaryKey(Integer authorPk)
//            throws FinderException, RemoteException, DataAccessException;
//
//
//    // Home business methods
//    public Collection<ThinEntityWrapper> getBooksInfo()
//            throws RemoteException, DataAccessException;
//
//    public List getBooksAmount()
//            throws RemoteException, DataAccessException;
//}
