package coryfoo.cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GridCenteredCluster implements ICluster {
  
  int width;
  int height;
  int grid;

  public GridCenteredCluster( int width, int height, int grid ) {
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
        marker.x = grid*(p.x / grid) + grid/2;
        marker.y = grid*(p.y / grid) + grid/2;;
        marker.count = 1;
      } else {
        marker.count++;
      }
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
