package org.matheclipse.ease.lang.symja;

import java.lang.reflect.Method;

import org.eclipse.ease.AbstractCodeFactory;
import org.eclipse.ease.modules.IEnvironment;

public class SymjaCodeFactory  extends AbstractCodeFactory {
	
	@Override
	public String classInstantiation(Class<?> clazz, String[] parameters) {
		return null;
	}

	@Override
	public String getSaveVariableName(String variableName) {
		return null;
	}

	@Override
	protected String createFunctionWrapper(IEnvironment environment, String identifier, Method method) {
		return "";
	}

	@Override
	protected Object getLanguageIdentifier() {
		return null;
	}

	@Override
	protected String getNullString() {
		return "Null";
	}

	@Override
	protected String toSafeName(String name) {
		return null;
	}

}
