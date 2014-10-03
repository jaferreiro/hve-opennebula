package es.indra.innovationlabs.hadoop.net;

import java.util.Map;

class Host {

    private String hostname ;
    private Map<String,VM> id2Vm ;
    
    
    public Host() {
    }

    public Host(String hostname) {
        this.hostname = hostname ;
    }
    
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Declares that this Host contains the given VM's id, but don't assign a value.
     * {@link #containsVm(String) will return true after declaring a VM.}
     * @param id
     */
    public Host declareVm(String id) {
        this.id2Vm.put(id, null) ;
        return this ;
    }
    
    public boolean containsVm(String id) {
        return this.id2Vm.containsKey(id) ;
    }
    
    public Host addVm(VM vm) {
        this.id2Vm.put(vm.getId(), vm) ;
        return this ;
    }
    
}
