package com.aemformssamples.documentservices.core.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jcr.Binary;
import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.adobe.aemds.guide.addon.dor.DoRGenerationException;
import com.adobe.aemds.guide.addon.dor.DoROptions;
import com.adobe.aemds.guide.addon.dor.DoRService;
import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.assembler.client.AssemblerOptionSpec;
import com.adobe.fd.assembler.client.AssemblerResult;
import com.adobe.fd.assembler.client.OperationException;
import com.adobe.fd.assembler.service.AssemblerService;
import com.adobe.fd.bcf.api.BarcodedFormsService;
import com.adobe.fd.bcf.api.CharSet;
import com.adobe.fd.bcf.api.Delimiter;
import com.adobe.fd.bcf.api.XMLFormat;
import com.adobe.fd.docassurance.client.api.DocAssuranceService;
import com.adobe.fd.docassurance.client.api.DocAssuranceServiceOperationTypes;
import com.adobe.fd.docassurance.client.api.ReaderExtensionOptions;
import com.adobe.fd.docassurance.client.api.SignatureOptions;
import com.adobe.fd.forms.api.AcrobatVersion;
import com.adobe.fd.forms.api.FormsService;
import com.adobe.fd.forms.api.FormsServiceException;
import com.adobe.fd.forms.api.PDFFormRenderOptions;
import com.adobe.fd.output.api.OutputService;
import com.adobe.fd.output.api.OutputServiceException;
import com.adobe.fd.output.api.PDFOutputOptions;
import com.adobe.fd.readerextensions.client.ReaderExtensionsOptionSpec;
import com.adobe.fd.readerextensions.client.UsageRights;
import com.adobe.fd.signatures.client.types.MDPPermissions;
import com.adobe.fd.signatures.pdf.inputs.CredentialContext;
import com.adobe.fd.signatures.pdf.inputs.DSSPreferences;
import com.adobe.fd.signatures.pdf.inputs.DSSPreferencesImpl;
import com.adobe.fd.signatures.pdf.inputs.PDFSignatureAppearenceOptions;
import com.adobe.fd.signatures.pdf.inputs.UnlockOptions;
import com.adobe.fd.signatures.pki.client.types.common.HashAlgorithm;
import com.adobe.fd.signatures.pki.client.types.common.RevocationCheckStyle;
import com.adobe.fd.signatures.pki.client.types.prefs.CRLPreferences;
import com.adobe.fd.signatures.pki.client.types.prefs.CRLPreferencesImpl;
import com.adobe.fd.signatures.pki.client.types.prefs.GeneralPreferencesImpl;
import com.adobe.fd.signatures.pki.client.types.prefs.PKIPreferences;
import com.adobe.fd.signatures.pki.client.types.prefs.PKIPreferencesImpl;
import com.adobe.fd.signatures.pki.client.types.prefs.PathValidationPreferences;
import com.adobe.fd.signatures.pki.client.types.prefs.PathValidationPreferencesImpl;
import com.adobe.pdfg.exception.ConversionException;
import com.adobe.pdfg.exception.FileFormatNotSupportedException;
import com.adobe.pdfg.exception.InvalidParameterException;
import com.adobe.pdfg.result.CreatePDFResult;
import com.adobe.pdfg.service.api.GeneratePDFService;
import com.aemformssamples.configuration.DocSvcConfiguration;
import com.aemformssamples.documentservices.core.DocumentServices;
import com.mergeandfuse.getserviceuserresolver.GetResolver;

@Component(service = { DocumentServices.class })
@Designate(ocd = DocSvcConfiguration.class)
public class DocumentServicesImpl implements DocumentServices {
	@Reference
	FormsService formsService;
	@Reference
	OutputService outputService;

	@Reference
	BarcodedFormsService barcodeService;
	@Reference
	DocAssuranceService docAssuranceService;
	@Reference
	AssemblerService assemblerService;
	@Reference
	private SlingRepository slingRepository;
	@Reference
	private GetResolver getResolver;
	@Reference
	GeneratePDFService generatePdfService;
	private static final Logger log = LoggerFactory.getLogger(DocumentServicesImpl.class);
	private DocSvcConfiguration docConfig;
	@Reference
	private ResourceResolverFactory jcrResourceResolverFactory;
	@Reference
	private DoRService dorService;
	String credentialAlias;
	String fieldName;

