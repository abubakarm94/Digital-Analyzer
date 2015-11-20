package com.connectedcomponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
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

public class FindConnectedComponents extends PixelPosition{
	private int[][] _board;
	private BufferedImage _input;
	private Graphics inputGD;
	private int _width;
	private int _height;
	private int backgroundColor;
	
	//will later be padded to make 28
	private final int IMAGE_SIZE = 20;
	
	//This contains the list of the connected components
	private ArrayList<List<Pixel>> connectedImages;

	// since the image would be converted to grayscale prior. the default background is white
	private final int bgColor = 0xFFFFFFFF; 
	
	//used for logging
	private LogArea logArea = null;
	

	public FindConnectedComponents(AbstractViewOptionPane view, Image passedImage) {
		
		logArea = view.getLogArea();
		
		try {

			BufferedImage img = (BufferedImage) passedImage;

			logArea.append(LogAreaEnums.ImageAnalysisRunning);
			
			Map<Integer, BufferedImage> components = this.StartProcessForConnectedComponent(img, bgColor);
			

			

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
			List<Pixel> temp  = patterns.get(id);
			BufferedImage bmp = CreateBitmap(temp);
			//add each connected components to the array list
			connectedImages.add(temp);
			
			images.put(id, bmp);
		}
		inputGD.dispose();

		logArea.append(LogAreaEnums.ImageConnectedComponentsDone);

		return images;
	}
	
	
	
	

	protected boolean CheckIsBackGround(Pixel currentPixel) {
		// check if pixel color is backgroundColor (white).
		// return currentPixel.color.getAlpha() == 255 &&
		// currentPixel.color.getRed() == 255 && currentPixel.color.getGreen()
		// == 255 && currentPixel.color.getBlue() == 255;
		return currentPixel.color == backgroundColor;
	}


	
	/*
	 * 
	 * returns the list of the component components as images
	 */
	public ArrayList<BufferedImage> getConnectedComponentsAsList(){
		ArrayList<BufferedImage> result = new ArrayList<>();
		ArrayList<List<Pixel>> returnedData =getOrderedPixelPatterns(connectedImages,0,connectedImages.size()-1);

		for(List<Pixel> data: returnedData){
			result.add(CreateBitmap(data));
		}
		return result;
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
	}
	
	

	private Map<Integer, List<Pixel>> Find() {
		logArea.append(LogAreaEnums.ImageFindingPattern);

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
		logArea.append(LogAreaEnums.ImageFindingNeigbours);

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
		logArea.append(LogAreaEnums.ImagePatternAggregation);

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
			
																
		}
		inputGD.drawRect(minX, minY, maxX - minX, maxY - minY);
		
		
		return resizeImage(bmp);
	}
	
	

	/*
	 * Center the image into a 20 by 20 image
	 */
	  private  BufferedImage resizeImage(BufferedImage originalImage){
		  	final int shiftBy = 2;
		  	
			BufferedImage resizedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE,  BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = resizedImage.createGraphics();
			
			int dx1 = (shiftBy);
			int dy1 = shiftBy;
			int dx2 = (IMAGE_SIZE - dx1);
			int dy2 = (IMAGE_SIZE - dy1);

			
			//Keep original width if possible
			if(IMAGE_SIZE >= originalImage.getWidth()){
				dx1 = IMAGE_SIZE - originalImage.getWidth();
				dx2 = (originalImage.getWidth());

			}
			
			//keep original height if possible -- might not be needed. We'll see
			if(IMAGE_SIZE >= originalImage.getHeight()){
				dy1 = IMAGE_SIZE -originalImage.getHeight();
				dy2 = originalImage.getHeight();
			}
			
		
			
			
			g.drawImage(originalImage, dx1,dy1, dx2,dy2, 0,0, originalImage.getWidth(), originalImage.getHeight(), null);
			g.dispose();
	
			
				
			return resizedImage;
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