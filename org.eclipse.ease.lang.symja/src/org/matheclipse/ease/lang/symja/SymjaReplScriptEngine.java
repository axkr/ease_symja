package org.matheclipse.ease.lang.symja;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IFile;
import org.eclipse.ease.AbstractReplScriptEngine;
import org.eclipse.ease.Script;
import org.eclipse.ease.ScriptEngineException;
import org.eclipse.ease.debugging.model.EaseDebugVariable;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalControlledCallable;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.exception.AbortException;
import org.matheclipse.core.eval.exception.FailedException;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.Documentation;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.Scanner;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

public class SymjaReplScriptEngine extends AbstractReplScriptEngine {

	@Override
	public Collection<EaseDebugVariable> getDefinedVariables() {
		return new ArrayList<EaseDebugVariable>();
	}

	/**
	 * No timeout limit as the default value for Symja expression evaluation.
	 */
	private long fSeconds = -1;

	private final static int OUTPUTFORM = 0;

	private final static int JAVAFORM = 1;

	private final static int TRADITIONALFORM = 2;

//	private final static int PRETTYFORM = 3;

	private final static int INPUTFORM = 4;

	private int fUsedForm = OUTPUTFORM;

	private ExprEvaluator fEvaluator;

	private OutputFormFactory fOutputFactory;

	private OutputFormFactory fOutputTraditionalFactory;

	private OutputFormFactory fInputFactory;

	private static int counter = 1;

	PrintStream stdout;

	PrintStream stderr;

	public SymjaReplScriptEngine() {
		super("Symja");
	}

