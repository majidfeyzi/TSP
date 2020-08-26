package main;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.Border;

/**
 * Help of program.
 * Description about TSP problem and it's solution and how to interact with program.
 * @author Majid Feyzi
 * */
public class Help {

	private JFrame frame;

	private final String title = "Program Help";
	private final String subTitle = "Developed by: Majid Feyzi";
	private final String help = "<br />" +
			"TSP (Travelling Salesman Problem):<br />" +
			"in TSP exist one salesman that wanna start one city and travel to all cities and sale its products in that cities and finally return to her own city. The goal is to pick shortest path for travel to all cities as soon as possible.<br />" +
			"This program allows you to draw your custom graph as an input by just some clicking.<br />" +
			"<br />" +
			"Drawing vertex:<br />" +
			"To drawing a vertex you only need to click on plane. by drawing points, edges of complete graph will be created automatically.<br />" +
			"The first vertex that you have been drew, will be the start vertex.<br />" +
			"<br />" +
			"Sets Modifying:<br />" +
			"If you want change the location of vertecies you can undo your action and change it by Ctrl+Z .<br />" +
			"<br />" +
			"<b>Running the algorithm</b>:<br />" +
			"After drawing graph, you can run algorithm step by step, by clicking on Next step button.<br />" +
			"To run program automatically, Just click on Run button.<br />" +
			"After finish steps or complete the run, the cost of the algorithm will be shown in a dialog box, and the selected path will be shown on the graph.<br />" +
			"<br />" +
			"<b>Generate random graph</b>:<br />" +
			"If you want to generate random graph, you can click on the Random button, and it will be generate random graph.<br />" +
			"<br />" +
			"<b>Clear content</b>:<br />" +
			"To clear all content of the plane, you can click on the Reset button." +
			"<br />";
	/**
	 * Create the help frame.
	 */
	public Help() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		frame = new JFrame();
		frame.setSize(Config.FRAME_WIDTH, Config.FRAME_HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Config.THEME_COLOR);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		Component verticalStrut = Box.createVerticalStrut(10);
		frame.getContentPane().add(verticalStrut);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(Config.FONT_NAME, Font.BOLD, Config.FONT_SIZE));
		titleLabel.setPreferredSize(new Dimension(Config.FRAME_WIDTH, 30));
		titleLabel.setForeground(Color.WHITE);
		frame.getContentPane().add(titleLabel);

		JLabel subTitleLabel = new JLabel(subTitle);
		subTitleLabel.setFont(new Font(Config.FONT_NAME, Font.PLAIN, Config.FONT_SIZE));
		subTitleLabel.setPreferredSize(new Dimension(Config.FRAME_WIDTH, 30));
		subTitleLabel.setForeground(Color.WHITE);
		frame.getContentPane().add(subTitleLabel);

		frame.getContentPane().add(verticalStrut);

		JTextPane helpTextPane = new JTextPane();
		helpTextPane.setFont(new Font(Config.FONT_NAME, Font.PLAIN, Config.FONT_SIZE));
		helpTextPane.setEditable(false);
		helpTextPane.setAlignmentY(Component.TOP_ALIGNMENT);
		helpTextPane.setContentType("text/html");
		helpTextPane.setText(help);
		helpTextPane.setBorder(padding);
		JScrollPane textAreaScroll = new JScrollPane (helpTextPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(textAreaScroll);
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

}
