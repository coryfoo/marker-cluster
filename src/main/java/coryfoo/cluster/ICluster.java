package coryfoo.cluster;

import java.util.Collection;
import java.util.List;

public interface ICluster {

  public Collection<ClusterMarker> createClusters( List<Point> matrix );

}
