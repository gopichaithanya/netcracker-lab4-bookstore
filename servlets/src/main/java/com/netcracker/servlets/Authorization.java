package com.netcracker.servlets;

import com.netcracker.ejb.session.UserAuthorizationService;
import com.netcracker.ejb.session.UserAuthorizationServiceHome;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.Helper;
import org.apache.log4j.Logger;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class Authorization extends HttpServlet {

    private static final Logger log = Logger.getLogger(Authorization.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{

            //--------------------------------------------------
            final String bookJNDI = "ear-1.0/ejbPart/UserAuthorizationService!com.netcracker.ejb.session.UserAuthorizationServiceHome";
            Object obj = Helper.getInstance().getContext().lookup(bookJNDI);
            UserAuthorizationServiceHome userAuthorizationServiceHome = (UserAuthorizationServiceHome) PortableRemoteObject.narrow(obj, UserAuthorizationServiceHome.class);
            UserAuthorizationService userAuthorizationService = userAuthorizationServiceHome.create();
            //--------------------------------------------------

            HttpSession session = req.getSession();
            String status;
            boolean isAdmin = false;
            if (req.getParameter("status")== null) {
                status = "user";
            } else {
                status = "admin";
                isAdmin = true;
            }
            boolean isAuthorized = userAuthorizationService.isUserAuthorized(req.getParameter("nick"), req.getParameter("password"), status);
            if (isAuthorized & isAdmin) {
                session.setAttribute("authorize", true);
                log.info("Administrator " + req.getParameter("nick") + " entered to the bookstore.");
                resp.sendRedirect("/bookStore/books");
            } else if (isAuthorized) {
                log.info("User " + req.getParameter("nick") + " entered to the bookstore.");
                resp.sendRedirect("/bookStore/books");
            } else {
                session.setAttribute("error", true);
                log.warn("User with nickname " + req.getParameter("nick") + " not registered.");
                req.getServletContext().getRequestDispatcher("/WEB-INF/authorization.jsp").forward(req, resp);
                //erase error massage flag
                session.setAttribute("error", false);
            }
        } catch (CreateException e) {
            throw new ServletException("Can't retrieve UserAuthorizationService bean", e);
        } catch (NamingException e) {
            throw new ServletException("Error occurs during lookup UserAuthorizationService bean.", e);
        } catch (DataAccessException e) {
            log.error("Error occurs during work with database", e);
            throw new ServletException("Error occurs during the work with datasource. ", e);
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/authorization.jsp").forward(req, resp);

    }
}
