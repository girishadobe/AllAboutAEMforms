package com.aemforms.setvalue.core;

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

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Add Items To Array List",
		Constants.SERVICE_VENDOR + "=Adobe Systems", "process.label" + "=Add Items to Array List" })
public class AddItemsToArrayList implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(AddItemsToArrayList.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub

		String propertyValues = arg2.get("PROCESS_ARGS", "string").toString();
		System.out.println("The property values are " + propertyValues);

		WorkflowData wfData = workItem.getWorkflowData();

		wfData.getMetaDataMap().put("CustomRoutes", propertyValues.split(","));

		workflowSession.updateWorkflowData(workItem.getWorkflow(), wfData);

	}

}
