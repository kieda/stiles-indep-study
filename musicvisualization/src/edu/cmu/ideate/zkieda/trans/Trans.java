package edu.cmu.ideate.zkieda.trans;

/**
 * This is the class responsible for translating an input midi sequence 
 * to a controller for the visuallization. 
 *
 * The internal controller -- we manually set the values for the 
 * lambda and alpha parameters inside of our 'Trip' visualization.
 * 
 * We create an event queue for the midi, that we can make transitions 
 * by using something similar to a spline curve to bridge multiple points
 * 
 * Trans -- takes our midi sequence, 
 * -- extract higher level features of our sequence
 * -- add in a controller that reacts to the values of our sequence
 * -- controller side -- spline curve to match bridges. 
 * 
 * 
 * Controller
 * 	.addComponent(new ComponentTypeDecl())
 * 	.
 * @author zkieda
 */
public class Trans {
	
	public void Trans(){
		
	}
}
