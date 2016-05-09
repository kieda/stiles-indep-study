package edu.cmu.ideate.zkieda.viz.graphics;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * fast powers of n by approximation.  
 * @author zkieda
 */
public class FastPow {
	//round up to the nearest precisionBase
	private int precisionPow;
	
	public FastPow(int precisionPow){
		this.precisionPow = precisionPow;
	}
	
	public float pow(double base, float exp){
		//k * base^(1/2^precisionPow) ~= base^exp
		double m2 = base;
		
		for(int i = 0; i < precisionPow; i++){
			m2 = Math.sqrt(m2);
		}
		// m2 = base ^ (1 / 2^precisionPow)
		
		
		return (float)(new IntPower(m2).pow((int)(exp*(1 << precisionPow))));
		//m2 ^ (exp * 2 ^ (precisionPow))
		//= (base ^ (1 / 2^precisionPow)) ^ (exp * 2^precisionPow)
		//= (base ^ exp)
	}
	
	
	//x^(k), k in nats
	//c / (x^k)
	private static class IntPower{
		private double base;
		
		private IntPower(double base){
			this.base = base;
		}
		// = base^(num)
		private double pow(int num){
			if(num==0) return 1;
			
			double pow = 1;
			double accum = base;
			
			while(num > 1){
				if((num&1) == 0){
					pow *= pow;
				} else{
					accum *= pow;
					pow *= pow;
				}
				num = num>>1;
			}
			
			return pow*accum;
//			if(num == 1) return base;
//			else if(num == 0) return 1;
//			
//			double p = pow(num>>1);
//			if(num % 2 == 1){
//				return base*p*p;
//			} else{
//				return p*p;
//			} 
		}
	}
	
	
	//estimator function
	//seems like 14 is a good estimator for a the power 
	public static void main(String[] args){
		FastPow fp = new FastPow(14);
		System.out.println(fp.pow(2, 2));
//		for(int i = 0; i < 100; i++){
//			float val = ((float)i) / 100f;
//			float actual = (float)Math.pow  (1.83094, val);
//			float expected = Math.abs(fp.pow(1.83094, val)); 
//			if(Math.abs(expected - actual) > .001){
//				System.out.println(expected + " " + actual);
//			}
//			
//		}
	}
}
