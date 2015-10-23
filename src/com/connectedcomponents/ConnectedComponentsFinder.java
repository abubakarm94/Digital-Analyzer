package com.connectedcomponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class ConnectedComponentsFinder {
	private int[][] _board;
	private BufferedImage _input;
	private Graphics inputGD;
	private int _width;
	private int _height;
	private int backgroundColor;
	
	//This contains the list of the connected components
	private ArrayList<BufferedImage> connectedImages;

	// since the image would be converted to grayscale prior. the default background is white
	private final int bgColor = 0xFFFFFFFF; 
	

	public ConnectedComponentsFinder(Image passedImage) {
		try {

			BufferedImage img = (BufferedImage) passedImage;

			Map<Integer, BufferedImage> components = this.StartProcessForConnectedComponent(img, bgColor);

			String format = getFileNameExtension("images/one.png");

			ImageIO.write(this.getProcessedImage(), format,
					new File(getBaseFileName("images/one.png") + "-processed" + "." + format));
			
			System.out.println("Connected Components Analyzes: Done");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	

	public Map<Integer, BufferedImage> StartProcessForConnectedComponent(BufferedImage input, int bgColor) {
		connectedImages = new ArrayList();
		backgroundColor = bgColor;
		_input = input;
		_width = input.getWidth();
		_height = input.getHeight();
		_board = new int[_width][];
		
		for (int i = 0; i < _width; i++)
			_board[i] = new int[_height];

		Map<Integer, List<Pixel>> patterns = Find();
		Map<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();

		inputGD = _input.getGraphics();
		inputGD.setColor(Color.BLUE);
		
		for (Integer id : patterns.keySet()) {
			BufferedImage bmp = CreateBitmap(patterns.get(id));

			//add each connected components to the array list
			connectedImages.add(bmp);
			
			images.put(id, bmp);
		}
		inputGD.dispose();

		return images;
	}

	protected boolean CheckIsBackGround(Pixel currentPixel) {
		// check if pixel color is backgroundColor (white).
		// return currentPixel.color.getAlpha() == 255 &&
		// currentPixel.color.getRed() == 255 && currentPixel.color.getGreen()
		// == 255 && currentPixel.color.getBlue() == 255;
		return currentPixel.color == backgroundColor;
	}

	private static int Min(List<Integer> neighboringLabels, Map<Integer, Label> allLabels) {
		if (neighboringLabels.isEmpty())
			return 0; // TODO: is 0 appropriate for empty list

		int ret = allLabels.get(neighboringLabels.get(0)).GetRoot().name;
		
		for (Integer n : neighboringLabels) {
			int curVal = allLabels.get(n).GetRoot().name;
			ret = (ret < curVal ? ret : curVal);
		}
		return ret;
	}
	
	/*
	 * 
	 * returns the list of the component components as images
	 */
	public ArrayList<BufferedImage> getConnectedComponentsAsList(){
		return connectedImages;
	}
	
	/*
	 * Writes each section of the connected component to the disk as 
	 * example: temp0.png
	 */
	public void exportConnectedComponentsAsImages(){
		int id =0;
		for(BufferedImage image: getConnectedComponentsAsList()){
			try {
				ImageIO.write(image, "png", new File(getBaseFileName("images/connectedComponentsImages/temp.png")+id+".png"));
				id++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Connected Components Sections Exported");
	}
	
	

	private static int Min(List<Pixel> pattern, boolean xOrY) {
		if (pattern.isEmpty())
			return 0; // TODO: is 0 appropriate for empty list

		int ret = (xOrY ? pattern.get(0).x : pattern.get(0).y);
		for (Pixel p : pattern) {
			int curVal = (xOrY ? p.x : p.y);
			ret = (ret < curVal ? ret : curVal);
		}
		return ret;
	}

	private static int Max(List<Pixel> pattern, boolean xOrY) {
		if (pattern.isEmpty())
			return 0; // TODO: is 0 appropriate for empty list

		int ret = (xOrY ? pattern.get(0).x : pattern.get(0).y);
		for (Pixel p : pattern) {
			int curVal = (xOrY ? p.x : p.y);
			ret = (ret > curVal ? ret : curVal);
		}
		return ret;
	}

	private Map<Integer, List<Pixel>> Find() {
		int labelCount = 1;
		Map<Integer, Label> allLabels = new HashMap<Integer, Label>();

		for (int i = 0; i < _height; i++) {
			for (int j = 0; j < _width; j++) {
				Pixel currentPixel = new Pixel(j, i, _input.getRGB(j, i));

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

				_board[j][i] = currentLabel;
			}
		}

		Map<Integer, List<Pixel>> patterns = AggregatePatterns(allLabels);

		return patterns;
	}

	private List<Integer> GetNeighboringLabels(Pixel pix) {
		List<Integer> neighboringLabels = new ArrayList<Integer>();

		for (int i = pix.y - 1; i <= pix.y + 2 && i < _height - 1; i++) {
			for (int j = pix.x - 1; j <= pix.x + 2 && j < _width - 1; j++) {
				if (i > -1 && j > -1 && _board[j][i] != 0) {
					neighboringLabels.add(_board[j][i]);
				}
			}
		}

		return neighboringLabels;
	}

	private Map<Integer, List<Pixel>> AggregatePatterns(Map<Integer, Label> allLabels) {
		Map<Integer, List<Pixel>> patterns = new HashMap<Integer, List<Pixel>>();

		for (int i = 0; i < _height; i++) {
			for (int j = 0; j < _width; j++) {
				int patternNumber = _board[j][i];

				if (patternNumber != 0) {
					patternNumber = allLabels.get(patternNumber).GetRoot().name;

					if (!patterns.containsKey(patternNumber)) {
						patterns.put(patternNumber, new ArrayList<Pixel>());
					}

					patterns.get(patternNumber).add(new Pixel(j, i, _input.getRGB(j, i)));
				}
			}
		}

		return patterns;
	}

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
																// by minX and
																// minY
		}

		inputGD.drawRect(minX, minY, maxX - minX, maxY - minY);

		return bmp;
	}

	public static String getBaseFileName(String fileName) {
		return fileName.substring(0, fileName.indexOf('.'));
	}

	public static String getFileNameExtension(String fileName) {
		return fileName.substring(fileName.indexOf('.') + 1);
	}

	public BufferedImage getProcessedImage() {
		return _input;
	}

}