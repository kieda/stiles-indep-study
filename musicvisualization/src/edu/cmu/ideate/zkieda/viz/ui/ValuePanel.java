package edu.cmu.ideate.zkieda.viz.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.DoubleConsumer;

import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ValuePanel extends JPanel{
	private JLabel label;
	private JTextField valueSigmaTextField;
	private JTextField valueTextField;
	private DoubleConsumer onValueChanged;
	private double value;
	private double valueSigma;
	private double delta = 0;
	private final int precision = 50; 
	
	
	private NumberFormat formatting;
	
	public ValuePanel(String title, Container parent, 
			int ypos,
			double value, 
			double sigma){
		GridBagConstraints gbc_valuePanel = new GridBagConstraints();
		gbc_valuePanel.anchor = GridBagConstraints.NORTH;
		gbc_valuePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_valuePanel.insets = new Insets(0, 10, 0, 10);
		gbc_valuePanel.gridx = 0;
		gbc_valuePanel.gridy = ypos;
		
		parent.add(this, gbc_valuePanel);
		GridBagLayout gbl_valuePanel = new GridBagLayout();
		gbl_valuePanel.columnWidths = new int[]{0, 0, 50, 80, 100, 0};
		gbl_valuePanel.rowHeights = new int[]{29, 0};
		gbl_valuePanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_valuePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gbl_valuePanel);
		
		label = new JLabel(title);
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.anchor = GridBagConstraints.WEST;
		gbc_lblValue.insets = new Insets(0, 0, 0, 5);
		gbc_lblValue.gridx = 0;
		gbc_lblValue.gridy = 0;
		this.add(label, gbc_lblValue);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setPreferredSize(new Dimension(10, 0));
		horizontalGlue.setMaximumSize(new Dimension(50, 0));
		GridBagConstraints gbc_horizontalGlue = new GridBagConstraints();
		gbc_horizontalGlue.fill = GridBagConstraints.HORIZONTAL;
		gbc_horizontalGlue.gridx = 1;
		gbc_horizontalGlue.gridy = 0;
		this.add(horizontalGlue, gbc_horizontalGlue);
		
		JSlider valueChanger = new JSlider();
		valueChanger.setPreferredSize(new Dimension(175, 29));
		valueChanger.setAlignmentX(Component.LEFT_ALIGNMENT);
		valueChanger.setMinimum(0);
		valueChanger.setMaximum(precision * 2);
		GridBagConstraints gbc_valueChanger = new GridBagConstraints();
		gbc_valueChanger.anchor = GridBagConstraints.WEST;
		gbc_valueChanger.insets = new Insets(0, 0, 0, 5);
		gbc_valueChanger.gridx = 2;
		gbc_valueChanger.gridy = 0;
		this.add(valueChanger, gbc_valueChanger);
		
		
		formatting = digitFormatting();
		valueSigmaTextField = new JFormattedTextField(formatting);
		valueSigmaTextField.setFont(new Font("Monospaced", Font.PLAIN, 13));
		valueSigmaTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		GridBagConstraints gbc_valueSigma = new GridBagConstraints();
		gbc_valueSigma.fill = GridBagConstraints.HORIZONTAL;
		gbc_valueSigma.insets = new Insets(0, 0, 0, 5);
		gbc_valueSigma.gridx = 3;
		gbc_valueSigma.gridy = 0;
		this.add(valueSigmaTextField, gbc_valueSigma);
		valueSigmaTextField.setColumns(7);
		
		valueTextField = new JFormattedTextField(formatting);
		valueTextField.setFont(new Font("Monospaced", Font.PLAIN, 13));
		valueTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		GridBagConstraints gbc_valueTextField = new GridBagConstraints();
		gbc_valueTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_valueTextField.gridx = 4;
		gbc_valueTextField.gridy = 0;
		this.add(valueTextField, gbc_valueTextField);
		valueTextField.setColumns(8);
		
		
		//listeners etc
		onValueChanged = d -> {};
		
		valueTextField.addCaretListener(evt -> {
			try{
				
				double d = Double.parseDouble(valueTextField.getText()) - this.delta;
				if(this.value != d){
					this.value = d;
					this.onValueChanged.accept(d + this.delta);
				}
				
			} catch(NumberFormatException e){
				//throw out -- user error.
			}
		});
		
		valueSigmaTextField.addCaretListener(evt -> {
			try{
				double d = Double.parseDouble(valueSigmaTextField.getText());
				
				if(this.valueSigma != d){
					this.valueSigma = d;
					this.value = this.value + this.delta;
					valueChanger.setValue(precision);
				}
			} catch(NumberFormatException e){
				//throw out -- user error.
			}
		});
		
		valueChanger.addChangeListener(ce -> {
			int val = valueChanger.getValue() - precision;
			double delta = (val / ((double)(precision))) * valueSigma;
			if(this.delta != delta) {
				this.delta = delta;
				valueTextField.setText(formatting.format(this.delta + this.value));
				this.onValueChanged.accept(this.delta + this.value);
			}
		});
		valueTextField.setText(formatting.format(value));
		valueSigmaTextField.setText(formatting.format(sigma));
		
	}
	public static NumberFormat digitFormatting(){
		DecimalFormat decimalFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMaximumFractionDigits(12);
		return decimalFormat;
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public JTextField getValueSigma() {
		return valueSigmaTextField;
	}
	
	public JTextField getValueTextField() {
		return valueTextField;
	}
	
	//sets the listener for when the value changes for this field
	public void setValueChangeListener(DoubleConsumer fn){
		this.onValueChanged = fn;
	}
	
//	public void setValue(double value){
////		if(this.value + delta != value){
////			this.value = value - this.delta;
////			
////			valueTextField.setText(formatting.format(value));
////		}
//	}
}
