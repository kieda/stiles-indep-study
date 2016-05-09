package edu.cmu.ideate.zkieda.viz.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;

public class ControllerUI extends JFrame {

	private JPanel contentPane;
	private JPanel mainPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ControllerUI frame = new ControllerUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ControllerUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{500, 0};
		gbl_contentPane.rowHeights = new int[]{16, 238, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblGroundControl = new JLabel("Ground Control");
		GridBagConstraints gbc_lblGroundControl = new GridBagConstraints();
		gbc_lblGroundControl.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblGroundControl.insets = new Insets(0, 0, 5, 0);
		gbc_lblGroundControl.gridx = 0;
		gbc_lblGroundControl.gridy = 0;
		contentPane.add(lblGroundControl, gbc_lblGroundControl);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		
		contentPane.add(scrollPane, gbc_scrollPane);
		
		mainPanel = new JPanel();
		scrollPane.setViewportView(mainPanel);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{300, 0};
		gbl_mainPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_mainPanel.rowWeights = null;//new double[]{0.0, 0.0, Double.MIN_VALUE};
		
		mainPanel.setLayout(gbl_mainPanel);
		
		
		addControllers();
	}
	private void addControllers(){
		int rowNum = 1;
		
		smoothingPanel = new EnumPanel("Smoothing", mainPanel, rowNum++);
		
		smoothingPanel.getSelector().addItem("Quadratic");
		smoothingPanel.getSelector().addItem("Linear");
		smoothingPanel.getSelector().addItem("None");
		display = new AlphaDisplay(mainPanel, rowNum++);
		alphaPanel = new ValuePanel("Alpha (\u03B1)", mainPanel, rowNum++, .001, .0005);
		alphaChangePanel = new ValuePanel("Alpha Change (\u0394\u03B1)", mainPanel, rowNum++, 0, .0005);
		lambdaPanel = new ValuePanel("Lambda (\u03BB)", mainPanel, rowNum++, 1.0, .5);
		lambdaChangePanel = new ValuePanel("Lambda Change (\u0394\u03BB)", mainPanel, rowNum++, 0, .25);

		darkenPanel = new EnumPanel("Darken", mainPanel, rowNum++);
		
		darkenPanel.getSelector().addItem("On");
		darkenPanel.getSelector().addItem("Off");
		
		GridBagLayout layout = (GridBagLayout)mainPanel.getLayout();
		
		double[] newWeights = new double[rowNum];
		newWeights[rowNum - 1] = 1.0;
		layout.rowWeights = newWeights;
		
		pack();
	}
	
	private ValuePanel alphaPanel, alphaChangePanel, lambdaPanel, lambdaChangePanel;
	private EnumPanel darkenPanel, smoothingPanel;
	
	public ValuePanel getAlphaPanel() {
		return alphaPanel;
	}
	public ValuePanel getAlphaChangePanel() {
		return alphaChangePanel;
	}
	public ValuePanel getLambdaPanel() {
		return lambdaPanel;
	}
	public ValuePanel getLambdaChangePanel() {
		return lambdaChangePanel;
	}
	
	public EnumPanel getDarkenPanel() {
		return darkenPanel;
	}
	public EnumPanel getSmoothingPanel() {
		return smoothingPanel;
	}
	
	
	//just displays the current value of alpha.
	//updates wrt time.
	public class AlphaDisplay extends JPanel{
//		private JLabel currentValue;
		private JLabel title;
		private JTextField valueField;
		
		private AlphaDisplay(Container parent, int ypos){
//			this.currentValue = new JLabel("");
			GridBagConstraints gbc_enumPanel = new GridBagConstraints();
			gbc_enumPanel.insets = new Insets(0, 10, 0, 10);
			gbc_enumPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_enumPanel.anchor = GridBagConstraints.NORTH;
			gbc_enumPanel.gridx = 0;
			gbc_enumPanel.gridy = ypos;
			parent.add(this, gbc_enumPanel);
			
			GridBagLayout gbl_enumPanel = new GridBagLayout();
			gbl_enumPanel.columnWidths = new int[]{35, 350, 0};
			gbl_enumPanel.rowHeights = new int[]{20, 0};
			gbl_enumPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_enumPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			this.setLayout(gbl_enumPanel);
			
			this.title = new JLabel("Current Alpha");
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.anchor = GridBagConstraints.WEST;
			gbc_label.insets = new Insets(0, 0, 0, 5);
			gbc_label.gridx = 0;
			gbc_label.gridy = 0;
			this.add(this.title, gbc_label);
			
			valueField = new JTextField(10);
			valueField.setEditable(false);
			valueField.setFont(new Font("Monospaced", Font.PLAIN, 13));
			valueField.setPreferredSize(new Dimension(385, 27));
			
			GridBagConstraints gbc_enumSelection = new GridBagConstraints();
			gbc_enumSelection.fill = GridBagConstraints.HORIZONTAL;
			gbc_enumSelection.anchor = GridBagConstraints.NORTH;
			gbc_enumSelection.gridx = 1;
			gbc_enumSelection.gridy = 0;
			this.add(this.valueField, gbc_enumSelection);
		}
		
		public void setAlpha(double d){
			this.valueField.setText(ValuePanel.digitFormatting().format(d));
		}
	}
	private AlphaDisplay display;
	public AlphaDisplay getAlphaDisplayPanel(){
		return display;
	}
}
