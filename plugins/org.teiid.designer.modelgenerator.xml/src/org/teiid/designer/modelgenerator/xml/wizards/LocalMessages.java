/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.modelgenerator.xml.wizards;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @since 8.0
 */
public class LocalMessages {
	
	private static final String I18N_NAME = "org.teiid.designer.modelgenerator.xml.i18n"; //$NON-NLS-1$

	//CHECKSTYLE:OFF
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(I18N_NAME);
	//CHECKSTYLE:ON

	private LocalMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
