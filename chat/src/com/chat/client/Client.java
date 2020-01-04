package com.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//�ͻ��˵�¼������
public class Client extends JFrame {
    private JButton jButton1;  //��¼��ť

    private JButton jButton2; // �˳���ť

    private JLabel jLabel1;  //�û���

    private JLabel jLabel2;  //������ip

    private JLabel jLabel3;  //�˿ں�

    private JPanel jPanel;  //�û���¼jpane

    private JTextField username;  //�û�����д����

    private JTextField hostAddress;  //������ip��д����

    private JTextField port;  //�˿ں���д����

    public Client(String name) {//������
        super(name);//�̳з���   ������ title

        initComponents(); // initialize UI
    }

    //��ʼ��
    private void initComponents() {
        jPanel = new JPanel();

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();

        username = new JTextField(15);
        hostAddress = new JTextField(15);
        port = new JTextField(15);

        jButton1 = new JButton();
        jButton2 = new JButton();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�ر�
        this.setAlwaysOnTop(true);//���ý����ö�
        this.setResizable(false);  //���ý���size�������û��ı�

        jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("�û���¼"));//����jpane��title
        //set��Ӧ��Text
        jLabel1.setText("�û���");
        jLabel2.setText("������");
        jLabel3.setText("�˿ں�");

        jButton1.setText("��¼");
        jButton2.setText("�˳�");
        //��button���һ�����������
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.this.login(e);
            }//������ʹ���ⲿ��cient�ķ���
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        username.setText("����������û���");
        hostAddress.setText("127.0.0.1");
        port.setText("5050");
        hostAddress.setEditable(false);  //���������Ų������û��༭
        port.setEditable(false);   //���ö˿ںŲ������û��༭

        jPanel.add(jLabel1);
        jPanel.add(username);
        jPanel.add(jLabel2);
        jPanel.add(hostAddress);
        jPanel.add(jLabel3);
        jPanel.add(port);

        jPanel.add(jButton1);
        jPanel.add(jButton2);

        this.getContentPane().add(jPanel);

        this.setSize(250, 200);
        this.setLocation(550, 300);
        this.setVisible(true);
    }

    //��¼����
    private void login(ActionEvent event) {
        String username = this.username.getText();//�����û���
        String hostAddress = this.hostAddress.getText();//���ط�����������
        String port = this.port.getText();//���ض˿ں�

        ClientConnection clientConnection = new ClientConnection(this,
                hostAddress, Integer.parseInt(port), username);  //���췽������ʼ������

        if (clientConnection.login()) {
            clientConnection.start();                            //��½�ɹ��������µ��߳�
        } else {
            JOptionPane.showMessageDialog(this, "�û����ظ��������δ���У�", "����",
                    JOptionPane.INFORMATION_MESSAGE);   //jOPtionpane�൯������
        }
    }

    public static void main(String[] args) {          //������
        new Client("�û���¼");
    }//�ͻ��˽��� ��������

}
