//package com.netcracker.ejb.entity;
//
//
//import com.netcracker.exceptions.DataAccessException;
//import com.netcracker.helper.ThinEntityWrapper;
//
//import javax.ejb.*;
//import java.util.Collection;
//import java.util.List;
//
//public class BookBean implements EntityBean {
//    @Override
//    public void setEntityContext(EntityContext entityContext) throws EJBException {
//
//    }
//
//    @Override
//    public void unsetEntityContext() throws EJBException {
//
//    }
//
//    @Override
//    public void ejbRemove() throws RemoveException, EJBException {
//
//    }
//
//    @Override
//    public void ejbActivate() throws EJBException{
//
//    }
//
//    @Override
//    public void ejbPassivate() throws EJBException{
//
//    }
//
//    @Override
//    public void ejbLoad() throws EJBException{
//
//    }
//
//    @Override
//    public void ejbStore() throws EJBException{
//
//    }
//
//    @Override
//    public int getBookId() {
//        return 0;
//    }
//
//    @Override
//    public String getTitle() {
//        return null;
//    }
//
//    @Override
//    public String getAuthor() {
//        return null;
//    }
//
//    @Override
//    public String getGenre() {
//        return null;
//    }
//
//    @Override
//    public String getPublisher() {
//        return null;
//    }
//
//    @Override
//    public List ejbHomeGetBooksAmount() throws DataAccessException {
//        return null;
//    }
//
//    // Home business methods
//    @Override
//    public Collection<ThinEntityWrapper> ejbHomeGetBooksInfo() throws DataAccessException {
//        return null;
//    }
//
//    @Override
//    public Integer ejbFindByPrimaryKey(Integer authorPk) throws FinderException, DataAccessException {
//        return null;
//    }
//
//    @Override
//    public Integer ejbCreate(Integer bookId, String title, String lastName) throws CreateException {
//        return null;
//    }
//
//    @Override
//    public void ejbPostCreate(Integer bookId, String title, String lastName) throws CreateException {
//
//    }
//}
