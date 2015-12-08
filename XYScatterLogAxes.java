package Homeproblem3;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class XYScatterLogAxes extends JFrame {

	private static List<Double> coords1;
	private static List<Double> coords2;
	private LinearRegression firstFit;
	private LinearRegression secondFit;

	// Constructor
	public XYScatterLogAxes(List<Double> coords1, List<Double> coords2) {
		super(" ");

		this.coords1 = coords1;
		this.coords2 = coords2;

		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);

		setVisible(true);
		
	}

	private JPanel createChartPanel() {

		String chartTitle = "Forest fires";
		String xAxisLabel = "Relative fire size";
		String yAxisLabel = "cCDF";

		XYDataset dataset = createDataset();
		
		
		JFreeChart chart = ChartFactory.createScatterPlot(chartTitle, xAxisLabel, yAxisLabel, dataset);

		XYPlot plot = chart.getXYPlot();
		// XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true,
		// false);

		Shape cross = ShapeUtilities.createDiagonalCross(1, 1);
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesShape(1, cross);

		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		range.setNumberFormatOverride(NumberFormat.getNumberInstance());

		final NumberAxis domainAxis = new LogarithmicAxis(xAxisLabel);
		final NumberAxis rangeAxis = new LogarithmicAxis(yAxisLabel);
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(rangeAxis);

		// Setting background color for the plot:
		plot.setBackgroundPaint(Color.WHITE);

		// Setting visibility and paint color for the grid lines:
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);

		return new ChartPanel(chart);
	}

	private XYDataset createDataset() {

		XYSeriesCollection dataset = new XYSeriesCollection();

		final XYSeries s1 = new XYSeries("Original fire");
		final XYSeries s2 = new XYSeries("Recreated fire");

		Collections.sort(coords1);
		Collections.sort(coords2);
		Collections.reverse(coords1);
		Collections.reverse(coords2);

		for (int i = 1; i <= coords1.size(); i++) {
			double yVal = (double) i / coords1.size();
			double xVal = coords1.get(i - 1);
			s1.add(xVal, yVal);
		}

		for (int j = 1; j <= coords2.size(); j++) {
			double yVal = (double) j / coords2.size();
			double xVal = coords2.get(j - 1);
			s2.add(xVal, yVal);
		}

		dataset.addSeries(s1);
		dataset.addSeries(s2);
		
		firstFit = new LinearRegression(coords1);
		secondFit = new LinearRegression(coords2);

		return dataset;
	}

	public class LinearRegression { 
		

	    public LinearRegression(List<Double> StdIn) { 
	        int MAXN = StdIn.size();
	        int n = 0;
	        double[] x = new double[MAXN];
	        double[] y = new double[MAXN];

	        // first pass: read in data, compute xbar and ybar
	        double sumx = 0.0;
	        double sumy = 0.0; 
	        double sumx2 = 0.0;
	        while(n < StdIn.size() ) {
	            x[n] = (double) StdIn.get(n);
	            y[n] = (double) StdIn.get(n);
	            sumx  += (double) x[n];
	            sumx2 += (double) x[n] * x[n];
	            sumy  += (double) y[n];
	            n++;
	        }
	        double xbar = (double) sumx / n;
	        double ybar = (double) sumy / n;

	        // second pass: compute summary statistics
	        double xxbar = 0.0; 
	        double yybar = 0.0;
	        double xybar = 0.0;
	        for (int i = 0; i < n; i++) {
	            xxbar += (double) (x[i] - xbar) * (x[i] - xbar);
	            yybar += (double) (y[i] - ybar) * (y[i] - ybar);
	            xybar += (double) (x[i] - xbar) * (y[i] - ybar);
	        }
	        double beta1 = (double) xybar / xxbar;
	        double beta0 = (double) ybar - beta1 * xbar;

	        // print results
	        System.out.println("y   = " + beta1 + " * x + " + beta0);

	        // analyze results
	        int df = n - 2;
	        double rss = 0.0;      // residual sum of squares
	        double ssr = 0.0;      // regression sum of squares
	        for (int i = 0; i < n; i++) {
	            double fit = (double) beta1*x[i] + beta0;
	            rss += (double) (fit - y[i]) * (fit - y[i]);
	            ssr += (double) (fit - ybar) * (fit - ybar);
	        }
	        double R2    = (double) ssr / yybar;
	        double svar  = (double) rss / df;
	        double svar1 = (double) svar / xxbar;
	        double svar0 = (double) svar/n + xbar*xbar*svar1;
	        System.out.println("R^2                 = " + R2);
	        System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
	        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
	        svar0 = (double) svar * sumx2 / (n * xxbar);
	        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));

	        System.out.println("SSTO = " + yybar);
	        System.out.println("SSE  = " + rss);
	        System.out.println("SSR  = " + ssr);
	    }
	}
}
