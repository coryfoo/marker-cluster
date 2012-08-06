package coryfoo.cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GridAvgCenteredCluster implements ICluster {

  int width;
  int height;
  int grid;

  public GridAvgCenteredCluster( int width, int height, int grid ) {
    this.width = width;
    this.height = height;
    this.grid = grid;
  }

  public Collection<ClusterMarker> createClusters( List<Point> matrix ) {
    HashMap<Integer, ClusterMarker> regions = new HashMap<Integer, ClusterMarker>();
    for ( Point p : matrix ) {
      int region = getRegion( p );

      ClusterMarker marker = regions.get( region );
      if ( marker == null ) {
        marker = new ClusterMarker();
        marker.x = p.x;
        marker.y = p.y;
        marker.count = 1;
      } else {
        marker.x += p.x;
        marker.y += p.y;
        marker.count++;
      }
      regions.put( region, marker );
    }
    
    // now calculate the avg positions
    for ( Integer region : regions.keySet() ) {
      ClusterMarker marker = regions.get( region );
      marker.x = marker.x / marker.count;
      marker.y = marker.y / marker.count;
      regions.put( region, marker );
    }
    
    return regions.values();
  }
  
  private int getRegion(Point p) {
    int temp = p.x / grid;
    temp += (p.y / grid) * (width / grid);
    return temp;
  }
}
