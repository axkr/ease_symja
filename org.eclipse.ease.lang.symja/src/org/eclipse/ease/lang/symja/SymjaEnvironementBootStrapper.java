package org.eclipse.ease.lang.symja;

import org.eclipse.ease.IScriptEngine;
import org.eclipse.ease.IScriptEngineLaunchExtension;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.expression.F;

public class SymjaEnvironementBootStrapper  implements IScriptEngineLaunchExtension {
	@Override
	public void createEngine(final IScriptEngine engine) {
		// load environment module
//		engine.executeAsync("java_import org.eclipse.ease.modules.EnvironmentModule");
//		engine.executeAsync("org.eclipse.ease.modules.EnvironmentModule.new().loadModule(\"/System/Environment\")");
		initialize();
	}

	public static void initialize() {
		Config.PARSER_USE_LOWERCASE_SYMBOLS = true;
		Config.USE_VISJS = true;
		Config.FILESYSTEM_ENABLED = true;
		F.initSymbols(null, null, true);
	}
}
