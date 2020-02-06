package org.eclipse.ease.lang.symja;

import java.net.URL;
import java.util.Map;

import org.eclipse.ease.AbstractScriptEngine;
import org.eclipse.ease.Script;
import org.matheclipse.core.eval.ExprEvaluator;

public class SymjaEngine extends AbstractScriptEngine {

	private ExprEvaluator fInterpreter = null;

	public SymjaEngine() {
		super("Symja");
	}

	@Override
	protected void setupEngine() {
		fInterpreter = new ExprEvaluator(false, 100);

//  fInterpreter.setOut(getOutputStream());
//  fInterpreter.setErr(getErrorStream());
	}

	@Override
	protected void teardownEngine() {
		fInterpreter = null;

	}

	@Override
	protected Object execute(final Script script, final Object reference, final String fileName, final boolean uiThread)
			throws Throwable {
		return fInterpreter.eval(script.getCode());
	}

	@Override
	public void registerJar(URL url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminateCurrent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object internalGetVariable(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> internalGetVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean internalHasVariable(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void internalSetVariable(String name, Object content) {
		// TODO Auto-generated method stub

	}
}