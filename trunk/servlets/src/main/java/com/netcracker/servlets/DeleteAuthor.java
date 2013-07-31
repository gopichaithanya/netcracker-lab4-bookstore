package com.netcracker.servlets;

import com.netcracker.ejb.entity.AuthorHome;
import com.netcracker.ejb.entity.Author;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DeleteAuthor extends HttpServlet {

    private static final Logger log = Logger.getLogger(AddAuthor.class);

    private String id;


    private AuthorHome authorHome;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            id = req.getParameter("Id");
            Object obj = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            authorHome = (AuthorHome) PortableRemoteObject.narrow(obj, AuthorHome.class);

                Author author = authorHome.findByPrimaryKey(Integer.parseInt(id));
                author.remove();

                log.info("Author with id  " + id + " successfully removed.");
                req.getServletContext().getRequestDispatcher("/books").forward(req, resp);

        } catch (NamingException e) {
            log.error("Can't lookup bean", e);
            throw new ServletException(e);
        }
        catch (FinderException e) {
            throw new ServletException("Can't find EJB with specified primary key",e);
        } catch (RemoveException e) {
            throw new ServletException("Errors occurs during book removing. The Author bean or the container does not allow the EJB object to be removed", e);
        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Errors occurs during the work with database", e);
        }

    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            Object obj = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            authorHome = (AuthorHome) PortableRemoteObject.narrow(obj, AuthorHome.class);
            List<Author> allAuthors = new ArrayList<Author>();

            req.setAttribute("authorsList", authorHome.getAuthorsInfo());
        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Errors occurs during the work with database", e);
        } catch (NamingException e) {
            log.error("Can't lookup bean", e);
            throw new ServletException(e);
        }
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/deleteAuthor.jsp").forward(req, resp);
    }
}
