package coryfoo.web;

import coryfoo.cluster.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class DataGeneratorServlet extends HttpServlet {
  
  static final Map<Integer, List<Point>> cache = new HashMap<Integer, List<Point>>();
  static final Random RAND = new Random( new Date().getTime() );

  @Override
  protected void service( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
    Integer size = Integer.valueOf( req.getParameter( "size" ) );
    Integer width = Integer.valueOf( req.getParameter( "width" ) );
    Integer height = Integer.valueOf( req.getParameter( "height" ) );
    String clusterType = req.getParameter( "cluster" );

    List<Point> matrix = cache.get( size );
    Object data = matrix;
    if ( matrix == null ) {
      int max = width*height;
      matrix = new ArrayList<Point>(size);
      while ( 0 < size-- ) {
        int value = RAND.nextInt( max );
        Point p = new Point();
        p.x = value % width;
        p.y = value / width;
        matrix.add( p );
      }

      cache.put( Integer.valueOf( req.getParameter( "size" ) ), matrix );
      data = matrix;

    } else if ( "grid-center".equals( clusterType ) ) {
      ICluster cluster = new GridCenteredCluster( width, height, 100 );
      data = cluster.createClusters( matrix );
    } else if ( "grid-avg".equals( clusterType ) ) {
      ICluster cluster = new GridAvgCenteredCluster( width, height, 100 );
      data = cluster.createClusters( matrix );
    } else if ( "float".equals( clusterType ) ) {
      ICluster cluster = new FloatingCluster( width, height, Integer.valueOf( req.getParameter( "maxDistance" ) ) );
      data = cluster.createClusters( matrix );
    }

    resp.setContentType( "application/json" );
    resp.getWriter().write( new Gson().toJson( data ) );
  }
  
}
