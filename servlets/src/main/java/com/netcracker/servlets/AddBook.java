package com.netcracker.servlets;

import com.netcracker.ejb.entity.*;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;


public class AddBook extends HttpServlet {


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {




        try {
            Properties props = new Properties();

            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            props.put(Context.PROVIDER_URL, "remote://localhost:4447");

            props.put(Context.SECURITY_PRINCIPAL, "user");
            props.put(Context.SECURITY_CREDENTIALS, "pass");
            //props.put("jboss.naming.client.ejb.context", true);
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

            Context ctx = new InitialContext(props);



            Object obj3 = ctx.lookup("ear-1.0/ejbPart/PublisherBean!com.netcracker.ejb.entity.PublisherHome");
            PublisherHome publisherHome = (PublisherHome) PortableRemoteObject.narrow(obj3, PublisherHome.class);
            Collection<ThinEntityWrapper> publishers = publisherHome.getPublishersInfo();
            req.setAttribute("publishers", publishers);
            //out.println(publishersRemote.getPublisherId() + " : " +publishersRemote.getPublisherName() + publishersRemote.getPublisherURL());
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/AddBook.jsp").forward(req, resp);


        } catch (NamingException e) {
            throw new ServletException(e);
       // } catch (DataAccessException e) {
       //     throw new ServletException(e);
        } catch (DataAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }



}
