package com.aemforms.setvalue.core;


import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;


@Component(property={Constants.SERVICE_DESCRIPTION+"=Get Manager Name",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Get Manager Name"})
public class GetManager implements ParticipantStepChooser {

	@Reference
	ResourceResolverFactory resourceResolverFactory;
	private static final Logger log = LoggerFactory.getLogger(GetManager.class);
	@Override
	public String getParticipant(WorkItem workItem, WorkflowSession wfSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		ResourceResolver resourceResolver = null;
		String manager = null;
		log.debug("The workflow initiator is ..."+workItem.getWorkflow().getInitiator());
		try {
			//resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			//resourceResolver = getResolver.getServiceResolver();
			resourceResolver = wfSession.adaptTo(ResourceResolver.class);
			UserManager userManager = resourceResolver.adaptTo(UserManager.class);
			Authorizable workflowInitiator = userManager.getAuthorizable(workItem.getWorkflow().getInitiator());
			log.debug("The family name of the workflow initiator is... "+workflowInitiator.getProperty("profile/familyName")[0].getString());
			log.debug("The manager of workflow initiator is.... "+workflowInitiator.getProperty("profile/manager")[0].getString());
			String managerPorperty = workflowInitiator.getProperty("profile/manager")[0].getString();
			String split[] = managerPorperty.split(",");
			manager = split[0].substring(3);
			log.debug("@@@@@The manager name is "+manager);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return manager;
	}
	 
}
