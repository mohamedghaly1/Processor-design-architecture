package GUI;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import HarvardProcessor.HarvardArch;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.io.BufferedWriter;
import java.io.FileWriter;



public class HarvardArchGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HarvardArchGUI frame = new HarvardArchGUI();
					frame.setBounds(0, 0, 1440, 800);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public HarvardArchGUI() {
		setTitle("Godfather");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1554, 1223);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JFrame popUpWindow = new JFrame("Help Window");
		JButton btnNewButton = new JButton("Help(?)");
		btnNewButton.setForeground(SystemColor.desktop);
		btnNewButton.setBackground(SystemColor.info);
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				 if (popUpWindow == null || !popUpWindow.isDisplayable()) {
					 JLabel imageLabel = new JLabel(new ImageIcon("help.png"));
			           int width = 600;  
			           int height = 400; 
			           popUpWindow.setSize(width, height);
			           popUpWindow.setLocationRelativeTo(null);
			           popUpWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			           popUpWindow.setContentPane(imageLabel);
			           popUpWindow.setVisible(true);
			        }
			    }
		});
		btnNewButton.setBounds(10, 10, 92, 21);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Enter program:-");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(139, 38, 146, 35);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 100, 414, 605);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		
		JLabel lblNewLabel_1 = new JLabel("Outputs:-");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(650, 38, 152, 35);
		contentPane.add(lblNewLabel_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(526, 100, 801, 605);
		contentPane.add(scrollPane_1);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		textArea_1.setEditable(false);
		
		JButton btnNewButton_1 = new JButton("Run");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter("program.txt"))) {
		            writer.write(textArea.getText());
		            System.out.println("Successfully wrote to the file.");
		        } catch (Exception e1) {
		        	JOptionPane.showMessageDialog(null, "Please ensure accurate assembly code input. Assistance is available via the help button if needed.", "Error", JOptionPane.PLAIN_MESSAGE);
		        }
				try {
					//HarvardArch x = new HarvardArch();
					textArea_1.setText(new HarvardArch().ExecuteProgram());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Please ensure accurate assembly code input. Assistance is available via the help button if needed.", "Error", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
				
		    }
			
		});
		btnNewButton_1.setBackground(Color.GREEN);
		btnNewButton_1.setBounds(21, 732, 85, 21);
		contentPane.add(btnNewButton_1);
		

      
		
		
	}
}
