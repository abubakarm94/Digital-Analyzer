/*
 * This abstract class performs different analysis on pixels
 */
package com.abstractClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.connectedcomponents.Label;
import com.connectedcomponents.Pixel;

public abstract class PixelPosition {

	/*
	 * Quicksort to Re-order pixels based on how it was read example: if brought in as ABA
	 * --> rearrange to ABA
	 */
	public ArrayList<List<Pixel>> getOrderedPixelPatterns(ArrayList<List<Pixel>> unOrderedImages, int left, int right) {
		int i = left;
		int j = right;
		List<Pixel> tempForSwap;

		List<Pixel> pivotValue = unOrderedImages.get((left + right) / 2);

		while (i <= j) {
			while ((getAveragePositionX(unOrderedImages.get(i)) < getAveragePositionX(pivotValue))) {
				i++;
			}
			while ((getAveragePositionX(unOrderedImages.get(j)) > getAveragePositionX(pivotValue))) {
				j--;
			}
			if (i <= j) {

				tempForSwap = unOrderedImages.get(i);
				unOrderedImages.set(i, unOrderedImages.get(j));
				unOrderedImages.set(j, tempForSwap);
				i++;
				j--;
			}

		}
		if (left < j) {
			getOrderedPixelPatterns(unOrderedImages, left, j);

		}
		if (i < right) {
			getOrderedPixelPatterns(unOrderedImages, i, right);
		}

		return unOrderedImages;
	}

	/*
	 * Gets average of the min x and max x position
	 */
	public int getAveragePositionX(List<Pixel> pattern) {
		int average = ((Min(pattern, true)) + (Min(pattern, true))) / 2;
		return average;
	}

	/*
	 * Gets average of the min y and max y position
	 */
	public int getAveragePositionY(List<Pixel> pattern) {
		int average = ((Min(pattern, false)) + (Max(pattern, false))) / 2;
		return average;
	}

	
	protected int Min(List<Integer> neighboringLabels, Map<Integer, Label> allLabels) {
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
	 * Returns the minimum X or Y position
	 */
	protected int Min(List<Pixel> pattern, boolean xOrY) {
		if (pattern.isEmpty())
			return 0; // TODO: is 0 appropriate for empty list

		int ret = (xOrY ? pattern.get(0).x : pattern.get(0).y);
		for (Pixel p : pattern) {
			int curVal = (xOrY ? p.x : p.y);
			ret = (ret < curVal ? ret : curVal);
		}
		return ret;
	}

	/*
	 * Return the maximum X or Y position
	 */
	protected static int Max(List<Pixel> pattern, boolean xOrY) {
		if (pattern.isEmpty())
			return 0; // TODO: is 0 appropriate for empty list

		int ret = (xOrY ? pattern.get(0).x : pattern.get(0).y);
		for (Pixel p : pattern) {
			int curVal = (xOrY ? p.x : p.y);
			ret = (ret > curVal ? ret : curVal);
		}
		return ret;
	}

}
