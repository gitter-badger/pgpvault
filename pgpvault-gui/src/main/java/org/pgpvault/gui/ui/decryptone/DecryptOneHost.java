package org.pgpvault.gui.ui.decryptone;

import javax.swing.Action;

public interface DecryptOneHost {
	void handleClose();
	
	Action getActionToOpenCertificatesList();
}