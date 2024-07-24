package com.paulusmaulus.raytracer.core.swing_assets;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;

import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.data.PixelData;

public class Graph extends JPanel {
	private PixelData[][] pixelData;
	private long[][] renderingTime;
	private double maxDistance = Double.NEGATIVE_INFINITY;
	private double maxTime = Double.NEGATIVE_INFINITY;

	public Graph(PixelData[][] hits, long[][] renderingTime) {
		super();
		this.pixelData = hits;
		this.renderingTime = renderingTime;

		// Calculate maxDistance and maxTime
		for (int y = 0; y < hits.length; y++) {
			for (int x = 0; x < hits[y].length; x++) { // Correct loop bounds
				if (hits[y][x].getDistance() > maxDistance) {
					if (hits[y][x].getDistance() == Double.POSITIVE_INFINITY)
						maxDistance = Double.MAX_VALUE;
					else
						maxDistance = hits[y][x].getDistance();
				}
				if (renderingTime[y][x] > maxTime) // Correct indexing
					maxTime = renderingTime[y][x]; // Correct indexing
			}
		}

		repaint();
		setSize(new Dimension(hits[0].length, hits.length));
		setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGraph(g, pixelData[0].length, pixelData.length, false);
	}

	private void drawGraph(Graphics g, int graphWidth, int graphHeight, boolean time) {
		for (int y = 0; y < pixelData.length; y++) {
			for (int x = 0; x < pixelData[y].length; x++) {
				double brightnessFactor = 0;
				if (time)
					brightnessFactor = maxTime / renderingTime[y][x]; // Correct indexing
				else
					brightnessFactor = maxDistance / pixelData[y][x].getDistance(); // Correct indexing

				Color color = Color.BLACK;
				color.brighten(brightnessFactor);
				g.setColor(color.toAWT());
				g.fillRect(x, y, 1, 1);
			}
		}
	}
}
