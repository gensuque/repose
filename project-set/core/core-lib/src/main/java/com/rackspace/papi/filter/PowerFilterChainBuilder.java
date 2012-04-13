/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rackspace.papi.filter;

import com.rackspace.papi.filter.resource.ResourceConsumerCounter;
import com.rackspace.papi.commons.util.Destroyable;
import com.rackspace.papi.model.DomainNode;
import com.rackspace.papi.model.ServiceDomain;
import java.util.List;
import javax.servlet.*;

/**
 *
 * @author zinic
 */
public class PowerFilterChainBuilder implements Destroyable {

   private final ResourceConsumerCounter resourceConsumerMonitor;
   private final List<FilterContext> currentFilterChain;
   private final ServiceDomain domain;
   private final DomainNode localhost;
   

   public PowerFilterChainBuilder(ServiceDomain domain, DomainNode localhost, List<FilterContext> currentFilterChain) {
      this.currentFilterChain = currentFilterChain;
      resourceConsumerMonitor = new ResourceConsumerCounter();
      this.domain = domain;
      this.localhost = localhost;
   }

   public ResourceConsumerCounter getResourceConsumerMonitor() {
      return resourceConsumerMonitor;
   }
   
   public PowerFilterChain newPowerFilterChain(FilterChain containerFilterChain, ServletContext servletContext) {
      return new PowerFilterChain(domain, localhost, currentFilterChain, containerFilterChain, servletContext, resourceConsumerMonitor);
   }
   
   public ServiceDomain getServiceDomain() {
      return domain;
   }
   
   public DomainNode getLocalhost() {
      return localhost;
   }

   @Override
   public void destroy() {
      for (FilterContext context : currentFilterChain) {
         context.destroy();
      }
   }
}
