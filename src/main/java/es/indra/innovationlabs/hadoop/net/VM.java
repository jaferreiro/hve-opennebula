package es.indra.innovationlabs.hadoop.net;

class VM {

    private String id ;
    private String hostname ;
    private String ip ;
    
    public VM() {
    }

    public VM(String id, String ip, String hostname) {
        this.id = id ;
        this.ip = ip ;
        this.hostname = hostname ;
    }
    
    public String getIp() {
        return ip;
    }

    public VM setIp(String ip) {
        this.ip = ip;
        return this ;
    }

    public String getHostname() {
        return hostname;
    }

    public VM setHostname(String hostname) {
        this.hostname = hostname;
        return this ;
    }

    public String getId() {
        return id;
    }

    public VM setId(String id) {
        this.id = id;
        return this ;
    }
    

}
