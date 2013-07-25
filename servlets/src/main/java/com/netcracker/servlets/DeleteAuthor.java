package com.netcracker.servlets;

import com.mysql.jdbc.StringUtils;
import com.netcracker.ejb.entity.AuthorHome;
import com.netcracker.ejb.entity.AuthorRemote;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.CreateException;
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
import java.rmi.RemoteException;
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

                AuthorRemote authorRemote = authorHome.findByPrimaryKey(Integer.parseInt(id));
                authorRemote.remove();

                log.info("Author with id  " + id + " successfully removed.");
                req.getServletContext().getRequestDispatcher("/books").forward(req, resp);

        } catch (NamingException e) {
            log.error("Error occurs during adding author to the database", e);
            throw new ServletException(e);
        }
        catch (FinderException e) {
            throw new ServletException(e);
        } catch (RemoveException e) {
            throw new ServletException(e);
        } catch (DataAccessException e) {
            throw new ServletException(e);
        }
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            Object obj = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            authorHome = (AuthorHome) PortableRemoteObject.narrow(obj, AuthorHome.class);
            List<AuthorRemote> allAuthors = new ArrayList<AuthorRemote>();

            req.setAttribute("authorsList", authorHome.getAuthorsInfo());
        } catch (DataAccessException e) {
            throw new ServletException(e);
        } catch (NamingException e) {
            throw  new ServletException(e);
        }
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/deleteAuthor.jsp").forward(req, resp);
    }
}
