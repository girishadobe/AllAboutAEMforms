package com.aemforms.setvalue.core;

import javax.jcr.Session;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Set MetaData", Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Get Delivery Details" })
public class GetDeliveryPref implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(GetDeliveryPref.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		WorkflowData wfData = workItem.getWorkflowData();
		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		System.out.println("hte payload path in my workflow is " + payloadPath);
		Session session = workflowSession.adaptTo(Session.class);
		ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);

		Resource resNode = resourceResolver.getResource(payloadPath);
		ModifiableValueMap mapProp = resNode.adaptTo(ModifiableValueMap.class);
		wfData.getMetaDataMap().put("email", (String) mapProp.get("email"));
		wfData.getMetaDataMap().put("phone", (String) mapProp.get("phone"));
		workflowSession.updateWorkflowData(workItem.getWorkflow(), wfData);
		System.out.println("The workflow meta data was updated");

	}

}
