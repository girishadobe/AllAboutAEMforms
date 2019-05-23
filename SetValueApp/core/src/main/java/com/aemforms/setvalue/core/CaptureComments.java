package com.aemforms.setvalue.core;


import java.util.List;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
@Component(property={Constants.SERVICE_DESCRIPTION+"=Capture Workflow Comments",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Capture Workflow Comments"})
public class CaptureComments implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(CaptureComments.class);
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		List<HistoryItem> workItemsHistory = workflowSession.getHistory(workItem.getWorkflow());
		int listSize = workItemsHistory.size();
		String metadataPropertyName = arg2.get("PROCESS_ARGS","string").toString();
		log.info("the size of the history item is ..."+workItemsHistory.size()+"The metadata property name I got was "+metadataPropertyName);
		WorkflowData wfData = workItem.getWorkflowData();
		HistoryItem lastItem = workItemsHistory.get(listSize-1);
		String reviewerComments = (String)lastItem.getWorkItem().getMetaDataMap().get("workitemComment");
		log.debug("####The comment I got was ...."+reviewerComments);
		wfData.getMetaDataMap().put(metadataPropertyName,reviewerComments);
		workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);

		
	}

}
