package org.eclipse.ease.lang.symja;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.core.filebuffers.IDocumentSetupParticipantExtension;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ease.lang.symja.symjaeditor.SymjaPartitionScanner;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.SyntaxError; 

public class ValidatorDocumentSetupParticipant
		implements IDocumentSetupParticipant, IDocumentSetupParticipantExtension {

	private final class DocumentValidator implements IDocumentListener {
		private final IFile file;
		private IMarker marker;

		private DocumentValidator(IFile file) {
			this.file = file;
		}

		@Override
		public void documentChanged(DocumentEvent event) {
			if (this.marker != null) {
				try {
					this.marker.delete();
				} catch (CoreException e) {
					e.printStackTrace();
				}
				this.marker = null;
			}
			try {
				String text = event.getDocument().get();
				if (text.length() > 0) {
					Parser parser = new Parser(true, true);
					parser.parsePackage(text);
				}
			} catch (Exception ex) {
				try {
					this.marker = file.createMarker(IMarker.PROBLEM);
					this.marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					this.marker.setAttribute(IMarker.MESSAGE, ex.getMessage());
					if (ex instanceof SyntaxError) {
						SyntaxError syntaxError = (SyntaxError) ex;
						int lineNumber = syntaxError.getRowIndex();
						int offset = event.getDocument().getLineInformation(lineNumber).getOffset()
								+ syntaxError.getColumnIndex();
						this.marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
						this.marker.setAttribute(IMarker.CHAR_START, offset);
						this.marker.setAttribute(IMarker.CHAR_END, offset + 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}
	}

	@Override
	public void setup(IDocument document) {
	}

	@Override
	public void setup(IDocument document, IPath location, LocationKind locationKind) {
		if (locationKind == LocationKind.IFILE) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(location);
			document.addDocumentListener(new DocumentValidator(file));
		}
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			IDocumentPartitioner partitioner= new FastPartitioner(SymjaPlugin.getDefault().getJavaPartitionScanner(), SymjaPartitionScanner.JAVA_PARTITION_TYPES);
			extension3.setDocumentPartitioner(SymjaPlugin.JAVA_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
	}

}