	@Override
	public void registerJar(URL url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminateCurrent() {
		fEvaluator = null;
	}

	@Override
	protected Object execute(Script script, Object reference, String fileName, boolean uiThread) throws Throwable {

		String inputExpression = null;
		if ((fileName != null) && (!fileName.isEmpty())) {
			final Object file = script.getFile();
			File f = null;
			if (file instanceof IFile) {
				f = ((IFile) file).getLocation().toFile();
			} else if (file instanceof File) {
				f = ((File) file);

			}
			if (f != null) {
				final String absolutePath = f.getAbsolutePath();
//				inputExpression = "Get(\"" + absolutePath + "\")";
				IExpr result = fEvaluator.eval(F.Get(F.stringx(absolutePath)));
				if (result != null) {
					stdout.println(printOutputForm(result));
					return printResult(result);
				}
				return "";
			}
		} else {
			inputExpression = script.getCode();
		}

		if (inputExpression != null && !inputExpression.startsWith("// use help")) {
			String trimmedInput = inputExpression.trim();
			if (trimmedInput.length() >= 4 && trimmedInput.charAt(0) == '/') {
				String command = trimmedInput.substring(1).toLowerCase(Locale.ENGLISH);
				if (command.equals("java")) {
					stdout.println("Enabling output for JavaForm");
					fUsedForm = JAVAFORM;
					return "";
				} else if (command.equals("traditional")) {
					stdout.println("Enabling output for TraditionalForm");
					fUsedForm = TRADITIONALFORM;
					return "";
				} else if (command.equals("output")) {
					stdout.println("Enabling output for OutputForm");
					fUsedForm = OUTPUTFORM;
					return "";
//				} else if (command.equals("pretty")) {
//					stdout.println("Enabling output for PrettyPrinterForm");
//					fUsedForm = PRETTYFORM;
//					return "";
				} else if (command.equals("input")) {
					stdout.println("Enabling output for InputForm");
					fUsedForm = INPUTFORM;
					return "";
				} else if (command.equals("timeoutoff")) {
					stdout.println("Disabling timeout for evaluation");
					fSeconds = -1;
					return "";
				} else if (command.equals("timeouton")) {
					stdout.println("Enabling timeout for evaluation to 60 seconds.");
					fSeconds = 60;
					return "";
				} else if (command.equals("help")) {
					printUsage();
					return "";
				}
			}
			String postfix = Scanner.balanceCode(trimmedInput);
			if (postfix != null && postfix.length() > 0) {
				stderr.println("Automatically closing brackets: " + postfix);
				trimmedInput = trimmedInput + postfix;
			}
			stdout.println("In [" + counter + "]: " + trimmedInput);
			stdout.flush();
			// if (console.fPrettyPrinter) {
			// console.prettyPrinter(inputExpression);
			// } else {
			String result = resultPrinter(trimmedInput);
			// }
			counter++;
			return result;
		}
		return "";// fEvaluator.eval(inputExpression);

	}

	private String resultPrinter(String inputExpression) {
		String outputExpression = interpreter(inputExpression);
		if (outputExpression.length() > 0) {
			stdout.println("Out[" + counter + "]: " + outputExpression);
			stdout.flush();
		}
		return outputExpression;
	}

	/**
	 * Evaluates the given string-expression and returns the result in
	 * <code>OutputForm</code>
	 * 
	 * @param trimmedInput a trimmed input string
	 * @return
	 */
	/* package private */ String interpreter(final String trimmedInput) {
		IExpr result;
		final StringWriter buf = new StringWriter();
		try {
			if (trimmedInput.length() > 1 && trimmedInput.charAt(0) == '?') {
				IExpr doc = Documentation.findDocumentation(trimmedInput);
				return printResult(doc);
			}
			if (fSeconds <= 0) {
				result = fEvaluator.eval(trimmedInput);
			} else {
				result = fEvaluator.evaluateWithTimeout(trimmedInput, fSeconds, TimeUnit.SECONDS, true,
						new EvalControlledCallable(fEvaluator.getEvalEngine()));
			}
			if (result != null) {
				return printResult(result);
			}
		} catch (final AbortException re) {
			try {
				return printResult(F.$Aborted);
			} catch (IOException e) {
				Validate.printException(buf, e);
				stderr.println(buf.toString());
				stderr.flush();
				return "";
			}
		} catch (final FailedException re) {
			try {
				return printResult(F.$Failed);
			} catch (IOException e) {
				Validate.printException(buf, e);
				stderr.println(buf.toString());
				stderr.flush();
				return "";
			}
		} catch (final SyntaxError se) {
			String msg = se.getMessage();
			stderr.println(msg);
			stderr.println();
			stderr.flush();
			return "";
		} catch (final RuntimeException re) {
			Throwable me = re.getCause();
			if (me instanceof MathException) {
				Validate.printException(buf, me);
			} else {
				Validate.printException(buf, re);
			}
			stderr.println(buf.toString());
			stderr.flush();
			return "";
		} catch (final IOException e) {
			Validate.printException(buf, e);
			stderr.println(buf.toString());
			stderr.flush();
			return "";
//		} catch (final Exception e) {
//			Validate.printException(buf, e);
//			stderr.println(buf.toString());
//			stderr.flush();
//			return "";
//		} catch (final OutOfMemoryError e) {
//			Validate.printException(buf, e);
//			stderr.println(buf.toString());
//			stderr.flush();
//			return "";
//		} catch (final StackOverflowError e) {
//			Validate.printException(buf, e);
//			stderr.println(buf.toString());
//			stderr.flush();
//			return "";
		}
		return buf.toString();
	}

	private String printResult(IExpr result) throws IOException {
		if (result.equals(F.Null)) {
			return "";
		}
		switch (fUsedForm) {
		case JAVAFORM:
			return result.internalJavaString(false, -1, false, true, false);
		case TRADITIONALFORM:
			StringBuilder traditionalBuffer = new StringBuilder();
			fOutputTraditionalFactory.reset();
			if (fOutputTraditionalFactory.convert(traditionalBuffer, result)) {
				return traditionalBuffer.toString();
			} else {
				return "ERROR-IN-TRADITIONALFORM";
			}
//		case PRETTYFORM:
//			ASCIIPrettyPrinter3 prettyBuffer = new ASCIIPrettyPrinter3();
//			prettyBuffer.convert(result);
//			stdout.println();
//			String[] outputExpression = prettyBuffer.toStringBuilder();
//			ASCIIPrettyPrinter3.prettyPrinter(stdout, outputExpression, "Out[" + counter + "]: ");
//			return "";
		case INPUTFORM:
			StringBuilder inputBuffer = new StringBuilder();
			fInputFactory.reset();
			if (fInputFactory.convert(inputBuffer, result)) {
				return inputBuffer.toString();
			} else {
				return "ERROR-IN-INPUTFORM";
			}
		default:
			return printOutputForm(result);
		}

	}

	private String printOutputForm(IExpr result) {
		if (Desktop.isDesktopSupported()) {
			IExpr outExpr = result;
			if (result.isAST(F.Graphics)) {// || result.isAST(F.Graphics3D)) {
				outExpr = F.Show(outExpr);
			}
			String html = F.show(outExpr);
			if (html != null) {
				return html;
			}
		}
		StringBuilder strBuffer = new StringBuilder();
		fOutputFactory.reset();
		if (fOutputFactory.convert(strBuffer, result)) {
			return strBuffer.toString();
		}
		return "ERROR-IN-OUTPUTFORM";
	}

	/**
	 * Prints the usage of how to use this class to stdout
	 */
	private void printUsage() {
		final String lineSeparator = System.getProperty("line.separator");
		final StringBuilder msg = new StringBuilder();
		msg.append(Config.SYMJA);
		msg.append("Get more information: /help<RETURN>" + lineSeparator);
		msg.append("To disable the evaluation timeout type: /timeoutoff<RETURN>" + lineSeparator);
		msg.append("To enable the evaluation timeout type: /timeouton<RETURN>" + lineSeparator);
		msg.append("To enable the output in Java form: /java<RETURN>" + lineSeparator);
		msg.append("To enable the output in standard form: /output<RETURN>" + lineSeparator);
		msg.append("To enable the output in standard form: /traditional<RETURN>" + lineSeparator);
		msg.append("****+****+****+****+****+****+****+****+****+****+****+****+");

		stdout.println(msg.toString());
		stdout.flush();
	}

	@Override
	protected Object internalGetVariable(String name) {
		return null;
	}

	@Override
	protected Map<String, Object> internalGetVariables() {
		return new HashMap<String, Object>();
	}

	@Override
	protected boolean internalHasVariable(String name) {
		return false;
	}

	@Override
	protected void internalSetVariable(String name, Object content) {
	}

	@Override
	protected void setupEngine() throws ScriptEngineException {
		SymjaPlugin.initialize();
		stdout = getOutputStream();
		stderr = getErrorStream();
		fEvaluator = new ExprEvaluator(false, 100);
		fOutputFactory = OutputFormFactory.get(true, false, 5, 7);
		EvalEngine evalEngine = fEvaluator.getEvalEngine();
		evalEngine.setFileSystemEnabled(true);
		evalEngine.setOutPrintStream(stdout);
		evalEngine.setErrorPrintStream(stderr);
		fOutputTraditionalFactory = OutputFormFactory.get(true, false, 5, 7);
		fInputFactory = OutputFormFactory.get(true, false, 5, 7);
		fInputFactory.setQuotes(true);
//		printUsage();
	}

}
