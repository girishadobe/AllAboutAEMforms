package com.aemforms.setvalue.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(property={Constants.SERVICE_DESCRIPTION+"=Set Value",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Set Value of Element in Json"})
public class SetValueInJson implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(SetValueInJson.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		log.debug("The string I got was ..."+arg2.get("PROCESS_ARGS","string").toString());
		String params = arg2.get("PROCESS_ARGS","string").toString();
		log.debug("The params string is "+params);
		String parameters[] = params.split(",");
		log.debug("The length is .."+parameters.length);
		String nodeName = parameters[0];
		log.debug("The node name is .."+nodeName);
		String objects[] = nodeName.split("\\.");
		log.debug("The objects length is .."+objects.length);
		String propertyName = objects[objects.length-1];
		log.debug("the property name is..."+propertyName);
		String newValue = parameters[1];
		log.debug("The new value of "+propertyName+" to be set is  .."+newValue);
		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		log.debug("The payload  in set Elmement Value in Json is  "+workItem.getWorkflowData().getPayload().toString());
		String dataFilePath = payloadPath+"/Data.xml/jcr:content";
		Session session = workflowSession.adaptTo(Session.class);
		Node xmlDataNode = null;
		try {
			xmlDataNode = session.getNode(dataFilePath);
			JSONObject finalObject = null;
			InputStream xmlDataStream = xmlDataNode.getProperty("jcr:data").getBinary().getStream();
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(xmlDataStream,"UTF-8"));
		    StringBuilder responseStrBuilder = new StringBuilder();

		    String inputStr;
		    while ((inputStr = streamReader.readLine()) != null)
		        responseStrBuilder.append(inputStr);
		    JSONObject jo = new JSONObject(responseStrBuilder.toString());
			for(int i=0;i<objects.length-1;i++)
			{
				if(i==0)
		    	{
		    		finalObject = jo.getJSONObject(objects[i]);
		    	}
		    	else
		    	{
		    		finalObject = finalObject.getJSONObject(objects[i]);
		    		System.out.println("The name is .."+finalObject.toString());
		    	}
		    	
			}
		System.out.println("The value is "+finalObject.getString(propertyName));
		finalObject.put(propertyName, newValue);
		InputStream is = new ByteArrayInputStream(jo.toString().getBytes());
		System.out.println("The value of JO is "+jo.toString());
		Binary binary = session.getValueFactory().createBinary(is);
		xmlDataNode.setProperty("jcr:data", binary);
		session.save();
		
		} catch (PathNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
