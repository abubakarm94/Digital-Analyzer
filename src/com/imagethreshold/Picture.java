package com.imagethreshold;

/*
 * Class for manipulating individual pixel in image
 */

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
public final class Picture {
	
	private BufferedImage image; // the rasterized image
	private JFrame frame; // on-screen view
	private String filename; // name of file
	private boolean isOriginUpperLeft = true; // location of origin
	private final int width, height; // width and height


	public Picture(int width, int height) {
		if (width < 0)
			throw new IllegalArgumentException("width must be nonnegative");
		if (height < 0)
			throw new IllegalArgumentException("height must be nonnegative");
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// set to TYPE_INT_ARGB to support transparency
		filename = width + "-by-" + height;
	}

	/*
	 * Initializes a new picture that is a deep copy of the argument picture.
	 *
	 */
	public Picture(Picture picture) {
		width = picture.width();
		height = picture.height();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		filename = picture.filename;
		for (int col = 0; col < width(); col++)
			for (int row = 0; row < height(); row++)
				image.setRGB(col, row, picture.get(col, row).getRGB());
	}

	/*
	 * Initializes a picture by reading from a file or URL.
	 *
	 */
	public Picture(String filename) {
		this.filename = filename;
		try {
			// try to read from file in working directory
			File file = new File(filename);
			if (file.isFile()) {
				image = ImageIO.read(file);
			}

			// now try to read from file in same directory as this .class file
			else {
				URL url = getClass().getResource(filename);
				if (url == null) {
					url = new URL(filename);
				}
				image = ImageIO.read(url);
			}
			width = image.getWidth(null);
			height = image.getHeight(null);
		} catch (IOException e) {
			// e.printStackTrace();
			throw new RuntimeException("Could not open file: " + filename);
		}
	}
	
	/*
	 * Initializes the picture by passing in BufferedImage
	 */
	public Picture(BufferedImage image2) {
		image = image2;

		
		width = image.getWidth(null);
		height = image.getHeight(null);
		
		filename = width + "-by-" + height;

	}

	/*
	 * Initializes a picture by reading in a .png, .gif, or .jpg from a file.
	 *
	 */
	public Picture(File file) {
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not open file: " + file);
		}
		if (image == null) {
			throw new RuntimeException("Invalid image file: " + file);
		}
		width = image.getWidth(null);
		height = image.getHeight(null);
		filename = file.getName();
	}

	/*
	 * returns the modified / non modified image -- add by us
	 * 
	 */
	public Image getImage() {
		if (image == null) {
			return null;
		}
		return image;
	}

	/*
	 * Sets the origin to be the upper left pixel. This is the default.
	 */
	public void setOriginUpperLeft() {
		isOriginUpperLeft = true;
	}

	/*
	 * Sets the origin to be the lower left pixel.
	 */
	public void setOriginLowerLeft() {
		isOriginUpperLeft = false;
	}

	/*
	 * Returns the height of the picture.
	 *
	 */
	public int height() {
		return height;
	}

	/*
	 * Returns the width of the picture.
	 *
	 */
	public int width() {
		return width;
	}

	/*
	 * Returns the color of pixel by passing in y and x
	 *
	 */
	public Color get(int col, int row) {
		if (col < 0 || col >= width())
			throw new IndexOutOfBoundsException("col must be between 0 and " + (width() - 1));
		if (row < 0 || row >= height())
			throw new IndexOutOfBoundsException("row must be between 0 and " + (height() - 1));
		if (isOriginUpperLeft)
			return new Color(image.getRGB(col, row));
		else
			return new Color(image.getRGB(col, height - row - 1));
	}

	/*
	 * Sets the color of pixel y and x to given color.
	 *
	 */
	public void set(int col, int row, Color color) {
		if (col < 0 || col >= width())
			throw new IndexOutOfBoundsException("col must be between 0 and " + (width() - 1));
		if (row < 0 || row >= height())
			throw new IndexOutOfBoundsException("row must be between 0 and " + (height() - 1));
		if (color == null)
			throw new NullPointerException("can't set Color to null");
		if (isOriginUpperLeft)
			image.setRGB(col, row, color.getRGB());
		else
			image.setRGB(col, height - row - 1, color.getRGB());
	}

	/*
	 * Returns true if this picture is equal to the argument picture.
	 *
	 */
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null)
			return false;
		if (other.getClass() != this.getClass())
			return false;
		Picture that = (Picture) other;
		if (this.width() != that.width())
			return false;
		if (this.height() != that.height())
			return false;
		for (int col = 0; col < width(); col++)
			for (int row = 0; row < height(); row++)
				if (!this.get(col, row).equals(that.get(col, row)))
					return false;
		return true;
	}

	/**
	 * This operation is not supported because pictures are mutable.
	 *
	 * @return does not return a value
	 * @throws UnsupportedOperationException
	 *             if called
	 */
	public int hashCode() {
		throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
	}

	/*
	 * Saves the picture to a file in a standard image format. The filetype must
	 * be .png or .jpg.
	 */
	public void save(String name) {
		save(new File(name));
	}

	/*
	 * Saves the picture to a file in a PNG or JPEG image format.
	 *
	 */
	public void save(File file) {
		filename = file.getName();
		if (frame != null)
			frame.setTitle(filename);
		String suffix = filename.substring(filename.lastIndexOf('.') + 1);
		suffix = suffix.toLowerCase();
		if (suffix.equals("jpg") || suffix.equals("png")) {
			try {
				ImageIO.write(image, suffix, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Error: filename must end in .jpg or .png");
		}
	}

}
