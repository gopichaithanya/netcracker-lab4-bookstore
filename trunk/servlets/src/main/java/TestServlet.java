import com.netcracker.ejb.entity.*;
import com.netcracker.ejb.session.BookSearchService;
import com.netcracker.ejb.session.BookSearchServiceHome;
import com.netcracker.exceptions.DataAccessException;
import com.netcracker.helper.ThinEntityWrapper;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
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
import java.util.ArrayList;
import java.util.Collection;
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

            // lookup book
            Object obj = ctx.lookup("ear-1.0/ejbPart/BookBean!com.netcracker.ejb.entity.BookHome");
            BookHome bookHome = (BookHome) PortableRemoteObject.narrow(obj, BookHome.class);
            ArrayList<Integer> authors = new ArrayList<Integer>();
            authors.add(new Integer(1307));
            Book book = bookHome.create("NewBook", 129, 2345, "Newly Created Book", "999.jpg", 1999, authors);

            // lookup genre
            /*Object obj1 = ctx.lookup("ear-1.0/ejbPart/GenreBean!com.netcracker.ejb.entity.GenreHome");
            GenreHome genreHome = (GenreHome) PortableRemoteObject.narrow(obj1, GenreHome.class);
            Genre genre = genreHome.findByPrimaryKey(new Integer(2355));

            book.setGenre(genre);


            String res = book.getGenre().getName();*/
            out.println("<strong>Added</strong>");


            /*Collection<ThinEntityWrapper> books = service.getBooksShortInfo(-1, -1);
            for (ThinEntityWrapper book : books) {
                out.println("<strong>" + book.getId() + " / " + book.getInfo() +" / "+ book.getAddInfo() +"</strong><br/>");
            }*/


        } catch (NamingException e) {
            throw new ServletException(e);
        } /*catch (DataAccessException e) {
            throw new ServletException(e);
        } catch (FinderException e) {
            throw new ServletException(e);
        }*/ catch (CreateException e) {
            throw new ServletException(e);
        } /*catch (RemoveException e) {
            throw new ServletException(e);
        }*/
    }
}
