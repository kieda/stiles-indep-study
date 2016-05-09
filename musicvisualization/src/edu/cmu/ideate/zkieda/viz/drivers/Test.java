package edu.cmu.ideate.zkieda.viz.drivers;

public class Test {
	public static void main(String[] args){
		double alpha = 1.0;
		double lambda = 1.0;
		
		double dist = 11*11;
		System.out.println(alpha*Math.pow(dist, lambda) % 255);
		dist = 12*12;
		System.out.println(alpha*Math.pow(dist, lambda) % 255);
		dist = 13*13;
		System.out.println(alpha*Math.pow(dist, lambda) % 255);
		dist = 14*14;
		System.out.println(alpha*Math.pow(dist, lambda) % 255);
		dist = 15*15;
		System.out.println(alpha*Math.pow(dist, lambda) % 255);
		dist = 16*16;
		System.out.println(alpha*Math.pow(dist, lambda) % 255);
	}
}
