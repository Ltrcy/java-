package com.chat.client;

import com.chat.server.Server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//�������� ��½	��������Ҫ����˺Ϳͻ��˴������ӣ�
//���췽����Ҫ��ʼ��cilentconnection��chatclient����
//��������������gui����ر���Ҫ����sendmessage()����������chatlient�رյ���Ϣ����2. ���Ͱ�ť������
public class ChatClient extends javax.swing.JFrame {
    private javax.swing.JButton jButton1;   //���Ͱ�ť
    private javax.swing.JButton jButton2;   //������ť
    private javax.swing.JPanel jPanel1;     //��������Ϣjpane
    private javax.swing.JPanel jPanel2;    //�����û�jpane
    private JPanel jPanel3;                //��Ϣ��������������ť��jpane
    private javax.swing.JScrollPane jScrollPane1;     //��ӹ��ֽ���
    private javax.swing.JScrollPane jScrollPane2;     //��ӹ���
    private javax.swing.JTextArea jTextArea1;         //��������Ϣ
    private javax.swing.JTextArea jTextArea2;         //�����û���Ϣ
    private javax.swing.JTextField jTextField;        //��Ϣ������

    private ClientConnection clientConnection;        //new һ���ͻ���������Ķ���


    public ChatClient(ClientConnection clientConnection)    //��������ʼ������
    {
        this.clientConnection = clientConnection;

        initComponents();   //��ʼ��
    }

    //����˽�б���
    public JTextArea getJTextArea2() {
        return jTextArea2;
    }

    public JTextArea getJTextArea1() {
        return jTextArea1;
    }

    //��ʼ������
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();    //��������ϢJpane
        jScrollPane1 = new javax.swing.JScrollPane();   //��������ϢjScrollPane
        jTextArea1 = new javax.swing.JTextArea();       //��������Ϣ
        jTextField = new javax.swing.JTextField(20);   //��Ϣ������
        jButton1 = new javax.swing.JButton();           //���Ͱ�ť
        jButton2 = new javax.swing.JButton();           //������ť
        jPanel2 = new javax.swing.JPanel();             //�û�����Jpane
        jScrollPane2 = new javax.swing.JScrollPane();   //�û����߽����SCrollpane
        jTextArea2 = new javax.swing.JTextArea();       //�����û���Ϣ

        jPanel3 = new JPanel();                        //��Ϣ��������������ť��Jpane

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //�ر�gui���˳�����
        this.setTitle("������");                           //���ñ��⡰�����ҡ�
        setResizable(false);                               //���ò��ɸı�ߴ�
        jPanel1.setBorder(BorderFactory.createTitledBorder("��������Ϣ"));         //����jpane1����
        jPanel2.setBorder(BorderFactory.createTitledBorder("�����û��б�"));       //����jpane2����
        jTextArea1.setColumns(30);
        jTextArea1.setRows(25);
        //��������
        jTextArea2.setColumns(20);
        jTextArea2.setRows(25);
        //��������
        this.jTextArea1.setEditable(false);
        this.jTextArea2.setEditable(false);
        //��������JtextArea��Ϊ�������û��޸�
        jPanel3.add(jTextField);
        jPanel3.add(jButton1);
        jPanel3.add(jButton2);

        jPanel1.setLayout(new BorderLayout());      //���ñ߽粼��
        jPanel1.add(jScrollPane1, BorderLayout.NORTH);
        jPanel1.add(jPanel3, BorderLayout.SOUTH);   //����jpane3��ӵ�jpane1��north����

        jPanel2.add(jScrollPane2);                  //jpanel2��� jSCRollpanel2
        //��������jpanel��Ϊ�ö�״̬
        jScrollPane1.setViewportView(jTextArea1);
        jScrollPane2.setViewportView(jTextArea2);

        jButton1.setText("����");         //����jbutton1�ı���Ϊ�����͡�
        jButton2.setText("����");         //����jbutton2�ı���Ϊ��������

        //button1�������������Ƿ��·��Ͱ�ť
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChatClient.this.sendMessage(e);
            }   //������˰�ť�͵����ⲿ���sendmeaage()����
        });

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChatClient.this.clear(e);


            }
        });


        //���window���������gui����ر��ˣ��ͷ���"client closed"
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ChatClient.this.clientConnection.sendMessage("client closed", "5");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //�������jpanel
        this.setLayout(new FlowLayout());
        this.getContentPane().add(jPanel1);
        this.getContentPane().add(jPanel2);


        this.pack();                                        //�Զ���������װ��jpanel
        this.setVisible(true);                              //����Ϊ�ɼ�״̬
    }


    private void clear(ActionEvent event) {
        this.jTextArea1.setText("");
    }

    //sendmessage����
    private void sendMessage(ActionEvent event) {
        // �û����������
        String message = this.jTextField.getText();
        // �����������
        this.jTextField.setText("");
        // ��������˷�����������
        this.clientConnection.sendMessage(message, "2");
    }


}
