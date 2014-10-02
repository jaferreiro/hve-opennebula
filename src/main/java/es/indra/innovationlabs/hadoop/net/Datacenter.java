package es.indra.innovationlabs.hadoop.net;

import java.util.List;

class Datacenter {

    private List<Cluster> clusters ;
    
    public Datacenter() {
    }

    public List<Cluster> getClusters() {
      return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
      this.clusters = clusters;
    }

}
