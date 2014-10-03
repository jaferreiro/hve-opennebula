package es.indra.innovationlabs.hadoop.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.net.DNSToSwitchMapping;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.host.HostPool;

/**
 * This class implements the {@link DNSToSwitchMapping} interface to resolve the
 * topology on opennebula managed clusters.
 * 
 * Must configure the OpenNebula Cloud API (OCA) 2.0 server URL via
 * <code>topology.opennebula.server.url</code>. If the key does not exists
 * in the configuration files, the endpoint wil be assumed to be at
 * <code>$ONE_XMLRPC</code>.
 * 
 * The server user and password must be configured via
 * <code>topology.opennebula.server.user</code> and
 * <code>topology.opennebula.server.password</code>
 * 
 * If neither key exists in the configuration files, the user and password
 * will be assumed to be at <code>$ONE_AUTH</code> (user:password)
 * 
 */
public class OpenNebulaMapping extends Configured implements DNSToSwitchMapping {

    public static final String USER_KEY = "topology.opennebula.server.user" ;
    public static final String PASSWORD_KEY = "topology.opennebula.server.password" ;
    public static final String SERVER_URL_KEY = "topology.opennebula.server.url" ;
    
    private Client ocaClient ;
    private Datacenter datacenter ;
    
    Map<String,String> hostname2Cluster = new HashMap<String,String>() ;    
    
    // Lazy loadings ---------------------------------------------------------
    
    private Client getOcaClient() throws ClientConfigurationException {
        if (this.ocaClient == null) {
            String user = this.getConf().get(USER_KEY) ;
            String password = this.getConf().get(PASSWORD_KEY) ;
            String endpoint = this.getConf().get(SERVER_URL_KEY) ;
            String secret = null ; // user:password
            
            if (user != null && password != null &&
                !"".equals(user) && !"".equals(password))
            {
                secret = user + ":" + password ;
            }
            
            this.ocaClient = new Client(secret, endpoint) ;
        }
        return this.ocaClient ;
    }

    private void CargarInfo() {
        HostPool hostPool = new HostPool(ocaClient) ;
        Iterator<org.opennebula.client.host.Host> hostIt = hostPool.iterator() ;
        while (hostIt.hasNext()) {
            org.opennebula.client.host.Host host= hostIt.next() ;
            String hostName = host.getName() ;
            String clusterName = host.xpath("CLUSTER") ;
            if (clusterName == null || clusterName.equals("")) {
                clusterName = "defaultCluster" ;
            }
            this.hostname2Cluster.put(hostName, clusterName) ;
        }
        
        
        
    }
    
    // -----------------------------------------------------------------------
    
    public OpenNebulaMapping() {
    }

    public OpenNebulaMapping(Configuration conf) {
        super(conf);
        cargarInfo() ;
    }

    public List<String> resolve(List<String> names) {
        this.getOcaClient().
        
        return null;
    }

}
