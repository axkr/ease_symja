package org.matheclipse.ease.lang.symja;

import org.eclipse.ease.AbstractCodeParser;

public class SymjaCodeParser extends AbstractCodeParser {

	@Override
	protected String getBlockCommentEndToken() { 
		return "*)";
	}

	@Override
	protected String getBlockCommentStartToken() { 
		return "(*";
	}

	@Override
	protected String getLineCommentToken() {
		return null;
	}

	@Override
	protected boolean hasBlockComment() {
		return false;
	}

}
