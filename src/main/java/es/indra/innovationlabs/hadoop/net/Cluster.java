package es.indra.innovationlabs.hadoop.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class Cluster {

    private String nombre ;
    private Map<String,Host> hosts = new HashMap<String, Host>();
    
    public Cluster(String nombre) {
        this.nombre = nombre ;
    }

    public String getNombre() {
        return this.nombre ;
    }

    public Cluster addHost(Host host) {
        this.hosts.put(host.getHostname(), host) ;
        return this ;
    }
    
    public Host getHost(String hostname) {
        return this.hosts.get(hostname) ;
    }
    
    public Host getOrCreateHost(String hostname) {
        if (!this.hosts.containsKey(hostname)) {
            this.hosts.put(hostname, new Host(hostname)) ;
        }
        return this.hosts.get(hostname) ;
    }
    
    public Cluster addVmInfo(String vmId, VM vm) {
        for (Entry<String,Host> entry: this.hosts.entrySet()) {
            Host host = entry.getValue() ;
            if (host.containsVm(vmId)) {
                host.addVm(vm) ;
            }
        }
        return this ;
    }
    

}
