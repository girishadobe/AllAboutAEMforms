package com.aemforms.setvalue.core;

import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.aemfd.watchfolder.service.api.ContentProcessor;
import com.adobe.aemfd.watchfolder.service.api.ProcessorContext;
import com.adobe.aemfd.watchfolder.workflow.api.WorkflowContext;
import com.mergeandfuse.getserviceuserresolver.GetResolver;

@Component(service = { TriggerWorkflowFromWatchedFolder.class, ContentProcessor.class }, property = {
		"girish=bedekar" })

public class TriggerWorkflowFromWatchedFolder implements ContentProcessor {
	@Reference
	GetResolver getResolver;
	@Reference
	WorkflowContext wfContext;

	@Override
	public void processInputs(ProcessorContext context) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("I am in my workflow");

		ResourceResolver serviceResolver = getResolver.getServiceResolver();
		Session session = serviceResolver.adaptTo(Session.class);
		ValueFactory valueFactory = session.getValueFactory();
		Map.Entry<String, Document> e = context.getInputMap().entrySet().iterator().next();
		System.out.println("The key is " + e.getKey());
		Document inputDocument = e.getValue();
		Binary contentValue = valueFactory.createBinary(inputDocument.getInputStream());
		Node jsonFiles = serviceResolver.getResource("/content/jsonfiles").adaptTo(Node.class);
		System.out.println("The jcrNode name is " + jsonFiles.getName());

		Node jsonFile = jsonFiles.addNode(e.getKey(), "nt:file");
		System.out.println("The jsonFile node was created");
		Node resNode = jsonFile.addNode("jcr:content", "nt:resource");
		resNode.setProperty("jcr:data", contentValue);

		serviceResolver.commit();

		// WorkflowSession wfSession =
		// serviceResolver.adaptTo(WorkflowSession.class);
		// com.day.cq.workflow.WorkflowSession wfSession =
		// workflowService.getWorkflowSession(session);
		// com.day.cq.workflow.model.WorkflowModel wfModel =
		// wfSession.getModel("/var/workflow/models/FromWatchedFolder");

		// com.day.cq.workflow.model.WorkflowModel wfModel =
		// wfSession.getModel("/var/workflow/models/FromWatchedFolder");
		// com.day.cq.workflow.exec.WorkflowData wfDataLoad =
		// wfSession.newWorkflowData("JCR_PATH", "abc");
		// wfSession.startWorkflow(wfModel, wfDataLoad);

	}

}
