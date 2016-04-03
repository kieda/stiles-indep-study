package edu.cmu.ideate.zkieda.viz.graphics;

public interface ImageProcessor {
	public void process(int[] pixels, int width, int height);
	
	//chains this ip with another
	public default ImageProcessor chain(ImageProcessor other){
		return (pix, w, h) -> {
			process(pix, w, h);
			other.process(pix, w, h);
		};
	}
}
