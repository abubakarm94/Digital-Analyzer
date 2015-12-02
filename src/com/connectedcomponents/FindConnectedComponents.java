/*
 * This class performs connected component analysis on inputed images
 */
package com.connectedcomponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.abstractClasses.AbstractViewOptionPane;
import com.abstractClasses.PixelPosition;
import com.enums.LogAreaEnums;
import com.uielements.LogArea;

public class FindConnectedComponents extends PixelPosition {
	private int[][] pixelBoard;
	private BufferedImage inputImage;
	private Graphics inputGD;
	private int imageWidth;
	private int imageHeight;
	private int backgroundColor;

	// Will be padded later to make 28
	private final int IMAGE_SIZE = 20;

	// This contains the list of the connected components
	private ArrayList<List<Pixel>> connectedImages;

	// since the image would be converted to grayscale prior. the default
	// background is white
	private final int bgColor = 0xFFFFFFFF;

	// used for logging
	private LogArea logArea = null;

	public FindConnectedComponents(AbstractViewOptionPane view, Image passedImage) {

		// get Log Area
		logArea = view.getLogArea();

		try {

			BufferedImage img = (BufferedImage) passedImage;

			logArea.append(LogAreaEnums.ImageAnalysisRunning);

			// returns each image found in the analysis as a map
			Map<Integer, BufferedImage> components = this.StartProcessForConnectedComponent(img, bgColor);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public Map<Integer, BufferedImage> StartProcessForConnectedComponent(BufferedImage input, int bgColor) {
		connectedImages = new ArrayList();
		backgroundColor = bgColor;
		inputImage = input;
		imageWidth = input.getWidth();
		imageHeight = input.getHeight();
		pixelBoard = new int[imageWidth][];

		for (int i = 0; i < imageWidth; i++)
			pixelBoard[i] = new int[imageHeight];

		// find pixel patterns
		Map<Integer, List<Pixel>> patterns = Find();

		// stores the images found in the analysis
		Map<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();

		inputGD = inputImage.getGraphics();

		inputGD.setColor(Color.BLUE);

		for (Integer id : patterns.keySet()) {
			List<Pixel> temp = patterns.get(id);
			// creates a BufferedImage from pixel patterns
			BufferedImage bmp = CreateBitmap(temp);
			// add each connected components to the array list
			connectedImages.add(temp);
			
			images.put(id, bmp);
		}
		inputGD.dispose();

		logArea.append(LogAreaEnums.ImageConnectedComponentsDone);

		return images;
	}

	/*
	 * checks pixel color to background color
	 */
	protected boolean CheckIsBackGround(Pixel currentPixel) {
		return currentPixel.color == backgroundColor;
	}

	/*
	 * 
	 * returns the list of the component components as images
	 */
	public ArrayList<BufferedImage> getConnectedComponentsAsList() {
		ArrayList<BufferedImage> result = new ArrayList<>();
		ArrayList<List<Pixel>> returnedData = getOrderedPixelPatterns(connectedImages, 0, connectedImages.size() - 1);

		for (List<Pixel> data : returnedData) {
			result.add(CreateBitmap(data));
		}
		return result;
	}

	/*
	 * Finds the possible pixel patterns and group them as a list of pixel
	 * patterns
	 */
	private Map<Integer, List<Pixel>> Find() {
		logArea.append(LogAreaEnums.ImageFindingPattern);

		int labelCount = 1;
		Map<Integer, Label> allLabels = new HashMap<Integer, Label>();

		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				Pixel currentPixel = new Pixel(j, i, inputImage.getRGB(j, i));

				if (CheckIsBackGround(currentPixel)) {
					continue;
				}

				List<Integer> neighboringLabels = GetNeighboringLabels(currentPixel);
				int currentLabel;

				if (neighboringLabels.isEmpty()) {
					currentLabel = labelCount;
					allLabels.put(currentLabel, new Label(currentLabel));
					labelCount++;
				} else {
					currentLabel = Min(neighboringLabels, allLabels);
					Label root = allLabels.get(currentLabel).GetRoot();

					for (Integer neighbor : neighboringLabels) {
						if (root.name != allLabels.get(neighbor).GetRoot().name) {
							allLabels.get(neighbor).Join(allLabels.get(currentLabel));
						}
					}
				}

				pixelBoard[j][i] = currentLabel;
			}
		}

		Map<Integer, List<Pixel>> patterns = AggregatePatterns(allLabels);

		return patterns;
	}

	/*
	 * Gets the neighboring labels of the passed in pixel
	 */
	private List<Integer> GetNeighboringLabels(Pixel pix) {
		logArea.append(LogAreaEnums.ImageFindingNeigbours);

		List<Integer> neighboringLabels = new ArrayList<Integer>();

		for (int i = pix.y - 1; i <= pix.y + 2 && i < imageHeight - 1; i++) {
			for (int j = pix.x - 1; j <= pix.x + 2 && j < imageWidth - 1; j++) {
				if (i > -1 && j > -1 && pixelBoard[j][i] != 0) {
					neighboringLabels.add(pixelBoard[j][i]);
				}
			}
		}

		return neighboringLabels;
	}

	/*
	 * Aggregates Pixel Patterns into a List and passes it as a map
	 */
	private Map<Integer, List<Pixel>> AggregatePatterns(Map<Integer, Label> allLabels) {
		logArea.append(LogAreaEnums.ImagePatternAggregation);

		Map<Integer, List<Pixel>> patterns = new HashMap<Integer, List<Pixel>>();

		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				int patternNumber = pixelBoard[j][i];

				if (patternNumber != 0) {
					patternNumber = allLabels.get(patternNumber).GetRoot().name;

					if (!patterns.containsKey(patternNumber)) {
						patterns.put(patternNumber, new ArrayList<Pixel>());
					}

					patterns.get(patternNumber).add(new Pixel(j, i, inputImage.getRGB(j, i)));
				}
			}
		}

		return patterns;
	}

	/*
	 * Create a BufferedImage from pixel patterns
	 */
	private BufferedImage CreateBitmap(List<Pixel> pattern) {
		int minX = Min(pattern, true);
		int maxX = Max(pattern, true);

		int minY = Min(pattern, false);
		int maxY = Max(pattern, false);

		int width = maxX + 1 - minX;
		int height = maxY + 1 - minY;

		BufferedImage bmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (Pixel pix : pattern) {
			bmp.setRGB(pix.x - minX, pix.y - minY, pix.color); // shift position

		}
		inputGD.drawRect(minX, minY, maxX - minX, maxY - minY);

		return resizeImage(bmp);
	}

	/*
	 * Center the image into a 20 by 20 image
	 */
	private BufferedImage resizeImage(BufferedImage originalImage) {
		final int shiftBy = 2;

		BufferedImage resizedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();

		int dx1 = (shiftBy);
		int dy1 = shiftBy;
		int dx2 = (IMAGE_SIZE - dx1);
		int dy2 = (IMAGE_SIZE - dy1);

		// Keep original width if possible
		if (IMAGE_SIZE >= originalImage.getWidth()) {
			dx1 = IMAGE_SIZE - originalImage.getWidth();
			
			dx2 = (originalImage.getWidth());
			

		}

		// keep original height if possible -- might not be needed. We'll see
		if (IMAGE_SIZE >= originalImage.getHeight()) {
			dy1 = IMAGE_SIZE - originalImage.getHeight();
			dy2 = originalImage.getHeight();
		}

		g.drawImage(originalImage, dx1, dy1, dx2, dy2, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
		g.dispose();

		return resizedImage;
	}

	public BufferedImage getProcessedImage() {
		return inputImage;
	}

}