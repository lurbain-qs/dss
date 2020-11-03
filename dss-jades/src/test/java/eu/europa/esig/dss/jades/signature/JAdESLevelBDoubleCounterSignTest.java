package eu.europa.esig.dss.jades.signature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.JAdESTimestampParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.signature.CounterSignatureService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.AdvancedSignature;

public class JAdESLevelBDoubleCounterSignTest extends AbstractJAdESCounterSignatureTest {

	private JAdESService service;
	private DSSDocument documentToSign;

	private Date signingDate;

	@BeforeEach
	public void init() throws Exception {
		service = new JAdESService(getCompleteCertificateVerifier());
		service.setTspSource(getGoodTsa());
		documentToSign = new FileDocument(new File("src/test/resources/sample.json"));
		signingDate = new Date();
	}

	@Override
	protected JAdESSignatureParameters getSignatureParameters() {
		JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
		signatureParameters.bLevel().setSigningDate(signingDate);
		signatureParameters.setSigningCertificate(getSigningCert());
		signatureParameters.setCertificateChain(getCertificateChain());
		signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
		signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
		signatureParameters.setJwsSerializationType(JWSSerializationType.JSON_SERIALIZATION);
		return signatureParameters;
	}

	@Override
	protected JAdESCounterSignatureParameters getCounterSignatureParameters() {
		JAdESCounterSignatureParameters signatureParameters = new JAdESCounterSignatureParameters();
		signatureParameters.bLevel().setSigningDate(signingDate);
		signatureParameters.setSigningCertificate(getSigningCert());
		signatureParameters.setCertificateChain(getCertificateChain());
		signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
		return signatureParameters;
	}
	
	@Override
	protected DSSDocument counterSign(DSSDocument signatureDocument, String signatureId) {
		DSSDocument counterSigned = super.counterSign(signatureDocument, signatureId);
		return super.counterSign(counterSigned, signatureId);
	}
	
	@Override
	protected void checkAdvancedSignatures(List<AdvancedSignature> signatures) {
		super.checkAdvancedSignatures(signatures);
		assertEquals(1, signatures.size());
		
		AdvancedSignature advancedSignature = signatures.get(0);
		List<AdvancedSignature> counterSignatures = advancedSignature.getCounterSignatures();
		assertEquals(2, counterSignatures.size());
		
		assertNotEquals(counterSignatures.get(0).getId(), counterSignatures.get(1).getId());
	}
	
	@Override
	protected void checkNumberOfSignatures(DiagnosticData diagnosticData) {
		assertEquals(3, Utils.collectionSize(diagnosticData.getSignatureIdList()));
	}

	@Override
	protected DSSDocument getDocumentToSign() {
		return documentToSign;
	}

	@Override
	protected DocumentSignatureService<JAdESSignatureParameters, JAdESTimestampParameters> getService() {
		return service;
	}

	@Override
	protected CounterSignatureService<JAdESCounterSignatureParameters> getCounterSignatureService() {
		return service;
	}

	@Override
	protected String getSigningAlias() {
		return GOOD_USER;
	}

}