

package com.netcracker.ejb.entity;


import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;
import com.netcracker.utils.DBUtils;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BookBean implements EntityBean {

    private EntityContext context;
    /**
     * Book's ID
     */
    private Integer bookId;
    /**
     * Book's title
     */
    private String title;
    /**
     * Book's publisher
     */
    private int publishId;
    /**
     * Book's genre
     */
    private int genreId;
    /**
     * Book's authors IDs
     */
    private Collection<Integer> authorIds;
    /**
     * Book's short description
     */
    private String description;

    /**
     * Book's image reference
     */
    private String imgRef;
    /**
     * Book's publish year
     */
    private int year;

    //---------------------------------- Home business methods -------------------------------------------
    public Collection<ThinEntityWrapper> ejbHomeGetBooksInfo() throws DataAccessException {
        List<ThinEntityWrapper> result = new ArrayList<ThinEntityWrapper>();
        String sql = "SELECT booksId, title, imgRef from books";
        try {
            Map<String, List> genresDescription = DBUtils.executeSelect(getConnection(), sql, new Object[]{}, new int[]{});
            // Retrieve columns values
            //Iterator<List> mapValuesIter = genresDescription.values().iterator();
            // First column
            List ids = genresDescription.get("1");//mapValuesIter.next();
            Iterator idsIter = ids.iterator();
            // Second column
            List titles = genresDescription.get("2");//mapValuesIter.next();
            Iterator titlesIter = titles.iterator();
            //Third column
            List imgRefs = genresDescription.get("3");
            Iterator imgsIter = imgRefs.iterator();

            // Filling the result
            while (idsIter.hasNext() && titlesIter.hasNext()) {
                String imgRef = imgsIter.hasNext() ? (String)(imgsIter.next()) : "";
                result.add(new ThinEntityWrapper(idsIter.next().toString(), titlesIter.next().toString(), imgRef));
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error occurs during the work with database", e);
        } catch (NamingException e) {
            throw new DataAccessException("Can't lookup datasource object", e);
        }
    }
    //----------------------------------------------------------------------------------------------------


    //------------------------------------------ Remote Business methods -------------------------------------------



    public void remove() throws RemoveException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public Collection<Integer> getAuthorIds() {
        return authorIds;
    }

    public Collection<Author> getAuthors() throws DataAccessException, RemoteException {
        final String authorRef = "java:comp/env/ejbs/AuthorBean";
        Integer authorId = -1;
        List<Author> authors = new ArrayList<Author>();
        try {
            for (Iterator<Integer> authorsIter = authorIds.iterator(); authorsIter.hasNext();) {
                authorId = authorsIter.next();
                authors.add(lookupBeanHomeObject(authorRef, AuthorHome.class).findByPrimaryKey(authorId));
            }
            return authors;
        } catch (FinderException e) {
            throw new EJBException("Can't find author with authorId = " + authorId);
        }
    }


    public int getGenreId() {
        return genreId;
    }

    public Genre getGenre() throws DataAccessException, RemoteException {
        final String genreRef = "java:comp/env/ejbs/GenreBean";
        try {
            return lookupBeanHomeObject(genreRef, GenreHome.class).findByPrimaryKey(genreId);
        } catch (FinderException e) {
            throw new EJBException("Can't find genre with genreId = " + genreId);
        }
    }


    public int getPublisherId() {
        return publishId;
    }

    public Publisher getPublisher() throws DataAccessException, RemoteException {
        final String publishRef = "java:comp/env/ejbs/PublisherBean";
        try {
            return lookupBeanHomeObject(publishRef, PublisherHome.class).findByPrimaryKey(publishId);
        } catch (FinderException e) {
            throw new EJBException("Can't find publisher with publishId = " + publishId);
        }
    }


    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public String getImageReference() {
        return imgRef;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublishId(int publishId) {
        this.publishId = publishId;
    }

    public void setPublisher(Publisher publisher) throws RemoteException {
        this.publishId = publisher.getPublisherId();
    }


    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public void setGenre(Genre genre) throws RemoteException {
        this.genreId = genre.getGenreId();
    }


    public void setAuthorIds(Collection<Integer> authorIds) {
        this.authorIds.clear();
        this.authorIds.addAll(authorIds);
    }

    public void setAuthors(Collection<Author> authors) throws RemoteException {
        authorIds.clear();
        for (Author author : authors) {
            authorIds.add(author.getAuthorId());
        }
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgRef(String imgRef) {
        this.imgRef = imgRef;
    }

    public void setYear(int year) {
        this.year = year;
    }

    //--------------------------------------------------------------------------------------------------------------



    //------------------------------------------ Finder methods ----------------------------------------------------

    public Integer ejbFindByPrimaryKey(Integer bookPk) throws FinderException, DataAccessException {
        final String sqlQuery = "SELECT booksId FROM books WHERE booksId = ?";
        try {
            Integer bookId = (Integer)(DBUtils.executeSelectSingle(getConnection(), sqlQuery, new Object[]{bookPk}));
            if (bookId == null) {
                throw new ObjectNotFoundException("Book entity with given bookId = " + bookPk + " hasn't been find");
            }
            return bookId;
        } catch (SQLException e) {
            throw new EJBException("Book EJBs with given bookId = " + bookPk + " can 't be find due to the errors " +
                                   " during the work with a database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    // Not used
    public Integer ejbFindByAuthorAndGenreIDs(int authorId, int genreId) throws DataAccessException, FinderException {
       /* System.out.println("---------[Book] ejbFindByAuthorAndGenreIDs()");
        final StringBuilder sqlQuery = new StringBuilder()
                                            .append("SELECT bks.booksId")
                                            .append("   FROM books bks")
                                            .append("   JOIN balink bl")
                                            .append("       ON (bks.booksId = bl.bookId and bks.genreId = 2355)")
                                            .append("   JOIN authors ath")
                                            .append("       ON (bl.authorId = ath.authorId and ath.authorId = 1310)");
        try {
            Integer bookPk = (Integer)(DBUtils.executeSelectSingle(getConnection(), sqlQuery.toString(), new Object[]{}));
            if (bookPk == null) {
                *//*throw new FinderException("Book with specified search criteria authorId = " + authorId +
                                          " and genreId = " + genreId + " doesn't exists");*//*
            }
            return bookPk;
        } catch (SQLException e) {
            throw new EJBException("Book EJB can't be find due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }*/
        return null;
    }
    // Not used
    public Collection ejbFindByAuthorID(int authorId) throws DataAccessException, FinderException {
        System.out.println("---------[Book] ejbFindByAuthorID");
        if (authorId < 0) {
            throw new FinderException("authorId parameter must be a positive number");
        }
        final StringBuilder sqlQuery = new StringBuilder()
                .append("SELECT bks.booksId")
                .append("   FROM books bks")
                .append("   JOIN balink bl")
                .append("       ON (bks.booksId = bl.bookId)")
                .append("   JOIN authors ath")
                .append("       ON (bl.authorId = ath.authorId and ath.authorId = ?)");
        try {
               Map<String, List> columns = DBUtils.executeSelect(getConnection(), sqlQuery.toString(),
                       new Object[]{authorId}, new int[]{1});

            return columns.get("1");

        } catch (SQLException e) {
            throw new EJBException("Book EJBs which written by the author = " + authorId +
                                   " can't be find due to the errors during the work with a database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }
    // Not used
    public Collection ejbFindByGenreID(int genreId) throws DataAccessException, FinderException {
        return Collections.emptyList();
    }

    //--------------------------------------------------------------------------------------------------------------


    //----------------------------------------- Create and Remove methods ------------------------------------------

    public Integer ejbCreate(String title, int publishId, int genreId, String description, String imgRef, int year)
            throws CreateException {

        this.title = title;
        this.publishId = publishId;
        this.genreId = genreId;
        this.description = description;
        this.imgRef = imgRef;
        this.year = year;

        final String sqlQuery = "INSERT INTO books (title, publishId, genreId, descript, imgRef, year) VALUES (?,?,?,?,?,?)";


        try {
            int newBookId = DBUtils.executeInsert(getConnection(), sqlQuery,
                                                  new Object[] {title, publishId, genreId, description, imgRef, year}, true);
            this.bookId = newBookId;

            // If creation procedure has been performed successfully
            if (newBookId != 0) {
                return new Integer(newBookId);
            } else {
                throw new CreateException("Unsuccessful book creation");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't create BookBean bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }


    public void ejbPostCreate(String title, int publishId, int genreId, String description, String imgRef, int year)
            throws CreateException {}


    public Integer ejbCreate(String title, int publishId, int genreId, String description, String imgRef, int year, Collection<Integer> authorsIds) throws CreateException {
        this.title = title;
        this.publishId = publishId;
        this.genreId = genreId;
        this.description = description;
        this.imgRef = imgRef;
        this.year = year;
        this.authorIds = authorsIds;

        final String sqlQuery1 = "INSERT INTO books (title, publishId, genreId, descript, imgRef, year) VALUES (?,?,?,?,?,?)";
        final String sqlQuery2 = "INSERT INTO balink (authorId, bookId) VALUES (?,?)";

        try {
            int newBookId = DBUtils.executeInsert(getConnection(), sqlQuery1,
                    new Object[] {title, publishId, genreId, description, imgRef, year}, true);
            this.bookId = newBookId;

            for (Integer author: authorsIds) {
                DBUtils.executeInsert(getConnection(), sqlQuery2, new Object[]{author, bookId}, false);
            }

            // If creation procedure has been performed successfully
            if (newBookId != 0) {
                return new Integer(newBookId);
            } else {
                throw new CreateException("Unsuccessful book creation");
            }
        } catch (SQLException e) {
            throw new EJBException("Can't create BookBean bean due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    public void ejbPostCreate(String title, int publishId, int genreId, String description, String imgRef, int year, Collection<Integer> authorId) throws CreateException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void ejbRemove() throws RemoveException {
        Integer bookId = (Integer) context.getPrimaryKey();
        final String deleteFromBalink = "DELETE FROM balink WHERE bookId = ?";
        final String deleteFromBooks = "DELETE FROM books WHERE booksId = ?";
        try {
            // This section must be applied in one transaction
            int affectedRows1 = DBUtils.executeUpdate(getConnection(), deleteFromBalink, new Object[]{bookId});
            int affectedRows2 = DBUtils.executeUpdate(getConnection(), deleteFromBooks, new Object[]{bookId});
            if (affectedRows2 == 0) {
                throw new RemoveException("[Error] book with bookId = " + bookId + " hasn't been removed");
            }
        } catch (SQLException e) {
            throw new EJBException("Book with bookId = " + bookId + " can't be removed due to the errors " +
                    " during the work with a database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }



    //--------------------------------------------------------------------------------------------------------------

    //----------------------------------------- Bean's life cycle methods -------------------------------------------

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        System.out.println("---------[Book] setEntityContext()");
        context = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        System.out.println("---------[Book] unsetEntityContext()");
        context = null;
    }

    public void ejbActivate() throws EJBException {
        System.out.println("---------[Book] ejbActivate()");

    }

    public void ejbPassivate() throws EJBException {
        System.out.println("---------[Book] ejbPassivate()");

    }

    public void ejbLoad() throws EJBException {
        System.out.println("---------[Book] ejbLoad()");
        Integer bookPk = (Integer)context.getPrimaryKey();
        final String sqlQuery = "SELECT * FROM books WHERE booksId = ?";
        try {
            Map<String, List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{bookPk.intValue()},
                    new int[]{});

            bookId = (Integer) (queryRes.get("1").get(0));
            title = (String) (queryRes.get("2").get(0));
            publishId = (Integer) (queryRes.get("3").get(0));
            genreId = (Integer) (queryRes.get("4").get(0));
            description = (String) (queryRes.get("5").get(0));
            imgRef = (String) (queryRes.get("6").get(0));
            year = (Integer) (queryRes.get("7").get(0));
            authorIds = getBookAuthorsIds(bookPk);

        } catch (SQLException e) {
            throw new EJBException("Book EJB can't be load due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }



    /**
     * Returns list of authors IDs which wrote the book with given "bookId" ID.
     *
     * @param bookId    analyzed book
     * @return          list of authors IDs that wrote given book
     *
     * @throws java.sql.SQLException     if any errors during the work with database occurs.
     * @throws javax.naming.NamingException  when can't lookup datasource object
     */
    private Collection<Integer> getBookAuthorsIds(Integer bookId) throws SQLException, NamingException {
        final String sqlQuery = "SELECT authorId FROM balink WHERE bookId = ?";
        Map<String, List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{bookId}, new int[]{});
        return (List<Integer>)(queryRes.get("1"));
    }

    public void ejbStore() throws EJBException {
        System.out.println("---------[Book] ejbStore()");
        final String updateBook = "UPDATE books SET title = ?, publishId = ?, genreId = ?, " +
                                  "descript = ?, imgref = ?, year = ? WHERE booksId = ?";
        final String updateBookAuthorLinks = "UPDATE balink SET authorId = ? WHERE bookId = ?";
        try {
            // update information about book
            Object[] params1 = {title, publishId, genreId, description, imgRef, year, bookId};
            DBUtils.executeUpdate(getConnection(), updateBook, params1);

            // update information about Book <--> Author relationship
            for (Integer authorId : authorIds) {
                Object[] params2 = {authorId, bookId};
                DBUtils.executeUpdate(getConnection(), updateBookAuthorLinks, params2);
            }
        } catch (SQLException e) {
            throw new EJBException("Can't store book's info due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }

    //--------------------------------------------- Helper methods ---------------------------------------------------

    /**
     * Returns connection to the database.
     *
     * @throws javax.naming.NamingException  in case when can't lookup datasource object
     * @throws java.sql.SQLException     when any errors occurs during the work with database
     */
    private Connection getConnection() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/datasources/booksStoreDB");
        return ds.getConnection();
    }

    private <T> T lookupBeanHomeObject(String jndiRef, Class<T> homeClass) {
        try {
            Context ctx = new InitialContext();
            return (T)(PortableRemoteObject.narrow(ctx.lookup(jndiRef), homeClass));
        } catch (NamingException e) {
            throw new EJBException("Can't lookup bean with class " + homeClass.getName());
        }
    }
}
