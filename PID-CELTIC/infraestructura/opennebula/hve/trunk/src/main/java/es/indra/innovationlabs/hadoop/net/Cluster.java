package es.indra.innovationlabs.hadoop.net;

import java.util.List;

class Cluster {

    private List<Host> hosts ;
    
    public Cluster() {
    }

    public List<Host> getHosts() {
      return hosts;
    }

    public void setHosts(List<Host> hosts) {
      this.hosts = hosts;
    }

}
