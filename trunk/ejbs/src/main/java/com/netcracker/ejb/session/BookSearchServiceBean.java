package com.netcracker.ejb.session;

import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;
import com.netcracker.utils.DBUtils;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BookSearchServiceBean implements SessionBean {

    //------------------------------------------- Business methods ----------------------------------------

    /**
     * Returns general information about the books (see the extractBooksInfo() method)
     * that satisfy the passed parameters (authorId and genreId). When authorId < 0
     * and genreId < 0 (for example authorId = genreId = -1) method return info
     * about all available books stored in the database.
     *
     * @param authorId  ID of the author that written the book
     * @param genreId   book's genre ID
     *
     * @return  books general info that satisfy the given criteria
     * @throws DataAccessException  when any errors occurs during the work with database
     */
    public Collection<ThinEntityWrapper> getBooksShortInfo(int authorId, int genreId) throws DataAccessException {
        if (authorId < 0 && genreId < 0) {
            return getAllBooks();
        } else if (authorId < 0 && genreId >= 0) {
            return getBooksByGenreId(genreId);
        } else if (authorId >= 0 && genreId < 0) {
            return getBooksByAuthorId(authorId);
        } else if (authorId >= 0 && genreId >= 0) {
            return getBooksByAuthorAndGenreIds(authorId, genreId);
        } else {
            return Collections.emptyList();
        }
    }



    public Collection<ThinEntityWrapper> getBookInfoBySearchCriteria(String title, String author, int publisherId,
                                                                     int genreId, int year)
            throws DataAccessException {

        try {
            List<ThinEntityWrapper> books = new ArrayList<ThinEntityWrapper>();

            Set<Integer> byTitleAndYear = findBooksByTitleAndYear(title, year);
            Set<Integer> byGenreAndPublisher = findBooksByGenreAndPublisher(genreId, publisherId);
            Set<Integer> byAuthor = findBooksByAuthor(author);

            // Intersect all the fetched results to determine the common elements among the three sets
            byTitleAndYear.retainAll(byGenreAndPublisher);
            byTitleAndYear.retainAll(byAuthor);
            return getBooksInfo(byTitleAndYear);

        } catch (SQLException e) {
            throw new DataAccessException("Error occurs during the work with database", e);
        }
    }

    //-----------------------------------------------------------------------------------------------------

    //----------------------------------------- Life cycle methods ----------------------------------------
    public void ejbCreate() throws CreateException { }

    public void setSessionContext(SessionContext sessionContext) throws EJBException { }

    public void ejbRemove() throws EJBException { }

    public void ejbActivate() throws EJBException { }

    public void ejbPassivate() throws EJBException { }
    //-----------------------------------------------------------------------------------------------------



    //--------------------------------------------- Service methods ---------------------------------------

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


    /**
     * Return general information about all available books. To see what kind of
     * info is returned see extractBooksInfo() method description.
     *
     * @return      returns general information about all books
     *
     * @throws DataAccessException  when errors during the work with database occurs
     */
    public Collection<ThinEntityWrapper> getAllBooks() throws DataAccessException {
        final String sqlQuery = "SELECT booksId, title, imgRef FROM books";
        try {
            Map<String, List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{},
                    new int[]{});

            return extractBooksInfo(queryRes);
        } catch (SQLException e) {
            throw new DataAccessException("General information about all available books can't be load " +
                                          "due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    /**
     * Return general information about books that written in genre "genreId". To see
     * what kind of info is returned see extractBooksInfo() method description.
     *
     * @return  books info that satisfy the genreId condition
     *
     * @throws DataAccessException   when errors during the work with database occurs
     */
    private Collection<ThinEntityWrapper> getBooksByGenreId(int genreId) throws DataAccessException {
        final String sqlQuery = "SELECT booksId, title, imgRef FROM books WHERE genreId = ?";
        try {
            Map<String, List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery,
                    new Object[]{genreId}, new int[]{});

            return extractBooksInfo(queryRes);
        } catch (SQLException e) {
            throw new DataAccessException("General information about books that have genreId = " + genreId +
                                          " can't be fetched due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }

    }

    /**
     * Return general information about books that written by "authorId" author. To see
     * what kind of info is returned see extractBooksInfo() method description.
     *
     * @return  books info for books that are written by the "authorId" author
     *
     * @throws DataAccessException   when errors during the work with database occurs
     */

    private Collection<ThinEntityWrapper> getBooksByAuthorId(int authorId) throws DataAccessException {
        final StringBuilder sqlQuery = new StringBuilder()
                                            .append("SELECT bks.booksId, bks.title, bks.imgRef")
                                            .append("   FROM books bks")
                                            .append("   JOIN balink bl")
                                            .append("       ON (bks.booksId = bl.bookId)")
                                            .append("   JOIN authors ath")
                                            .append("       ON (bl.authorId = ath.authorId AND ath.authorId = ?)");
        try {
            Map<String, List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery.toString(),
                                                               new Object[]{authorId}, new int[]{});
            return extractBooksInfo(queryRes);
        } catch (SQLException e) {
            throw new DataAccessException("General information about books that are written by " +
                                          "authorId = " + authorId + " author can't be fetched due to " +
                                          "the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    /**
     * Return general information about books that written by "authorId" author in genre
     * determined by "genreId" parameter. To see what kind of info is returned see
     * extractBooksInfo() method description.
     *
     * @return  books info that satisfy passed conditions (authorId and genreId params)
     *
     * @throws DataAccessException   when errors during the work with database occurs
     */
    private Collection<ThinEntityWrapper> getBooksByAuthorAndGenreIds(int authorId, int genreId)
            throws DataAccessException {
        final StringBuilder sqlQuery = new StringBuilder()
                                        .append("SELECT bks.booksId, bks.title, bks.imgRef")
                                        .append("   FROM books bks")
                                        .append("   JOIN balink bl")
                                        .append("       ON (bks.booksId = bl.bookId AND bks.genreId = ?)")
                                        .append("   JOIN authors ath")
                                        .append("       ON (bl.authorId = ath.authorId AND ath.authorId = ?)");
        try {
            Map<String, List> queryRes = DBUtils.executeSelect(getConnection(), sqlQuery.toString(),
                    new Object[]{genreId, authorId}, new int[]{});
            return extractBooksInfo(queryRes);
        } catch (SQLException e) {
            throw new DataAccessException("General information about books that are written by " +
                                          "authorId = " + authorId + " author and have genreId = " + genreId +
                                          " can't be fetched due to the errors during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    /**
     * Extract information about book such as bookId, title and image reference from
     * the result set of the SQL query execution (represented by "queryRes" parameter).
     *
     * @param queryRes  result of SQL execution
     *
     * @return  brief information about books. Each book brief info encapsulated in
     *          ThinEntityWrapper instance
     */
    private List<ThinEntityWrapper> extractBooksInfo(Map<String, List> queryRes) {
        List<ThinEntityWrapper> result = new ArrayList<ThinEntityWrapper>();

        Iterator idsIter = queryRes.get("1").iterator();
        Iterator titlesIter = queryRes.get("2").iterator();
        Iterator imgsIter = queryRes.get("3").iterator();

        while (idsIter.hasNext()) {
            String id = idsIter.next().toString();
            String title = (String)(titlesIter.next());
            String imgRef = imgsIter.hasNext() ? (String)(imgsIter.next()) : "";
            result.add(new ThinEntityWrapper(id, title, imgRef));
        }
        return result;
    }

    //-----------------------------------------------------------------------------------------------------
    /**
     * Retrieve set of book's ids that satisfy the title and year criteria.
     *
     * @param title         book's title
     * @param year          book's year
     * @return              found book's ids
     *
     * @throws SQLException an exception that provides information on a database access error or other errors
     */
    private Set<Integer> findBooksByTitleAndYear(String title, int year) throws SQLException {
        try {
            String sqlQuery = null;
            List bookIds;
            // If year parameter is specified by the user find books by title and year
            if (year >= 0) {
                sqlQuery = "select booksId from books where (title like ?) and (books.year = ?)";
                bookIds = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{"%" + title + "%", year},
                                                new int[]{}).get("1");
            } else {
                sqlQuery = "select booksId from books where title like ?";
                bookIds = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{"%" + title + "%"},
                                                new int[]{}).get("1");
            }

            return new HashSet<Integer>(bookIds);
        } catch(NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }

    private Set<Integer> findBooksByGenreAndPublisher(int genreId, int publisherId) throws SQLException {
        try {
            String sqlQuery = null;
            List bookIds;

            if (genreId >= 0 && publisherId < 0) {
                sqlQuery = "select booksId from books where genreId = ?";
                bookIds = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{genreId}, new int[]{}).get("1");
            } else if (genreId < 0 && publisherId >=0) {
                sqlQuery = "select booksId from books where publishId = ?";
                bookIds = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{publisherId}, new int[]{}).get("1");
            } else if (genreId >= 0 && publisherId >= 0) {
                sqlQuery = "select booksId from books where genreId = ? and publishId = ?";
                bookIds = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{genreId, publisherId}, new int[]{}).get("1");
            } else {
                sqlQuery = "select booksId from books";
                bookIds = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{}, new int[]{}).get("1");
            }

            return new HashSet<Integer>(bookIds);
        } catch(NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }


    private Set<Integer> findBooksByAuthor(String author) throws SQLException{
        try {
            String sqlQuery = "SELECT ba.bookId FROM balink ba, authors aut " +
                              "WHERE ba.authorId = aut.authorId AND CONCAT(firstName, lastName) LIKE ?";
            List bookIds = DBUtils.executeSelect(getConnection(), sqlQuery, new Object[]{"%" + author + "%"}, new int[]{}).get("1");

            return new HashSet<Integer>(bookIds);
        } catch(NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }


    private Collection<ThinEntityWrapper> getBooksInfo(Collection<Integer> bookIds) throws DataAccessException {
        List<ThinEntityWrapper> result = new ArrayList<ThinEntityWrapper>();
        if (bookIds == null || bookIds.size() == 0) {
            return result;
        }

        StringBuilder sqlQuery = new StringBuilder("SELECT booksId, title, imgref FROM books WHERE booksId in (");

        try {
            for (Iterator<Integer> ids = bookIds.iterator(); ;ids.hasNext()) {
                sqlQuery.append(ids.next().intValue());
                if (ids.hasNext()) {
                    sqlQuery.append(", ");
                } else {
                    sqlQuery.append(")");
                    break;
                }
            }

            Map<String, List> booksInfo = DBUtils.executeSelect(getConnection(), sqlQuery.toString(), new Object[]{}, new int[]{});

            Iterator<Integer> idsIter = booksInfo.get("1").iterator();
            Iterator<String> titlesIter = booksInfo.get("2").iterator();
            Iterator<String> imgRefsIter = booksInfo.get("3").iterator();

            // Filling the result
            while (idsIter.hasNext() && titlesIter.hasNext()) {
                String imgRef = imgRefsIter.hasNext() ? imgRefsIter.next() : "";
                result.add(new ThinEntityWrapper(idsIter.next().toString(), titlesIter.next().toString(), imgRef));
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Error occurs during the work with database", e);
        } catch (NamingException e) {
            throw new EJBException("Can't lookup datasource object", e);
        }
    }
    //-----------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------

}
