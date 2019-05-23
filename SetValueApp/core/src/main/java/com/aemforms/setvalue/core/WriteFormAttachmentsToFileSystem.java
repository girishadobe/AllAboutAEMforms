package com.aemforms.setvalue.core;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Write Adaptive Form Attachments to File System",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Save Adaptive Form Attachments to File System" })
public class WriteFormAttachmentsToFileSystem implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(WriteFormAttachmentsToFileSystem.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments)
			throws WorkflowException {
		// TODO Auto-generated method stub
		log.debug("The string I got was ..." + processArguments.get("PROCESS_ARGS", "string").toString());
		String[] params = processArguments.get("PROCESS_ARGS", "string").toString().split(",");
		String attachmentsPath = params[0];
		String saveToLocation = params[1];
		log.debug("The seperator is" + File.separator);
		String payloadPath = workItem.getWorkflowData().getPayload().toString();

		String attachmentsFilePath = payloadPath + "/" + attachmentsPath + "/attachments";
		log.debug("The data file path is " + attachmentsFilePath);

		ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);

		Resource attachmentsNode = resourceResolver.getResource(attachmentsFilePath);
		Iterator<Resource> attachments = attachmentsNode.listChildren();
		while (attachments.hasNext()) {
			Resource attachment = attachments.next();
			String attachmentPath = attachment.getPath();
			String attachmentName = attachment.getName();

			log.debug("The attachmentPath is " + attachmentPath + " and the attachmentname is " + attachmentName);
			com.adobe.aemfd.docmanager.Document attachmentDoc = new com.adobe.aemfd.docmanager.Document(attachmentPath,
					attachment.getResourceResolver());
			try {
				File file = new File(saveToLocation + File.separator + workItem.getId());
				if (!file.exists()) {
					file.mkdirs();
				}

				attachmentDoc.copyToFile(new File(file + File.separator + attachmentName));

				log.debug("Saved attachment" + attachmentName);
				attachmentDoc.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
