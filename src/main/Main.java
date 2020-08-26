package main;

import java.awt.*;
import javax.swing.*;

import grid.Context;
import grid.Grid;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.GroupLayout.Alignment;

/**
 * Main frame of the program.
 * @author Majid Feyzi
 * */
public class Main extends Context {
	
	private JFrame frame;
	private Grid grid;

	private JScrollPane buttonsScrollablePanel;
	private JPanel buttonsPanel;

	private JButton clearButton;
	private JButton nextButton;
	private JButton helpButton;
	private JButton runButton;
	private JButton randomButton;
	private JButton exitButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Main window = new Main();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initialize frame
		frame = new JFrame("Travelling Salesman Problem");
		frame.setSize(Config.FRAME_WIDTH, Config.FRAME_HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				grid.setSize(new Dimension(grid.getWidth(), frame.getHeight()));
				grid.resize();

				// Change focus to grid
				grid.requestFocus();
			}
		});
		
		// Initialize grid canvas and add it to frame
		grid = new Grid(this);
		grid.setBackground(Color.WHITE);
		grid.setPreferredSize(new Dimension(grid.getWidth(), Config.FRAME_HEIGHT));

		// Initialize bottom panel and add it to bottom of frame
		buttonsScrollablePanel = createScrollablePanel();
		buttonsPanel = (JPanel) buttonsScrollablePanel.getViewport().getView();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Initialize clear canvas button
		clearButton = createButton("Reset");
		clearButton.addActionListener(e -> {
			grid.clear();

			// Change focus to grid
			grid.requestFocus();
		});

		nextButton = createButton("Next step");
		nextButton.addActionListener(e -> {
			grid.next();

			// Change focus to grid
			grid.requestFocus();
		});

		runButton = createButton("Run");
		runButton.addActionListener(e -> {
			grid.startAutoSolve();

			// Change focus to grid
			grid.requestFocus();
		});
		buttonsPanel.add(runButton);
		buttonsPanel.add(nextButton);

		randomButton = createButton("Random");
		randomButton.addActionListener(e -> {
			try {
				int count = Integer.parseInt(JOptionPane.showInputDialog("Please insert number of points"));

				int padding = 100;
				int width = grid.getWidth() - padding;
				int height = grid.getHeight() - padding;
				grid.generateRandomGraph(count, padding, padding, width, height);

			} catch (Exception ignored) {}
		});
		buttonsPanel.add(randomButton);
		buttonsPanel.add(clearButton);

		helpButton = createButton("Help");
		helpButton.addActionListener(e -> {
			Help hframe = new Help();
			hframe.setVisible(true);
		});
		buttonsPanel.add(helpButton);

		exitButton = createButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));
		buttonsPanel.add(exitButton);
		
		// Change focus to grid
		grid.setFocusable(true);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(grid, GroupLayout.DEFAULT_SIZE, 1754, Short.MAX_VALUE)
					.addComponent(buttonsScrollablePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(grid, GroupLayout.DEFAULT_SIZE, 1023, Short.MAX_VALUE)
				.addComponent(buttonsScrollablePanel, GroupLayout.DEFAULT_SIZE, 1023, Short.MAX_VALUE)
		);
		frame.getContentPane().setLayout(groupLayout);
		grid.requestFocus();
	}

	@Override
	public void onSolveComplete(String result) {
		JLabel label = new JLabel("<html>" + result.replace("\n", "<br>") + "</html>");
		label.setFont(new Font(Config.FONT_NAME, Font.PLAIN, Config.FONT_SIZE));
		JOptionPane.showMessageDialog(null, label, "Result", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Create left and right panels method
	 * */
	private JScrollPane createScrollablePanel() {
		JPanel panel = new JPanel();
		panel.setAutoscrolls(true);
		panel.setBackground(Config.THEME_COLOR);
		panel.setPreferredSize(new Dimension(Config.RIGHT_PANEL_WIDTH, 0));
		JScrollPane panelScorllPane = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelScorllPane.setPreferredSize(new Dimension(Config.RIGHT_PANEL_WIDTH, 0));

		return panelScorllPane;
	}

	/**
	 * Create button method
	 * */
	private JButton createButton(String title) {
		JButton button = new JButton(title);
		button.setPreferredSize(new Dimension(Config.BUTTON_WIDTH, Config.BUTTON_HEIGHT));
		button.setFont(new Font(Config.FONT_NAME, Font.BOLD, Config.FONT_SIZE));
		button.setForeground(Color.WHITE);
		button.setBackground(null);
		button.setBorderPainted(false);

		return button;
	}
}
