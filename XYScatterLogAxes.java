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

	private double tau = 1.10;

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
		final XYSeries s3 = new XYSeries("Synthetic data");

		final XYSeries linear = new XYSeries("Hello");

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

		double xMin = 0.0001;
		double x_min = xMin;
		// (Double) coords1.get(coords1.size() - 1);
		List<List<Double>> powerData = generatePowerNumbers(x_min, tau, coords1.size());
		for (int j = 0; j < powerData.size(); j++) {
			List<Double> thisPoint = powerData.get(j);
			double yVal = thisPoint.get(1);
			double xVal = thisPoint.get(0);
			s3.add(xVal, yVal);
		}

		int nrOfDataPoints = 100;
		double xMax = 0.2;
		double minX = (double) 1 / 10000;
		double steplength = (xMax - xMin) / nrOfDataPoints;

		for (int i = 1; i <= nrOfDataPoints + 1; i++) {
			double xVal = (double) (xMin + (i - 1) * steplength);
			double yVal = getY(xVal, minX);
			linear.add(xVal, yVal);
		}

		/*
		 * secondFit = new LinearRegression(linearFit2);
		 * 
		 * /System.out.println("X=" + coords2.get(0)); System.out.println("Y=" +
		 * getY(coords2.get(0), slope, intersect));
		 * 
		 * System.out.println("X=" + coords2.get(coords2.size() / 2));
		 * System.out.println("Y=" + getY(coords2.get(coords2.size() / 2),
		 * slope, intersect));
		 * 
		 * 
		 * linear2.add((double) coords2.get(0), getY(coords2.get(0), slope,
		 * intersect)); linear2.add((double) coords2.get(coords2.size() / 2),
		 * getY(coords2.get(coords2.size() / 2), slope, intersect));
		 */
		dataset.addSeries(s1);
		dataset.addSeries(s2);
		dataset.addSeries(s3);
		dataset.addSeries(linear);
		return dataset;
	}

	public List<List<Double>> generatePowerNumbers(double x_min, double tau, int num) {
		List<List<Double>> powerNumbers = new ArrayList(num);
		for (int i = 0; i < num; i++) {
			double r = Math.random() * 0.6 + 0.0001;
			//double powerNumber = x_min * Math.pow(1 - r, (double) 1 / (1-tau));
			double powerNumber = Math.pow(Math.pow(1 - r, (double) 1 / (1-tau)),-tau);
			powerNumbers.add(Arrays.asList(r, powerNumber));
		}
		return powerNumbers;
	}

	public double getY(double xPos, double xmin) {
		return Math.pow(xPos / (xmin), (double) 1 - tau);
	}
}
