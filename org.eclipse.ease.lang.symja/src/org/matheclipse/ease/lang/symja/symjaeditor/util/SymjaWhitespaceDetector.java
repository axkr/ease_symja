/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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


import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * A Symja aware white space detector.
 */
public class SymjaWhitespaceDetector implements IWhitespaceDetector {

	@Override
	public boolean isWhitespace(char character) {
		return Character.isWhitespace(character);
	}
}
