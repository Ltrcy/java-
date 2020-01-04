package com.chat.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.chat.util.XMLUtil;

//serverconnection�̳ж��߳���
//��������������Ҫserver��
public class ServerConnection extends Thread {
    private ServerSocket serverSocket;

    private Server server;

    public ServerConnection(Server server, int port) {
        try {
            this.server = server;

            this.serverSocket = new ServerSocket(port);

            // �������д��벻�ܷŵ�����һ�д���֮ǰ
            // ��Ϊ����������ʱ�п��ܻ��׳��쳣
            // ����������棬�����쳣ʱҲ�ᵼ��label�ϵ��ı������仯
            // ����ʵ���������
            this.server.getJLabel2().setText("����");         //�ı�״̬ ����״̬
            this.server.getJButton().setEnabled(false);       //���á�������������ťΪ������״̬��
        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(this.server, "�˿ںű�ռ�ã�", "����", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();           //socket���ӷ���

                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                byte[] buf = new byte[5000];
                int length = is.read(buf);                   //��ȡ�ӿͻ��˷������ֽ���

                //�ͻ��˷�����������Ϣ���������û�����
                String loginXML = new String(buf, 0, length);           //ת����char���͵�

                // �ӿͻ��˵�¼��������ȡ���û�����Ϣ��username��
                String username = XMLUtil.extractUsername(loginXML);

                String loginResult = null;

                // �ж��û��Ƿ��¼�ɹ�
                boolean isLogin = false;

                // �ж��û����Ƿ��ظ�
                // �û����ظ�
                if (this.server.getMap().containsKey(username))     //�����ⲿ��server�е�getmap()ȡ���Ƿ�������username
                {
                    loginResult = "failure";
                }
                // �û������ظ�
                else {
                    loginResult = "success";

                    isLogin = true;
                }

                String xml = XMLUtil.constructLoginResultXML(loginResult);       ///����result��xmlString���͸�clientconnection

                os.write(xml.getBytes());     //���͸��ͻ��� connection  loginresult

                // ����û���¼�ɹ��������߳�
                if (isLogin) {
                    // ׼�������µ��̣߳����ڴ����û����������ݣ�ÿһ�������ϵ��û������Ӧһ�����߳�
                    ServerMessageThread serverMessageThread = new ServerMessageThread(this.server, socket);
                    //���û�������֮��Ӧ���̶߳��� �ŵ�Map��
                    this.server.getMap().put(username, serverMessageThread);

                    //�����û��б���������+�ͻ��ˣ�
                    serverMessageThread.updateUserList();

                    serverMessageThread.start();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
