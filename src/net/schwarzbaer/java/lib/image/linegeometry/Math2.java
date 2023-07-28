package net.schwarzbaer.java.lib.image.linegeometry;

public class Math2 {
	public static final double FULL_CIRCLE = 2*Math.PI;
	
	private static void Assert(boolean condition) {
		if (!condition) throw new IllegalStateException();
	}

	public static double dist(double xC, double yC, double x, double y) {
		double localX = x-xC;
		double localY = y-yC;
		return Math.sqrt(localX*localX+localY*localY);
	}
	
	public static double angle(double xC, double yC, double x, double y) {
		double localX = x-xC;
		double localY = y-yC;
		return Math.atan2(localY, localX);
	}

	public static double normalizeAngle(double minW, double w) {
		double wDiff = w-minW;
		if (wDiff<0 || FULL_CIRCLE<wDiff) w -= Math.floor(wDiff/FULL_CIRCLE)*FULL_CIRCLE;
		Assert(minW<=w);
		Assert(w<=minW+FULL_CIRCLE);
		return w;
	}
	
	public static boolean isInsideAngleRange(double minW, double maxW, double w) {
		Assert(Double.isFinite(minW));
		Assert(Double.isFinite(maxW));
		Assert(minW<=maxW);
		
		w = normalizeAngle(minW,w);
		return w<=maxW;
	}
	
	public static double computeAngleDist(double a1, double a2) {
		// result: -Math.PI ... Math.PI
		a2 = normalizeAngle(a1, a2); // a2 in a1...a1+2*Math.PI
		if (a2>a1+Math.PI) return a2-a1-2*Math.PI;
		return a2-a1;
	}
}
