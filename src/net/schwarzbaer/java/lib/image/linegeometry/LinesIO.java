package net.schwarzbaer.java.lib.image.linegeometry;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

	public static void writeForms(PrintWriter out, Form[] forms) { writeForms(out, forms, null); }
	public static void writeForms(PrintWriter out, Form[] forms, Consumer<String> addError)
	{
		writeForms(out, forms, ()->forms.length, i->forms[i], addError);
	}

	public static void writeForms(PrintWriter out, List<? extends Form> forms) { writeForms(out, forms, null); }
	public static void writeForms(PrintWriter out, List<? extends Form> forms, Consumer<String> addError)
	{
		writeForms(out, forms, forms::size, forms::get, addError);
	}
	
	private static void writeForms(PrintWriter out, Object formsList, Supplier<Integer> getSize, Function<Integer,Form> getForm, Consumer<String> addError)
	{
		if (formsList==null)
		{
			if (addError!=null) addError.accept("LinesIO.writeForms: Given list of forms is null.");
			return;
		}
		
		for (int i=0; i<getSize.get(); i++)
		{
			Form form = getForm.apply(i);
			if (form==null)
			{
				if (addError!=null) addError.accept(String.format("LinesIO.writeForms: Form[%d] is null.", i));
				continue;
			}
			
			double[] values = form.getValues();
			if (values==null)
			{
				if (addError!=null) addError.accept(String.format("LinesIO.writeForms: Form[%d].Values is null.", i));
				continue;
			}
			
			String valuesStr = String.join(";", Arrays.stream(values).mapToObj(d->Double.toString(d)).toArray(String[]::new));
			out.printf("%s=%s%n", getName(form), valuesStr);
		}
	}

	private static String getName(Form form)
	{
		if (form instanceof Form.PolyLine) return "PolyLine";
		if (form instanceof Form.Line    ) return "Line";
		if (form instanceof Form.Arc     ) return "Arc";
		throw new IllegalArgumentException();
	}
}
