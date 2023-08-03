package net.schwarzbaer.java.lib.image.linegeometry;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.function.Consumer;

import net.schwarzbaer.java.lib.image.linegeometry.Form.Factory;

public class LinesIO
{
	private final Factory formFactory;

	public LinesIO(Factory formFactory)
	{
		this.formFactory = formFactory != null
			? formFactory
			: new Form.Factory() {
				@Override public Form.PolyLine createPolyLine(double[] values) { return new Form.PolyLine().setValues(values); }
				@Override public Form.Line     createLine    (double[] values) { return new Form.Line    ().setValues(values); }
				@Override public Form.Arc      createArc     (double[] values) { return new Form.Arc     ().setValues(values); }
			};
	}

	public void parseFormLine(String line, Consumer<Form> addNewForm)
	{
		String value;
		if ( (value=getValue(line,"PolyLine="))!=null ) addNewForm.accept(formFactory.createPolyLine(toArray(value)));
		if ( (value=getValue(line,"Line="    ))!=null ) addNewForm.accept(formFactory.createLine    (toArray(value)));
		if ( (value=getValue(line,"Arc="     ))!=null ) addNewForm.accept(formFactory.createArc     (toArray(value)));
	}
	
	private static String getValue(String line, String prefix)
	{
		if (line.startsWith(prefix))
			return line.substring(prefix.length());
		return null;
	}
	
	private static double[] toArray(String str)
	{
		String[] valueStrs = str.split(";");
		double[] values = new double[valueStrs.length];
		for (int i=0; i<values.length; i++)
		{
			try { values[i] = Double.parseDouble(valueStrs[i]); }
			catch (NumberFormatException e) { values[i] = Double.NaN; }
			if (Double.isNaN(values[i])) {
				System.err.printf("Can't parse Double value (\"%s\") in String \"%s\" at position %d.%n", valueStrs[i], str, i);
				return null;
			}
		}
		return values;
	}

	public static void writeForms(PrintWriter out, Form[] forms)
	{
		for (Form form : forms)
			writeForm(out, form);
	}

	public static void writeForms(PrintWriter out, Iterable<? extends Form> forms)
	{
		for (Form form : forms)
			writeForm(out, form);
	}

	private static void writeForm(PrintWriter out, Form form)
	{
		double[] values = form.getValues();
		String valuesStr = String.join(";", Arrays.stream(values).mapToObj(d->Double.toString(d)).toArray(String[]::new));
		out.printf("%s=%s%n", getName(form), valuesStr);
	}

	private static String getName(Form form)
	{
		if (form instanceof Form.PolyLine) return "PolyLine";
		if (form instanceof Form.Line    ) return "Line";
		if (form instanceof Form.Arc     ) return "Arc";
		throw new IllegalArgumentException();
	}
}
