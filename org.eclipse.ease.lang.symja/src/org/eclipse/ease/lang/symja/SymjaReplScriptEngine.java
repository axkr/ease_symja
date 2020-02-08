package org.eclipse.ease.lang.symja;

import java.net.URL;
import java.util.Map;

import org.eclipse.ease.AbstractReplScriptEngine;
import org.eclipse.ease.Script;
import org.eclipse.ease.ScriptEngineException;
import org.matheclipse.core.eval.ExprEvaluator;

public class SymjaReplScriptEngine extends AbstractReplScriptEngine {

	private ExprEvaluator fInterpreter = null;

	public SymjaReplScriptEngine() {
		super("Symja");
	}
	
	@Override
	public void registerJar(URL url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminateCurrent() {
		fInterpreter = null;
	}

	@Override
	protected Object execute(Script script, Object reference, String fileName, boolean uiThread) throws Throwable {
		return fInterpreter.eval(script.getCode());
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

	@Override
	protected void setupEngine() throws ScriptEngineException {
		fInterpreter = new ExprEvaluator(false, 100);
	}

}
