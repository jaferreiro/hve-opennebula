package es.indra.innovationlabs.hadoop.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class Datacenter {

    private Map<String,Cluster> id2Cluster = new HashMap<String, Cluster>();
    
    
    public Datacenter() {
    }

    public Cluster getOrCreateCluster(String nombreCluster) {
        if (!existeCluster(nombreCluster)) {
            this.id2Cluster.put(nombreCluster, new Cluster(nombreCluster)) ;
        }
        return this.getCluster(nombreCluster) ;
    }

    public Cluster getCluster(String nombreCluster) {
        return this.id2Cluster.get(nombreCluster) ;
    }
    
    public boolean existeCluster(String nombreCluster) {
        return this.id2Cluster.containsKey(nombreCluster) ;
    }
    
    public Map<String,String> getHostname2Path() {
        
    }
    
    public Map<String,String> getIp2Path() {
        
    }
    
    public void cargarDatos(Client ocaClient) {
        HostPool hostPool = new HostPool(ocaClient) ;
        OneResponse response = hostPool.info() ;
        // TODO
        if (response.isError()) {
            throw new RuntimeException("error") ;
        }

        // From ocaHost we get clusters and vm's id.
        Iterator<org.opennebula.client.host.Host> ocaHostIt = hostPool.iterator() ;
        while (ocaHostIt.hasNext()) {
            org.opennebula.client.host.Host ocaHost= ocaHostIt.next() ;

            // Create cluster
            String nombreCluster = ocaHost.xpath("CLUSTER") ;
            if (nombreCluster == null || nombreCluster.equals("")) {
                nombreCluster = "defaultCluster" ;
            }
            Cluster cluster = this.getOrCreateCluster(nombreCluster) ;
            
            // Create host
            Host host = new Host(ocaHost.getName()) ; // Physical host
            cluster.addHost(host) ;

            NodeList poolElements = null ;
            try
            {
                DocumentBuilder builder;
                Document        doc;
                Element         xml;

                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.parse(new ByteArrayInputStream(ocaHost.info().getMessage().getBytes()));
                xml = doc.getDocumentElement();

                poolElements = xml.getElementsByTagName("VMS/ID");
            }
            catch (ParserConfigurationException e) {}
            catch (SAXException e) {}
            catch (IOException e) {}            
            
            // Declare VMs
            for (int i=0 ; i<poolElements.getLength(); i++) {
                Node node = poolElements.item(i) ;
                host.declareVm(node.getTextContent()) ;
            }
        }
        
        // Now get all VMs and assign to the proper cluster by VM id
        VirtualMachinePool vmPool = new VirtualMachinePool(ocaClient) ;
        response = vmPool.info() ;
        // TODO
        if (response.isError()) {
            throw new RuntimeException("error") ;
        }
        
        Iterator<VirtualMachine> vmPoolIt= vmPool.iterator() ;
        while(vmPoolIt.hasNext()) {
            VirtualMachine ocaVm = vmPoolIt.next() ;
            
            VM vm = new VM()
                        .setHostname(ocaVm.getName())
                        .setId(ocaVm.getId())
                        .setIp(ocaVm.xpath("NIC/IP")) ;
            this.addVM(vm.getId(), vm) ;
        }
        
        // TODO: ===========================================================
        // Generar las tablas inversas
        
    }
    
    private Datacenter addVM(String vmId, VM vm) {
        for (Entry<String,Cluster> entry: this.id2Cluster.entrySet()) {
            Cluster cluster = entry.getValue() ;
            cluster.addVmInfo(vmId, vm) ;
        }
        return this ;
    }
    
}
