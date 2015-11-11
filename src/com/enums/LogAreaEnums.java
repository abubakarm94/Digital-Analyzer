package com.enums;

public enum LogAreaEnums {

	ImageReadyForAnalysis {

		@Override
		public String toString() {
			return "New image is ready for analysis";
		}
	},
	
	ImageNotReadyForAnalysis{
		@Override
		public String toString() {
			return "No Image selected";
		}
	},
	ImageFindingPattern{
		@Override
		public String toString() {
			return "Searching for recognizable pattern";
		}
	},
	ImagePatternAggregation{
		@Override
		public String toString() {
			return "Aggregating the recognized pattern";
		}
	},
	ImageFindingNeigbours{
		@Override
		public String toString() {
			return "Searching for neighbouring patterns";
		}
	},
	
	ImageConversionStarted{
		@Override
		public String toString() {
			return "Converting the image to black and white";
		}
	},
	ImageConversionFinished{
		@Override
		public String toString() {
			return "Finished converting the image to black and white";
		}
	},
	ImageAnalysisRunning{
		@Override
		public String toString() {
			return "Image Analysis is currently running, it'll only take a minute";
		}
	},ImageConnectedComponentsDone{
		@Override
		public String toString() {
			return "The recognizable connected components has been found ";
		}
	},

	CanvasReadyForAnalysis {

		@Override
		public String toString() {
			return "Canvas is ready for use";
		}
	};

}
