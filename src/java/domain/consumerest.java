/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
   
   



/**
 *
 * @author oracle
 */
public class consumerest {
   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:7001/ProiectJavaWeb_bun_1/webresources/domain.autori");
        String s = webResource.get(String.class);
        System.out.println(s);
       
        
        
    }
    
}
