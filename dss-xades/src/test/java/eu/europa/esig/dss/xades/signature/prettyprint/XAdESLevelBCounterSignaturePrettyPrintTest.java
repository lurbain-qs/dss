package eu.europa.esig.dss.xades.signature.prettyprint;

import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESCounterSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESLevelBCounterSignatureTest;

public class XAdESLevelBCounterSignaturePrettyPrintTest extends XAdESLevelBCounterSignatureTest {

	@Override
	protected XAdESSignatureParameters getSignatureParameters() {
		XAdESSignatureParameters signatureParameters = super.getSignatureParameters();
		signatureParameters.setPrettyPrint(true);
		return signatureParameters;
	}

	@Override
	protected XAdESCounterSignatureParameters getCounterSignatureParameters() {
		XAdESCounterSignatureParameters signatureParameters = super.getCounterSignatureParameters();
		signatureParameters.setPrettyPrint(true);
		return signatureParameters;
	}

}
