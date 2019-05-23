package com.aemforms.setvalue.core;

import java.io.InputStream;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.adobe.fd.cpdf.api.ConvertPdfService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.adobe.pdfg.result.HtmlToPdfResult;
import com.adobe.pdfg.service.api.GeneratePDFService;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;


@Component(property={Constants.SERVICE_DESCRIPTION+"=Tag and Store Dor in DAM",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Tag and Store Dor in DAM"})
@Designate(ocd = TagDorServiceConfiguration.class)
public class TagAndStoreDoRinDAM implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(TagAndStoreDoRinDAM.class);

	
	private TagDorServiceConfiguration serviceConfig;
	@Activate
	public void activate(TagDorServiceConfiguration config) {
		this.serviceConfig = config;
		}
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		
		log.debug("The process arguments passed ..."+arg2.get("PROCESS_ARGS","string").toString());
		String params = arg2.get("PROCESS_ARGS","string").toString();
		WorkflowModel wfModel = workflowSession.getModel("/var/workflow/models/dam/update_asset");
		
		String damFolder = serviceConfig.damFolder();
		String dorPDFName = serviceConfig.dorPath();
		String dataXmlFile = serviceConfig.dataFilePath();
		log.debug("The Data Xml File is ..."+dataXmlFile+"DorPDFName"+dorPDFName);
		
		String parameters[] = params.split(",");
		log.debug("The %%%% length of parameters is "+parameters.length);
		Tag []tagArray = new Tag[parameters.length];
		WorkflowData wfData = workItem.getWorkflowData();
		String dorFileName = (String) wfData.getMetaDataMap().get("filename");
		log.debug("The dorFileName is ..."+dorFileName);
		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		String dataFilePath = payloadPath+"/"+dataXmlFile+"/jcr:content";
		
		String dorDocumentPath = payloadPath+"/"+dorPDFName+"/jcr:content";
		
		System.out.println("The dor path is ..."+dorDocumentPath);
		log.debug("Data File Path"+dataFilePath);
		log.debug("Dor File Path"+dorDocumentPath);
		Session session = workflowSession.adaptTo(Session.class);
		ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);
		com.day.cq.dam.api.AssetManager assetMgr = resourceResolver.adaptTo(com.day.cq.dam.api.AssetManager.class);
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document xmlDocument= null;
		Node xmlDataNode = null;
		Node dorDocumentNode = null;
		try
			{
				xmlDataNode = session.getNode(dataFilePath);
				log.debug("xml Data Node"+xmlDataNode.getName());
				dorDocumentNode = session.getNode(dorDocumentPath);
				log.debug("DOR Document Node is "+dorDocumentNode.getName());
			}
		catch (PathNotFoundException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		catch (RepositoryException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
				InputStream xmlDataStream = xmlDataNode.getProperty("jcr:data").getBinary().getStream();
				InputStream dorInputStream = dorDocumentNode.getProperty("jcr:data").getBinary().getStream();
				XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
				xmlDocument = builder.parse(xmlDataStream);
				String newFile = "/content/dam/"+damFolder+"/"+dorFileName ; 
				log.debug("the new file is ..."+newFile);
			    assetMgr.createAsset(newFile, dorInputStream,"application/pdf", true);
			    WorkflowData wfDataLoad = workflowSession.newWorkflowData("JCR_PATH",newFile);
			    log.debug("Wrote the document to DAM"+newFile);
			    TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
			    Resource pdfDocumentNode = resourceResolver.getResource(newFile);
				Resource metadata = pdfDocumentNode.getChild("jcr:content/metadata");
				for(int i=0;i<parameters.length;i++)
				{
						String tagTitle = parameters[i].split("=")[0];
						log.debug("The tag title is"+tagTitle);
						String nameOfNode = parameters[i].split("=")[1];
						org.w3c.dom.Node xmlElement = (org.w3c.dom.Node)xPath.compile(nameOfNode).evaluate(xmlDocument, javax.xml.xpath.XPathConstants.NODE);
						log.debug("###The value data node is "+xmlElement.getTextContent());
						Tag tagFound = tagManager.resolveByTitle(tagTitle+xmlElement.getTextContent());
						log.debug("The tag found was ..."+tagFound.getPath());
						tagArray[i] = tagFound;
				}
				tagManager.setTags(metadata, tagArray, true);
				workflowSession.startWorkflow(wfModel, wfDataLoad);
				log.debug("Workflow started");
				log.debug("Done setting tags");
				xmlDataStream.close();
				dorInputStream.close();
       		}
			catch(Exception e)
			{
				e.printStackTrace();
			
			}

	}

}