	@Activate
	public void activate(DocSvcConfiguration config) {
		docConfig = config;
	}

	public Document GenerateDor(String afPath, String dataXml) {
		Resource afResource = getResolver.getFormsServiceResolver().getResource(afPath);
		DoROptions dorOptions = new DoROptions();
		dorOptions.setData(dataXml);
		dorOptions.setFormResource(afResource);
		java.util.Locale locale = new java.util.Locale("en");
		dorOptions.setLocale(locale);
		com.adobe.aemds.guide.addon.dor.DoRResult dorResult = null;
		try {
			dorResult = dorService.render(dorOptions);
		} catch (DoRGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] fileBytes = dorResult.getContent();
		com.adobe.aemfd.docmanager.Document dorDocument = new com.adobe.aemfd.docmanager.Document(fileBytes);
		return dorDocument;

	}

	public InputStream certifyDocument(com.adobe.aemfd.docmanager.Document documentToCertify, String credentialAlias,
			String fieldName) {
		this.credentialAlias = credentialAlias;
		this.fieldName = fieldName;
		Session adminSession = null;
		ResourceResolver resourceResolver = null;
		com.adobe.aemfd.docmanager.Document certifiedDocument = null;

		try {
			try {
				adminSession = slingRepository.loginAdministrative(null);
			} catch (javax.jcr.LoginException e1) {
				e1.printStackTrace();
			} catch (RepositoryException e1) {
				e1.printStackTrace();
			}
			try {
				resourceResolver = jcrResourceResolverFactory.getAdministrativeResourceResolver(null);
			} catch (org.apache.sling.api.resource.LoginException e1) {
				e1.printStackTrace();
			}
			try {
				certifiedDocument = docAssuranceService.secureDocument(documentToCertify, null,
						getCertificationOptions(resourceResolver), null, null);
				log.info("Certified the document");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} finally {

			if (documentToCertify != null) {
				try {
					documentToCertify.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if ((adminSession != null) && (adminSession.isLive())) {
				if (resourceResolver != null) {
					resourceResolver.close();
				}
				adminSession.logout();
			}
		}
		try {
			return certifiedDocument.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private SignatureOptions getCertificationOptions(ResourceResolver rr) {
		log.info("Creating Signature Options");

		SignatureOptions signatureOptions = SignatureOptions.getInstance();

		signatureOptions.setOperationType(DocAssuranceServiceOperationTypes.CERTIFY);

		String fieldName = this.fieldName;

		String alias = credentialAlias;

		HashAlgorithm algo = HashAlgorithm.SHA256;

		String reason = "Reason";

		String location = "Location";

		String contactInfo = "Contact Info";

		MDPPermissions mdpPermissions = MDPPermissions.valueOf("FormChanges");

		PDFSignatureAppearenceOptions appOptions = new PDFSignatureAppearenceOptions(
				PDFSignatureAppearenceOptions.PDFSignatureAppearanceType.NAME, null, 1.0D, null, true, true, true, true,
				false, true, true, PDFSignatureAppearenceOptions.TextDirection.AUTO);

		signatureOptions.setLockCertifyingField(true);
		signatureOptions.setSignatureFieldName(fieldName);
		signatureOptions.setAlgo(algo);
		signatureOptions.setContactInfo(contactInfo);
		signatureOptions.setLocation(location);
		signatureOptions.setSigAppearence(appOptions);
		signatureOptions.setReason(reason);
		signatureOptions.setDssPref(getDSSPreferences(rr));
		signatureOptions.setCredential(new CredentialContext(alias, rr));
		signatureOptions.setMdpPermissions(mdpPermissions);
		log.info("Returing Signature Options");
		return signatureOptions;
	}

	private DSSPreferences getDSSPreferences(ResourceResolver rr) {
		log.info("Returing DSSS Options");
		DSSPreferencesImpl prefs = DSSPreferencesImpl.getInstance();
		prefs.setPKIPreferences(getPKIPreferences());
		GeneralPreferencesImpl gp = (GeneralPreferencesImpl) prefs.getPKIPreferences().getGeneralPreferences();
		gp.setDisableCache(true);
		return prefs;
	}

	private PKIPreferences getPKIPreferences() {
		PKIPreferences pkiPref = new PKIPreferencesImpl();
		pkiPref.setCRLPreferences(getCRLPreferences());
		pkiPref.setPathPreferences(getPathValidationPreferences());
		log.info("Returing PKI Options");
		return pkiPref;
	}

	private CRLPreferences getCRLPreferences() {
		CRLPreferencesImpl crlPrefs = new CRLPreferencesImpl();
		crlPrefs.setRevocationCheck(RevocationCheckStyle.CheckIfAvailable);
		crlPrefs.setGoOnline(true);
		log.info("Returing CRL Options");
		return crlPrefs;
	}

	private PathValidationPreferences getPathValidationPreferences() {
		PathValidationPreferencesImpl pathPref = new PathValidationPreferencesImpl();
		pathPref.setDoValidation(false);
		return pathPref;
	}

	public com.adobe.aemfd.docmanager.Document assembleDocuments(Map<String, Object> mapOfDocuments,
			com.adobe.aemfd.docmanager.Document ddxDocument) {
		AssemblerOptionSpec aoSpec = new AssemblerOptionSpec();
		aoSpec.setFailOnError(true);
		AssemblerResult ar = null;
		try {
			ar = assemblerService.invoke(ddxDocument, mapOfDocuments, aoSpec);
		} catch (OperationException e) {
			e.printStackTrace();
		}
		return (com.adobe.aemfd.docmanager.Document) ar.getDocuments().get("GeneratedDocument.pdf");
	}

	public com.adobe.aemfd.docmanager.Document orgw3cDocumentToAEMFDDocument(org.w3c.dom.Document xmlDocument) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DOMSource source = new DOMSource(xmlDocument);
		log.debug("$$$$In orgW3CDocumentToAEMFDDocument method");
		StreamResult outputTarget = new StreamResult(outputStream);
		try {
			TransformerFactory.newInstance().newTransformer().transform(source, outputTarget);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		InputStream is1 = new ByteArrayInputStream(outputStream.toByteArray());
		com.adobe.aemfd.docmanager.Document xmlAEMFDDocument = new com.adobe.aemfd.docmanager.Document(is1);
		return xmlAEMFDDocument;
	}

	public org.w3c.dom.Document w3cDocumentFromStrng(String xmlString) {
		try {
			log.debug("Inside w3cDocumentFromString");
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlString));

			return db.parse(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public com.adobe.aemfd.docmanager.Document createSimpleDDX(Map<String, Object> mapOfDocuments) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		org.w3c.dom.Document ddx = null;
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			ddx = docBuilder.newDocument();
			Element rootElement = ddx.createElementNS("http://ns.adobe.com/DDX/1.0/", "DDX");
			ddx.appendChild(rootElement);
			Element pdfResult = ddx.createElement("PDF");
			pdfResult.setAttribute("result", "GeneratedDocument.pdf");
			rootElement.appendChild(pdfResult);
			for (String key : mapOfDocuments.keySet()) {
				log.debug(key + " " + mapOfDocuments.get(key));
				Element pdfSourceElement = ddx.createElement("PDF");
				pdfSourceElement.setAttribute("source", key);
				pdfResult.appendChild(pdfSourceElement);
			}
		} catch (ParserConfigurationException e) {
			Element pdfResult;
			e.printStackTrace();
		}

		return orgw3cDocumentToAEMFDDocument(ddx);
	}

	public com.adobe.aemfd.docmanager.Document createDDXFromMapOfDocuments(
			Map<String, com.adobe.aemfd.docmanager.Document> mapOfDocuments) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		org.w3c.dom.Document ddx = null;
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			ddx = docBuilder.newDocument();
			Element rootElement = ddx.createElementNS("http://ns.adobe.com/DDX/1.0/", "DDX");
			ddx.appendChild(rootElement);
			Element pdfResult = ddx.createElement("PDF");
			pdfResult.setAttribute("result", "GeneratedDocument.pdf");
			rootElement.appendChild(pdfResult);
			for (String key : mapOfDocuments.keySet()) {
				log.debug(key + " " + mapOfDocuments.get(key));
				Element pdfSourceElement = ddx.createElement("PDF");
				pdfSourceElement.setAttribute("source", key);
				pdfResult.appendChild(pdfSourceElement);
			}
		} catch (ParserConfigurationException e) {
			Element pdfResult;
			e.printStackTrace();
		}

		return orgw3cDocumentToAEMFDDocument(ddx);
	}

	public com.adobe.aemfd.docmanager.Document renderAndExtendXdp(String xdpPath) {
		log.debug("In renderAndExtend xdp the alias is " + docConfig.ReaderExtensionAlias());
		PDFFormRenderOptions renderOptions = new PDFFormRenderOptions();
		renderOptions.setAcrobatVersion(AcrobatVersion.Acrobat_11);
		try {
			com.adobe.aemfd.docmanager.Document xdpRenderedAsPDF = formsService.renderPDFForm("crx://" + xdpPath, null,
					renderOptions);
			UsageRights usageRights = new UsageRights();
			usageRights.setEnabledBarcodeDecoding(docConfig.BarcodeDecoding());
			usageRights.setEnabledFormFillIn(docConfig.FormFill());
			usageRights.setEnabledComments(docConfig.Commenting());
			usageRights.setEnabledEmbeddedFiles(docConfig.EmbeddingFiles());
			usageRights.setEnabledDigitalSignatures(docConfig.DigitialSignatures());
			usageRights.setEnabledFormDataImportExport(docConfig.FormDataExportImport());
			ReaderExtensionsOptionSpec reOptionsSpec = new ReaderExtensionsOptionSpec(usageRights, "Sample ARES");
			UnlockOptions unlockOptions = null;
			ReaderExtensionOptions reOptions = ReaderExtensionOptions.getInstance();
			reOptions.setCredentialAlias(docConfig.ReaderExtensionAlias());
			log.debug("set the credential");
			reOptions.setResourceResolver(getResolver.getFormsServiceResolver());

			reOptions.setReOptions(reOptionsSpec);
			log.debug("set the resourceResolver and re spec");
			return docAssuranceService.secureDocument(xdpRenderedAsPDF, null, null, reOptions, unlockOptions);

		} catch (FormsServiceException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	String convertToString(org.w3c.dom.Document inputDoc) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer tr = tf.newTransformer();
		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);
		tr.transform(new DOMSource(inputDoc), sr);
		return sw.toString();
	}

	public JSONObject extractBarCode(com.adobe.aemfd.docmanager.Document pdfDocument) {
		try {
			org.w3c.dom.Document result = barcodeService.decode(pdfDocument, Boolean.valueOf(true),
					Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false),
					Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), CharSet.UTF_8);
			List<org.w3c.dom.Document> listResult = barcodeService.extractToXML(result, Delimiter.Carriage_Return,
					Delimiter.Tab, XMLFormat.XDP);

			log.debug("the form1 lenght is "
					+ ((org.w3c.dom.Document) listResult.get(0)).getElementsByTagName("form1").getLength());
			JSONObject decodedData = new JSONObject();
			decodedData.put("name",
					((org.w3c.dom.Document) listResult.get(0)).getElementsByTagName("Name").item(0).getTextContent());
			decodedData.put("address", ((org.w3c.dom.Document) listResult.get(0)).getElementsByTagName("Address")
					.item(0).getTextContent());
			decodedData.put("city",
					((org.w3c.dom.Document) listResult.get(0)).getElementsByTagName("City").item(0).getTextContent());
			decodedData.put("state",
					((org.w3c.dom.Document) listResult.get(0)).getElementsByTagName("State").item(0).getTextContent());
			decodedData.put("zipCode", ((org.w3c.dom.Document) listResult.get(0)).getElementsByTagName("ZipCode")
					.item(0).getTextContent());
			decodedData.put("country", ((org.w3c.dom.Document) listResult.get(0)).getElementsByTagName("Country")
					.item(0).getTextContent());
			log.debug("The JSON Object is " + decodedData.toString());
			return decodedData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String ocrDocument(String jcrPath, String fileName) throws ConversionException, InvalidParameterException,
			FileFormatNotSupportedException, ItemExistsException, PathNotFoundException, NoSuchNodeTypeException,
			LockException, VersionException, ConstraintViolationException, RepositoryException, IOException {
		ResourceResolver serviceResolver = getResolver.getFormsServiceResolver();
		System.out.println("the jcr path i got was" + jcrPath);
		javax.jcr.Node pdfDoc = (javax.jcr.Node) serviceResolver.getResource(jcrPath).adaptTo(javax.jcr.Node.class);
		System.out.println("Got tiffFile Node" + pdfDoc.getPath());
		com.adobe.aemfd.docmanager.Document tiffDocument = null;
		javax.jcr.Node jcrContent = pdfDoc.getNode("jcr:content");
		System.out.println("The jcr content node path is " + jcrContent.getPath());
		InputStream is = jcrContent.getProperty("jcr:data").getBinary().getStream();
		System.out.println("The is size is " + is.available());

		try {
			tiffDocument = new com.adobe.aemfd.docmanager.Document(is);
			System.out.println("Got tiff Document " + tiffDocument.getContentType());
		} catch (IOException e) {
			e.printStackTrace();
		}
		CreatePDFResult createPdfResult = generatePdfService.createPDF2(tiffDocument, fileName, "Standard OCR",
				"Standard", "No Security", null, null);

		com.adobe.aemfd.docmanager.Document ocrDocument = createPdfResult.getCreatedDocument();
		javax.jcr.Node ocrFiles = (javax.jcr.Node) serviceResolver.getResource("/content/ocrfiles")
				.adaptTo(javax.jcr.Node.class);
		System.out.println("The jcrNode name is " + ocrFiles.getName());
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		javax.jcr.Node ocrFile = ocrFiles.addNode(uuidString + ".pdf", "nt:file");
		System.out.println("The ocrFiles node was created");
		javax.jcr.Node resNode = ocrFile.addNode("jcr:content", "nt:resource");
		Session session = (Session) serviceResolver.adaptTo(Session.class);
		ValueFactory valueFactory = session.getValueFactory();

		Binary contentValue = valueFactory.createBinary(ocrDocument.getInputStream());

		resNode.setProperty("jcr:data", contentValue);
		serviceResolver.commit();

		return ocrFile.getPath();
	}

	public JSONObject ocrScannedImage(com.adobe.aemfd.docmanager.Document scannedImage, String fileName)
			throws org.apache.sling.api.resource.PersistenceException, ConversionException, InvalidParameterException,
			FileFormatNotSupportedException, ItemExistsException, PathNotFoundException, NoSuchNodeTypeException,
			LockException, VersionException, ConstraintViolationException, RepositoryException {
		ResourceResolver serviceResolver = getResolver.getFormsServiceResolver();
		CreatePDFResult createPdfResult = generatePdfService.createPDF2(scannedImage, fileName, "Standard OCR",
				"Standard", "No Security", null, null);

		com.adobe.aemfd.docmanager.Document ocrDocument = createPdfResult.getCreatedDocument();
		javax.jcr.Node ocrFiles = (javax.jcr.Node) serviceResolver.getResource("/content/ocrfiles")
				.adaptTo(javax.jcr.Node.class);
		System.out.println("The jcrNode name is " + ocrFiles.getName());
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		javax.jcr.Node ocrFile = ocrFiles.addNode(uuidString + ".pdf", "nt:file");
		System.out.println("The ocrFiles node was created");
		javax.jcr.Node resNode = ocrFile.addNode("jcr:content", "nt:resource");
		Session session = (Session) serviceResolver.adaptTo(Session.class);
		ValueFactory valueFactory = session.getValueFactory();

		try {
			Binary contentValue = valueFactory.createBinary(ocrDocument.getInputStream());
			resNode.setProperty("jcr:data", contentValue);
			serviceResolver.commit();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("path", ocrFile.getPath());
			return jsonObject;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String saveDocumentInCrx(String jcrPath, Document documentToSave) {
		// TODO Auto-generated method stub
		ResourceResolver serviceResolver = getResolver.getFormsServiceResolver();
		javax.jcr.Node ocrFiles = (javax.jcr.Node) serviceResolver.getResource(jcrPath).adaptTo(javax.jcr.Node.class);

		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		javax.jcr.Node assembledPDF;
		try {
			assembledPDF = ocrFiles.addNode(uuidString + ".pdf", "nt:file");
			javax.jcr.Node resNode = assembledPDF.addNode("jcr:content", "nt:resource");
			Session session = (Session) serviceResolver.adaptTo(Session.class);
			ValueFactory valueFactory = session.getValueFactory();

			Binary contentValue = valueFactory.createBinary(documentToSave.getInputStream());

			resNode.setProperty("jcr:data", contentValue);
			serviceResolver.commit();

			return assembledPDF.getPath();

		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("The ocrFiles node was created");
		return null;

	}

	@Override
	public Document mobileFormToInteractivePdf(Document xmlData) {
		System.out.println("####In mobile form to interactive pdf####");
		String uri = "crx:///content/dam/formsanddocuments";
		PDFFormRenderOptions renderOptions = new PDFFormRenderOptions();
		renderOptions.setAcrobatVersion(AcrobatVersion.Acrobat_11);
		renderOptions.setContentRoot(uri);
		Document interactivePDF = null;
		try {
			interactivePDF = formsService.renderPDFForm("schengen.xdp", xmlData, renderOptions);
		} catch (FormsServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return interactivePDF;

	}

	@Override
	public Document mobileFormToPDF(String xmlData) {
		// TODO Auto-generated method stub
		org.w3c.dom.Document xmlDataDoc = w3cDocumentFromStrng(xmlData);
		Node base64Image = xmlDataDoc.getElementsByTagName("base64image").item(0);
		if (base64Image.getTextContent() != "") {
			int startingPosition = base64Image.getTextContent().indexOf(",");
			System.out.println("The starting position is " + startingPosition);
			Node imageNode = xmlDataDoc.getElementsByTagName("img").item(0);
			log.debug("The massaged string is " + base64Image.getTextContent().substring(23));
			imageNode.setTextContent(base64Image.getTextContent().substring(startingPosition + 1));
		}
		Document xmlDataDocument = orgw3cDocumentToAEMFDDocument(xmlDataDoc);
		ResourceResolver serviceResolver = getResolver.getFormsServiceResolver();
		javax.jcr.Node xdpTemplateNode = (javax.jcr.Node) serviceResolver
				.getResource("/content/dam/formsanddocuments/schengen.xdp/jcr:content/renditions/original/jcr:content")
				.adaptTo(javax.jcr.Node.class);
		log.debug("Got the xdp node ");
		PDFOutputOptions pdfOptions = new PDFOutputOptions();
		pdfOptions.setAcrobatVersion(com.adobe.fd.output.api.AcrobatVersion.Acrobat_11);
		com.adobe.aemfd.docmanager.Document xdpTemplate = null;
		try {
			InputStream is = xdpTemplateNode.getProperty("jcr:data").getBinary().getStream();
			xdpTemplate = new com.adobe.aemfd.docmanager.Document(is);

			com.adobe.aemfd.docmanager.Document generatedPDF = outputService.generatePDFOutput(xdpTemplate,
					xmlDataDocument, pdfOptions);
			return generatedPDF;
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (OutputServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Document createDDXForFragments(String masterTemplate, Map<Integer, Map> fragmentsToAssemble) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		org.w3c.dom.Document ddx = null;
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			ddx = docBuilder.newDocument();
			Element rootElement = ddx.createElementNS("http://ns.adobe.com/DDX/1.0/", "DDX");
			ddx.appendChild(rootElement);
			Element pdf = ddx.createElement("PDF");
			pdf.setAttribute("result", "rendered.pdf");
			rootElement.appendChild(pdf);
			Element xdpResult = ddx.createElement("XDP");
			// xdpResult.setAttribute("result", "resultxdp.xdp");
			pdf.appendChild(xdpResult);
			Element xdpSource = ddx.createElement("XDP");
			xdpSource.setAttribute("source", masterTemplate);
			xdpResult.appendChild(xdpSource);
			// pdf.appendChild(xdpSource);
			for (Map.Entry<Integer, Map> map : fragmentsToAssemble.entrySet()) {
				System.out.println(map.getKey() + " = " + map.getValue().get("source"));
				Element xdpContent = ddx.createElement("XDPContent");
				xdpContent.setAttribute("insertionPoint", (String) map.getValue().get("insertionPoint"));
				xdpContent.setAttribute("source", (String) map.getValue().get("source"));
				xdpContent.setAttribute("fragment", (String) map.getValue().get("fragment"));
				xdpSource.appendChild(xdpContent);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		DOMSource source = new DOMSource(ddx);
		FileWriter writer;
		try {
			writer = new FileWriter(new File("c:\\scrap\\assemblefragments.xml"));
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			return orgw3cDocumentToAEMFDDocument(ddx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String insertFragments(String masterTemplate, Map<Integer, Map> fragmentsToAssemble, Document ddx) {
		System.out.println("The master template is " + masterTemplate);
		ResourceResolver serviceResolver = getResolver.getFormsServiceResolver();
		javax.jcr.Node masterXdp = getResolver.getFormsServiceResolver()
				.getResource("/content/dam/fidelitydemo/templates/" + masterTemplate
						+ "/jcr:content/renditions/original/jcr:content")
				.adaptTo(javax.jcr.Node.class);

		Map<String, Object> mapOfDocuments = new HashMap<String, Object>();
		try {
			InputStream masterXdpIS = masterXdp.getProperty("jcr:data").getBinary().getStream();
			Document masterXdpTemplate = new Document(masterXdpIS);
			mapOfDocuments.put(masterTemplate, masterXdpTemplate);
			for (Map.Entry<Integer, Map> map : fragmentsToAssemble.entrySet()) {
				String fragmentName = (String) map.getValue().get("source");
				System.out.println("The fragment name is " + fragmentName);
				String fragmentPath = "/content/dam/fidelitydemo/fragments/" + fragmentName
						+ "/jcr:content/renditions/original/jcr:content";
				System.out.println(map.getKey() + " = " + map.getValue().get("source"));
				javax.jcr.Node fragment = getResolver.getFormsServiceResolver().getResource(fragmentPath)
						.adaptTo(javax.jcr.Node.class);
				InputStream fragmentis = fragment.getProperty("jcr:data").getBinary().getStream();
				Document fragmentToInclude = new Document(fragmentis);
				System.out.println("$$$$ Added document " + fragmentName + " to the map");
				mapOfDocuments.put(fragmentName, fragmentToInclude);
				fragmentName = null;

			}
			for (Map.Entry<String, Object> map : mapOfDocuments.entrySet()) {
				System.out.println("The keys in the custom map are " + map.getKey());

			}
			AssemblerOptionSpec aoSpec = new AssemblerOptionSpec();
			aoSpec.setFailOnError(true);
			AssemblerResult ar = null;

			ar = assemblerService.invoke(ddx, mapOfDocuments, aoSpec);
			Document pdfRendered = ar.getDocuments().get("rendered.pdf");
			javax.jcr.Node ocrFiles = (javax.jcr.Node) serviceResolver.getResource("/content/ocrfiles")
					.adaptTo(javax.jcr.Node.class);
			System.out.println("The jcrNode name is " + ocrFiles.getName());
			UUID uuid = UUID.randomUUID();
			String uuidString = uuid.toString();
			javax.jcr.Node ocrFile = ocrFiles.addNode(uuidString + ".pdf", "nt:file");
			System.out.println("The ocrFiles node was created");
			javax.jcr.Node resNode = ocrFile.addNode("jcr:content", "nt:resource");
			Session session = (Session) serviceResolver.adaptTo(Session.class);
			ValueFactory valueFactory = session.getValueFactory();

			Binary contentValue = valueFactory.createBinary(pdfRendered.getInputStream());

			resNode.setProperty("jcr:data", contentValue);
			serviceResolver.commit();
			File f1 = new File("c:\\scrapp\\pdfRendered.pdf");
			pdfRendered.copyToFile(f1);

			System.out.println("Assembled fragments succesfully");

			return ocrFile.getPath();

		} catch (ValueFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
