/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.matheclipse.ease.lang.symja.symjaeditor.util;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * A Symja aware word detector.
 */
public class SymjaWordDetector implements IWordDetector {

	@Override
	public boolean isWordPart(char character) {
		return Character.isJavaIdentifierPart(character) && character != '_';
	}

	@Override
	public boolean isWordStart(char character) {
		return Character.isJavaIdentifierStart(character) && character != '_';
	}
}
