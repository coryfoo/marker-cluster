package coryfoo.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpServer {
  
  public static void main( String[] args ) throws Exception {
    Server server = new Server();

    SelectChannelConnector con = new SelectChannelConnector();
    con.setPort(8000);
    server.addConnector(con);

    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed( true );
    resourceHandler.setResourceBase( "/home/cory/projects/marker-cluster/src/main/resources/" );
    resourceHandler.setWelcomeFiles( new String[]{ "index.html" } );

    ServletContextHandler context = new ServletContextHandler( ServletContextHandler.SESSIONS );
    context.setContextPath( "/data" );
    context.addServlet( new ServletHolder( new DataGeneratorServlet() ), "/" );

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resourceHandler, context });
    server.setHandler(handlers);

    server.start();
    server.join();


  }

}
