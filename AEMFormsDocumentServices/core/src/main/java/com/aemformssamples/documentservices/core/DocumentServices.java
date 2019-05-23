package com.aemformssamples.documentservices.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.sling.api.resource.PersistenceException;
import org.json.JSONObject;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.pdfg.exception.ConversionException;
import com.adobe.pdfg.exception.FileFormatNotSupportedException;
import com.adobe.pdfg.exception.InvalidParameterException;

public abstract interface DocumentServices {
	public abstract com.adobe.aemfd.docmanager.Document orgw3cDocumentToAEMFDDocument(
			org.w3c.dom.Document paramDocument);

	public Document createDDXForFragments(String masterTemplate, Map<Integer, Map> fragmentsToAssemble);

	public String insertFragments(String masterTemplate, Map<Integer, Map> fragmentsToAssemble, Document ddx);

	public Document GenerateDor(String afPath, String dataXml);

	public Document mobileFormToPDF(String xmlData);

	public Document mobileFormToInteractivePdf(Document xmlData);

	public abstract String saveDocumentInCrx(String jcrPath, Document documentToSave);

	public abstract com.adobe.aemfd.docmanager.Document createSimpleDDX(Map<String, Object> paramMap);

	public abstract com.adobe.aemfd.docmanager.Document createDDXFromMapOfDocuments(
			Map<String, com.adobe.aemfd.docmanager.Document> paramMap);

	public abstract com.adobe.aemfd.docmanager.Document assembleDocuments(Map<String, Object> paramMap,
			com.adobe.aemfd.docmanager.Document paramDocument);

	public abstract InputStream certifyDocument(com.adobe.aemfd.docmanager.Document paramDocument, String paramString1,
			String paramString2);

	public abstract org.w3c.dom.Document w3cDocumentFromStrng(String paramString);

	public abstract com.adobe.aemfd.docmanager.Document renderAndExtendXdp(String paramString);

	public abstract JSONObject extractBarCode(com.adobe.aemfd.docmanager.Document paramDocument);

	public abstract JSONObject ocrScannedImage(com.adobe.aemfd.docmanager.Document paramDocument, String paramString)
			throws PersistenceException, ConversionException, InvalidParameterException,
			FileFormatNotSupportedException, ItemExistsException, PathNotFoundException, NoSuchNodeTypeException,
			LockException, VersionException, ConstraintViolationException, RepositoryException;

	public abstract String ocrDocument(String paramString1, String paramString2)
			throws ConversionException, InvalidParameterException, FileFormatNotSupportedException, ItemExistsException,
			PathNotFoundException, NoSuchNodeTypeException, LockException, VersionException,
			ConstraintViolationException, RepositoryException, IOException;
}
