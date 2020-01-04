package com.chat.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.chat.util.XMLUtil;

//sever's GUI
public class Server extends JFrame {

	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JButton jButton;

	private JPanel jPanel1;//�ϲ� jpane

	private JPanel jPanel2; //�²� jpane

	private JScrollPane jScrollPane;  //jTestArea's scroll

	private JTextArea jTextArea;//��������Ϣ

	private JTextField jTextField;//  �˿ں�	port  ��д����

	private Map<String, ServerMessageThread> map = new HashMap<String, ServerMessageThread>();//�������߳���Ϣ

	public Server(String name)//constracter
		{
			super(name);//public jFrame (String title);

			this.initComponents(); //initialize UI
	}
	//get ������
	public Map<String, ServerMessageThread> getMap()
	{
		return map;
	}

	public JLabel getJLabel2()
	{
		return jLabel2;
	}

	public JButton getJButton()
	{
		return jButton;
	}

	public JTextArea getJTextArea()
	{
		return jTextArea;
	}

	public JTextField getJTextField()
	{
		return jTextField;
	}

	public void setJTextField(JTextField textField)
	{
		jTextField = textField;
	}

	private void initComponents()///��ʼ��
	{
		jPanel1 = new JPanel();//�ϲ����
		jPanel2 = new JPanel();//�²����

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();

		jTextField = new JTextField(10);  //
		jButton = new JButton();   //  ��ť
		jScrollPane = new JScrollPane();  //   ���ֽ���
		jTextArea = new JTextArea(); //

		jPanel1.setBorder(BorderFactory.createTitledBorder("��������Ϣ"));//title
		jPanel1.setBackground(new Color(145,145,145));//	���ñ���ɫ
		jPanel2.setBorder(BorderFactory.createTitledBorder("�����û��б�"));  //title
		jPanel2.setBackground(new Color(192,192,192));//???
		jTextField.setText("5050"); //Ĭ�����ö˿ں�5050
		jTextField.setEditable(false);  //�������޸�

		jLabel1.setText("������״̬");  //label1 �����ı���������״̬��
		jLabel2.setText("ֹͣ");  //  label2�����ı�  ��ֹͣ��
		jLabel2.setForeground(new Color(204, 0, 51));
		jLabel3.setText("�˿ں�");  //�ı��������ı��˿ں�

		jButton.setText("����������");

		//Jbutton ��� ���������
		jButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				Server.this.execute(event);
			}
		});

		this.addWindowListener(new WindowAdapter()//�������һ��Windows�¼���Ϣ�������������Ƿ�ر�
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					Collection<ServerMessageThread> cols = Server.this.map.values();

								String messageXML = XMLUtil.constructCloseServerWindowXML();      //���ع���ķ��������ڹر���Ϣ��xml

						for(ServerMessageThread smt : cols)
						{
							smt.sendMessage(messageXML);                                //����collection��ÿ��Ԫ�أ����͸��ͻ���
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					System.exit(0);                  				//�˳�
				}
			}
		});

//��ӵ�jpane��
		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);
		jPanel1.add(jTextField);
		jPanel1.add(jButton);

		jTextArea.setEditable(false); //�������û��ֶ��޸������û��б�
		jTextArea.setRows(15);//�������У�����setbounds()
		jTextArea.setColumns(30);
		jTextArea.setForeground(new Color(0, 51, 204));

		jScrollPane.setViewportView(jTextArea);

		jPanel2.add(jScrollPane);  //��JTextArea����JScrollPane��

		this.getContentPane().add(jPanel1, BorderLayout.NORTH);//�߽粼�ַ������ݽ�����
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�رս����ʱ��رս���
		this.setAlwaysOnTop(true);//ʵ�ִ����ö�
		this.setResizable(false);//���ò����������Ĵ�С
		this.pack();//�����˴��ڵĴ�С,���ʺ������������ѡ��С�Ͳ���
		this.setLocation(500, 170);
		this.setVisible(true);//���ÿɼ�
	}

	private void execute(ActionEvent evt)      //�����ť�Ϳ�ʼ����  �˿ںź����ӷ�����
	{
		int port = Integer.parseInt(this.getJTextField().getText());//��port��ֵ������Ķ˿ںţ�

		new ServerConnection(this, port).start();  //���ӷ�����
	}

	public static void main(String[] args)
	{
		new Server("Chat Server");
	}//    chatServerr  ����

}