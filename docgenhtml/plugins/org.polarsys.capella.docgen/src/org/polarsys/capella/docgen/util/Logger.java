/*******************************************************************************
 * Copyright (c) 2020, 2021 THALES GLOBAL SERVICES.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *  
 * Contributors:
 *    THALES GLOBAL SERVICES - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.docgen.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.polarsys.capella.docgen.Activator;

public class Logger {
	
	private Logger() {}
	
	public static void logInfo(String message, Exception exception) {
		log(IStatus.INFO, message, exception);
	}
	
	public static void logWarning(String message, Exception exception) {
		log(IStatus.WARNING, message, exception);
	}
	
	public static void logError(String message, Exception exception) {
		log(IStatus.ERROR, message, exception);
	}
	
	private static void log(int status, String message, Exception exception) {
		IStatus newStatus = new Status(status, Activator.PLUGIN_ID, message, exception);
		Activator.getDefault().getLog().log(newStatus);
	}

}
