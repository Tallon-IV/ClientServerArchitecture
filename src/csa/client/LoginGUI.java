package csa.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import csa.database.DatabaseEntry;
import csa.shared.RequestReply;

public class LoginGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public LoginGUI()
	{
		GridLayout grid = new GridLayout(3,3);
		//JFrame j = new JFrame();
		setLayout(grid);
		setSize(256, 256);
		setLocation(256, 256);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JLabel lblUsername = new JLabel("Username:");
		JLabel lblPassword = new JLabel("Password:");
		JTextField fieldUsername = new JTextField();
		JPasswordField fieldPassword = new JPasswordField();
		JButton btnSubmit = new JButton("Submit");
		JLabel lblResult = new JLabel();
		
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String szName = fieldUsername.getText().trim();
				String szPassword = new String(fieldPassword.getPassword()).trim();
				
				if (szName.isEmpty() || szPassword.isEmpty())
				{
					return;
				}
				
				try 
				{
					Client client = new Client(8888);
					HashMap<String, String> mapData = new HashMap<String, String>();
					mapData.put(DatabaseEntry.KEY_ID, szName);
					mapData.put(DatabaseEntry.KEY_PASS, szPassword);
					RequestReply request = new RequestReply(RequestReply.AUTH, mapData);
					client.SendString(request.toString());
					RequestReply response = RequestReply.Parse(client.ReceiveString());
					
					if (response.GetRequestType().equals(RequestReply.AUTH))
					{
						HashMap<String, String> mapResponseData = response.GetData();
						if (mapResponseData.containsKey(RequestReply.ERRORKEY))
						{
							lblResult.setText(mapResponseData.get(RequestReply.ERRORKEY));
							return;
						}
						
						fieldUsername.setEnabled(false);
						fieldPassword.setEnabled(false);
						lblUsername.setEnabled(false);
						lblPassword.setEnabled(false);
						btnSubmit.setEnabled(false);
						
						lblResult.setText(String.format("Your marks are: %s", mapResponseData.get("marks")));
					}
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		add(lblUsername);
		add(fieldUsername);
		add(lblPassword);
		add(fieldPassword);
		add(btnSubmit);
		add(lblResult);
	}
}
