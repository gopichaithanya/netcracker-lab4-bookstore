package com.netcracker.servlets;

import com.netcracker.ejb.entity.AuthorHome;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;

public class AddAuthor extends HttpServlet {

    private static final Logger log = Logger.getLogger(AddAuthor.class);

    private String firstName;
    private String lastName;

    private AuthorHome authorHome;


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Context ctx = Helper.getInstance().getContext();
            firstName = req.getParameter("firstName");
            lastName = req.getParameter("lastName");
            Object obj = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            authorHome = (AuthorHome) PortableRemoteObject.narrow(obj, AuthorHome.class);
            if (isAuthorExist(firstName, lastName, req)) {
                req.setAttribute("alreadyExist", true);
                doGet(req, resp);
                req.setAttribute("alreadyExist", false);
            } else if (!isAuthorValid(firstName, lastName)) {
                req.setAttribute("error", true);
                doGet(req, resp);
                req.setAttribute("error", false);
            } else {
                authorHome.create(firstName, lastName);
                log.info("Author  " + firstName + " " + lastName+ " successfully added.");

                if ((Boolean)req.getSession().getAttribute("fromAddBookPage")) {
                     resp.sendRedirect("http://localhost:8080/bookStore/addBook");
                } else {
                    req.getServletContext().getRequestDispatcher("/books").forward(req, resp);
                }

            }
        } catch (CreateException e) {
            log.error("Error occurs during adding author to the database", e);
            throw new ServletException(e);
        } catch (NamingException e) {
            log.error("Error occurs during adding author to the database", e);
            throw new ServletException(e);
        }

    }

    /**
     * Figure out is author with such first and last name already exist or not.
     *
     * @param firstName         author's first name
     * @param lastName          author's last name
     * @return                  exist or not

     * @throws ServletException
     */
    private boolean isAuthorExist(String firstName, String lastName, HttpServletRequest req) throws ServletException {
        boolean authorExist;
        try {
                authorExist = authorHome.isAuthorExist(firstName, lastName);

        } catch (RemoteException e) {
            log.error("Error occurs during finding out is author exist in the database", e);
            throw new ServletException(e);
        } catch (FinderException e) {
            log.error("Error occurs during finding out is author exist in the database", e);
            throw new ServletException(e);
        }
        return authorExist;
    }

    private boolean isAuthorValid(String firstName, String lastName) {
        return !(firstName.isEmpty()
                || lastName.isEmpty()
                || firstName.matches(".*\\d.*")
                || lastName.matches(".*\\d.*"));
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/addAuthor.jsp").forward(req, resp);
    }
}
