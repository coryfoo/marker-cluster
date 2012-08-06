package coryfoo.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FloatingCluster implements ICluster {

  int width;
  int height;
  int maxDistance;

  public FloatingCluster( int width, int height, int maxDistance ) {
    this.width = width;
    this.height = height;
    this.maxDistance = maxDistance;
  }

  public Collection<ClusterMarker> createClusters( List<Point> matrix ) {
    // Ok, naive approach:
    // - Iterate over each point in the matrix, while also iterating over all the saved points
    // - If the Point is not less than maxDistance away, it is now a new save point, otherwise, add it to the group 
    
    List<ClusterMarker> markers = new ArrayList<ClusterMarker>();
    for ( Point p : matrix ) {
      ClusterMarker marker = null;
      for ( ClusterMarker m : markers ) {
        if ( Math.abs( p.x - m.x ) <= maxDistance && Math.abs( p.y - m.y ) <= maxDistance ) {
          marker = m;
          break;
        }
      }
      
      if ( marker != null ) {
        marker.count++;
      } else {
        marker = new ClusterMarker();
        marker.count = 1;
        marker.x = p.x;
        marker.y = p.y;

        markers.add( marker );
      }
    }
    
    return markers;
  }
  
}
