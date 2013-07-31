package com.netcracker.servlets;

import com.netcracker.ejb.entity.*;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ModifyBookHandler extends HttpServlet {

    private static final Logger log = Logger.getLogger(ModifyBookHandler.class);


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            String title = req.getParameter("title");
            int publishId = Integer.parseInt(req.getParameter("publisherId"));
            int genreId = Integer.parseInt(req.getParameter("genreId"));
            int authorId = Integer.parseInt(req.getParameter("authorId"));
            String description = req.getParameter("descript");
            String imgRef = req.getParameter("imgRef");
            int year = Integer.parseInt(req.getParameter("year"));
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            Object obj = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
            BookHome bookHome = (BookHome) PortableRemoteObject.narrow(obj, BookHome.class);


            if (isBookValid(title)) {

                Collection<Integer> authorsId = new ArrayList<Integer>();
                authorsId.add(authorId);

                Book book = bookHome.findByPrimaryKey(bookId);
                book.setAuthorIds(authorsId);
                book.setTitle(title);
                book.setPublishId(publishId);
                book.setGenreId(genreId);
                book.setImgRef(imgRef);
                book.setDescription(description);
                book.setYear(year);

                log.info("Book with id: " + bookId + " successfully updated.");
                req.getServletContext().getRequestDispatcher("/books").forward(req, resp);
            } else {
                req.setAttribute("error", true);
                doGet(req, resp);
                req.setAttribute("error", false);
                log.error("Error occurs during checking book parameters typing by user.");
            }

        } catch (NamingException e) {
            log.error("Can't lookup book bean", e);
            throw new ServletException(e);
        } catch (FinderException e) {
            throw new ServletException("Can't find EJB with specified primary key", e);
        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Errors occurs during the work with database", e);
        }
    }


    private boolean isBookValid(String title) {
        return !(title.isEmpty()
                || title.matches(".*\\d.*"));
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int bookId = Integer.parseInt(req.getParameter("bookId"));

        try {
            Context ctx = Helper.getInstance().getContext();
            Object authorObj = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            AuthorHome authorHome = (AuthorHome) PortableRemoteObject.narrow(authorObj, AuthorHome.class);
            req.setAttribute("authorsList", authorHome.getAuthorsInfo());

            Object genreObj = ctx.lookup("ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome");
            GenreHome genreHome = (GenreHome) PortableRemoteObject.narrow(genreObj, GenreHome.class);
            req.setAttribute("genresList", genreHome.getGenresInfo());

            Object publisherObj = ctx.lookup("ear-1.0/ejbPart/PublisherBean!com.netcracker.ejb.entity.PublisherHome");
            PublisherHome publisherHome = (PublisherHome) PortableRemoteObject.narrow(publisherObj, PublisherHome.class);
            req.setAttribute("publishersList", publisherHome.getPublishersInfo());

            Object bookObj = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
            BookHome bookHome = (BookHome) PortableRemoteObject.narrow(bookObj, BookHome.class);
            req.setAttribute("book", bookHome.findByPrimaryKey(bookId));

        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Error occurs during work with database", e);
        } catch (NamingException e) {
            throw new ServletException("Can't lookup bean", e);
        } catch (FinderException e) {
            throw  new ServletException("Can't find EJB with specified primary key");
        }
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/modifyBook.jsp").forward(req, resp);
    }
}
