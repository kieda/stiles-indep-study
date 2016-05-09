package edu.cmu.ideate.zkieda.viz.graphics;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.IntStream;
/**
 * buffer used to calculate the power of 2 at each pixel. 
 */
class ImagePow {
	private final int precisionPow;
	
	//original values (fill[x, y])^1
	private double[] buffer;
	
	//root values (fill[x, y])^(1/2^precisionPow)
	private double[] rootBuffer;
	
	//bounds
	private int width, height;
	
	private final ToDoubleBiFunction<Integer, Integer> fill;
	
	private double root(double base){
		for(int i = 0; i < precisionPow; i++){
			base = Math.sqrt(base);
		}
		return base;
	}
	
	/**
	 * @param fill the function we fill our buffer with
	 * @param precisionPow the best power of precision we will use. 
	 */
	public ImagePow(ToDoubleBiFunction<Integer, Integer> fill, int precisionPow, int width, int height){
		this.precisionPow = precisionPow;
		this.width = width;
		this.height = height;
		this.buffer = new double[width * height];
		this.rootBuffer = new double[width * height];
		this.fill = fill;
		
		Arrays.parallelSetAll(buffer, idx -> fill.applyAsDouble(idx % width, idx/width));
		
		Arrays.parallelSetAll(rootBuffer, idx -> root(buffer[idx]));
	}
	
	public void refactor(int width, int height){
		if(this.width == width && this.height == height) return;
		double[] buffer = new double[width*height];
		double[] rootBuffer = new double[width*height];
		
		IntStream.range(0, width*height)
			.parallel()
			.forEach(idx -> { 
				int x = idx % width;
				int y = idx / width;
				int originalIdx = x + y*this.width;
				
				if(x < this.width && y < this.height){
					buffer[idx] = this.buffer[originalIdx];
					rootBuffer[idx] = this.rootBuffer[originalIdx];
				} else {
					buffer[idx] = fill.applyAsDouble(x, y);
					rootBuffer[idx] = root(buffer[idx]);
				} 
			});
		
		this.buffer = buffer;
		this.rootBuffer = rootBuffer;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * returns approximation of Math.pow(fill.applyAsDouble(x, y), exp)
	 * note : unsafe.
	 */
	public double pow(int x, int y, float exp){
		final int idx = x + y*width; // index we look up
		int numericalPow = (int)exp; // round exp down. Integer component of exponentation 
		
		//exp - numericalPow is the remaining bits
		int numPow = (int)((exp - numericalPow)*(1L << precisionPow));
		
		return smallPow(buffer[idx], numericalPow) * smallPow(rootBuffer[idx], numPow);
		//note : final version is
		
		// let exp = integer + fractional
		// let buffer[idx] = val
		// we know rootBuffer[idx] = val^(1/2^precisionPow) 
		
		// then we return 
		// = (val^integer) * (val^(1/2^precisionPow))^(fractional * 2^precisionPow)
		// = (val^integer) * (val^(fractional  * 2^precisionPow / (2^precisionPow)))
		// = (val^integer) * (val^fractional)
		// = (val^(integer + fractional))
		// = (val^exp)
		
		//note that this is an approximation since we have fractional * 2^precisionPow as an integer
		//if precisionPow is large enough it will make no difference.
	}
	
	
	private double smallPow(double base, int exp){
		if(exp < 0) return smallPow(1.0/base, -exp);
		if(exp == 1) return base;
		else if(exp == 0) return 1;
		
		double p = smallPow(base, exp>>1);
		if(exp % 2 == 1){
			return base*p*p;
		} else{
			return p*p;
		}	
	}
	
	public static void main(String[] args){
		ImagePow ip = new ImagePow((x,  y) -> x * .33 + y, 14, 5, 2);
		for(double exp = .75; exp < 2.0; exp += .41){
			for(int x = 0; x < 5; x++){
				for(int y = 0; y < 2; y++){
					double pow1 = ip.pow(x, y, (float)exp);
					double pow2 = Math.pow(x*.33 + y, exp);
					if(Math.abs(pow1 - pow2) > .0001){
						System.out.println(pow1 + " " + pow2);
					}
				}
			}
		}
	}
}
