package edu.cmu.ideate.zkieda.viz.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnumPanel extends JPanel {
	private JLabel label;
	private JComboBox<String> selector;
	
	public EnumPanel(String title, Container parent, int ypos){
		GridBagConstraints gbc_enumPanel = new GridBagConstraints();
		gbc_enumPanel.insets = new Insets(0, 10, 0, 10);
		gbc_enumPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_enumPanel.anchor = GridBagConstraints.NORTH;
		gbc_enumPanel.gridx = 0;
		gbc_enumPanel.gridy = ypos;
		parent.add(this, gbc_enumPanel);
		
		GridBagLayout gbl_enumPanel = new GridBagLayout();
		gbl_enumPanel.columnWidths = new int[]{35, 0, 350, 0};
		gbl_enumPanel.rowHeights = new int[]{20, 0};
		gbl_enumPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_enumPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gbl_enumPanel);
		
		this.label = new JLabel(title);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		this.add(this.label, gbc_label);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalGlue_1.setPreferredSize(new Dimension(10, 0));
		horizontalGlue_1.setMaximumSize(new Dimension(50, 0));
		GridBagConstraints gbc_horizontalGlue_1 = new GridBagConstraints();
		gbc_horizontalGlue_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_horizontalGlue_1.gridx = 1;
		gbc_horizontalGlue_1.gridy = 0;
		this.add(horizontalGlue_1, gbc_horizontalGlue_1);
		
		selector = new JComboBox<String>();
		selector.setPreferredSize(new Dimension(385, 27));
		GridBagConstraints gbc_enumSelection = new GridBagConstraints();
		gbc_enumSelection.fill = GridBagConstraints.HORIZONTAL;
		gbc_enumSelection.anchor = GridBagConstraints.NORTH;
		gbc_enumSelection.gridx = 2;
		gbc_enumSelection.gridy = 0;
		this.add(selector, gbc_enumSelection);
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public JComboBox<String> getSelector() {
		return selector;
	}
	
	private ItemListener current; 
	public void setItemListener(Consumer<String> listnerFn){
		if(current != null) selector.removeItemListener(current);
		current = it -> {
			if(it.getStateChange() == ItemEvent.SELECTED){
				listnerFn.accept((String)selector.getSelectedItem());
			}
		};
		selector.addItemListener(current);
	}
}
