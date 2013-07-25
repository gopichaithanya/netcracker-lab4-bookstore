import com.netcracker.ejb.entity.*;
import com.netcracker.exceptions.DataAccessException;

import javax.ejb.CreateException;
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
import java.io.PrintWriter;
import java.util.Properties;

public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Properties props = new Properties();

            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            props.put(Context.PROVIDER_URL, "remote://localhost:4447");

            props.put(Context.SECURITY_PRINCIPAL, "user");
            props.put(Context.SECURITY_CREDENTIALS, "pass");
            //props.put("jboss.naming.client.ejb.context", true);
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

            Context ctx = new InitialContext(props);

            Object obj = ctx.lookup("ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome");
            GenreHome genreHome = (GenreHome) PortableRemoteObject.narrow(obj, GenreHome.class);
            GenreRemote remote = genreHome.findByPrimaryKey(new Integer(2355));
            out.println(remote.getGenreId() + " : " +remote.getGenreName());
            /*
            for (ThinEntityWrapper th : genreHome.getGenresInfo()) {
                out.println("<ul>");
                out.println("<li>" + th.getId() + "///" + th.getInfo() + "</li>");
                out.println("</ul>");
            }*/

            /*ThinEntityWrapper th = genreHome.getGenresInfo().iterator().next();
            response.getWriter().println("<strong>" + th.getId()+ ":" +th.getInfo() + "</strong>");*/


            Object obj2 = ctx.lookup("ear-1.0/ejbPart/AuthorBean!com.netcracker.ejb.entity.AuthorHome");
            AuthorHome authorHome = (AuthorHome) PortableRemoteObject.narrow(obj2, AuthorHome.class);
            //AuthorRemote authorRemote = authorHome.findByPrimaryKey(new Integer(1305));
            AuthorRemote authorRemote1 = authorHome.create("Lev", "Tolstoy");
            //out.println(authorRemote.getAuthorId() + " : " +authorRemote.getAuthorFirstName() + authorRemote.getAuthorLastName());
            out.println(authorRemote1.getAuthorId() + " : " +authorRemote1.getAuthorFirstName() + authorRemote1.getAuthorLastName());



            Object obj3 = ctx.lookup("ear-1.0/ejbPart/PublisherBean!com.netcracker.ejb.entity.PublisherHome");
            PublisherHome publisherHome = (PublisherHome) PortableRemoteObject.narrow(obj3, PublisherHome.class);
            PublisherRemote publisherRemote = publisherHome.findByPrimaryKey(new Integer(125));
            out.println(publisherRemote.getPublisherId() + " : " +publisherRemote.getPublisherName() + publisherRemote.getPublisherURL());

        } catch (NamingException e) {
            throw new ServletException(e);
        } catch (DataAccessException e) {
            throw new ServletException(e);
        } catch (FinderException e) {
            throw new ServletException(e);
    //    } catch (CreateException e) {
    //        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CreateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
