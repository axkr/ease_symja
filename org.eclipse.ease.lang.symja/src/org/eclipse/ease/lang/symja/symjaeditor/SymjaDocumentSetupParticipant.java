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
package org.eclipse.ease.lang.symja.symjaeditor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.ease.lang.symja.SymjaPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

/**
 *
 */
public class SymjaDocumentSetupParticipant implements IDocumentSetupParticipant {

	/**
	 */
	public SymjaDocumentSetupParticipant() {
	}

	@Override
	public void setup(IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			IDocumentPartitioner partitioner= new FastPartitioner(SymjaPlugin.getDefault().getJavaPartitionScanner(), SymjaPartitionScanner.JAVA_PARTITION_TYPES);
			extension3.setDocumentPartitioner(SymjaPlugin.JAVA_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
	}
}
