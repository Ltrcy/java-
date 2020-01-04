package com.chat.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import com.chat.util.CharacterUtil;
import com.chat.util.XMLUtil;

//clientconnection {login() ,  sendmessage(), run() }
//
public class ClientConnection extends Thread//�̳ж��߳���
{
    private String hostAddress;                    //������ַ

    private int port;                                //�˿ں�

    private String username;                        //�û���

    private Client client;                            //������gui����

    private Socket socket;                        //socket����

    private InputStream is;                        //socket������

    private OutputStream os;                        //socket�����

    private ChatClient chatClient;                  //��������

    //clientconnection���췽��
    public ClientConnection(Client client, String hostAddress, int port, String username) {
        this.client = client;
        this.hostAddress = hostAddress;
        this.port = port;
        this.username = username;

        //���ӷ�����
        this.connect2Server();
    }

    // ���ӷ��������ɹ��췽������
    private void connect2Server() {
        try {
            this.socket = new Socket(this.hostAddress, this.port);

            this.is = this.socket.getInputStream();    //�����get������������
            this.os = this.socket.getOutputStream();   //������get�����������
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // �û���¼����������˴����û���
    // ����true��ʾ��¼�ɹ�
    // ����false��ʾ��¼ʧ��
    public boolean login() {
        try {
            String xml = XMLUtil.constructLoginXML(this.username);                //����username������xml��Ϣ
            System.out.println(xml.toString());                                     //����̨���xml��Ϣ
            os.write(xml.getBytes());
            // ��������˷����û��ĵ�¼��Ϣ�����а������û�����getbytes()����һ������ϵͳĬ�ϵ��ֽ�����

            byte[] buf = new byte[5000];
            int length = is.read(buf);
            // ��ȡ�������˵���Ӧ������ж��û��Ƿ��¼�ɹ�

            String loginResultXML = new String(buf, 0, length);

            String loginResult = XMLUtil.extractLoginResult(loginResultXML);

            // ��¼�ɹ�
            if ("success".equals(loginResult))      //��Ӧserverconnection  �е�islogin   ���û���ظ��û����͸ĳ�true���ҷ���true  ���򷵻�false
            {
                //��������������
                this.chatClient = new ChatClient(this);     //���������ҽ���

                this.client.setVisible(false);                        //ͬʱ���õ�½����Ϊ���ɼ�

                return true;                                       //����true
            }
            // ��¼ʧ��
            else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Socket getSocket() {
        return socket;
    }

    //�ͻ����������������Ϣ
    public void sendMessage(String message, String type) {
        try {
            int t = Integer.parseInt(type);

            String xml = null;

            //�ͻ�����������˷�����������
            if (CharacterUtil.CLIENT_MESSAGE == t)                                //����t==2������ʱ
            {
                xml = XMLUtil.constructMessageXML(this.username, message);      //����{type---2��user---name��content----message}----->����document��asxml()
            }
            //�ͻ�����������˷��͹رմ��ڵ�����
            else if (CharacterUtil.CLOSE_CLIENT_WINDOW == t) {
                xml = XMLUtil.constructCloseClientWindowXML(this.username);     //����{type--5,user---uesrname}--->����document��asxml()
            }

            //��������˷�������
            this.os.write(xml.getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //�� ����˶�ȡ��Ϣ  ->   ��ȡ����Ϣ������type ->
    @Override
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[5000];
                int length = is.read(buf);

                String xml = new String(buf, 0, length);

                int type = Integer.parseInt(XMLUtil.extractType(xml));            //��xml�н�����typeֵ    xml��ʽ{type-----��}

                //�����û��б�
                if (type == CharacterUtil.USER_LIST)                                ///4
                {
                    List<String> list = XMLUtil.extractUserList(xml);

                    String users = "";

                    for (String user : list) {
                        users += user + "\n";
                    }

                    this.chatClient.getJTextArea2().setText(users);          //���оٱ���Ϣ��ӵ�chatclient�е������û�TEXTAREA֮��
                }
                // �������˷�������������
                else if (type == CharacterUtil.SERVER_MESSAGE)     //3
                {
                    String content = XMLUtil.extractContent(xml);

                    this.chatClient.getJTextArea1().append(content + "\n");      //��ӵ��������
                }
                // �رշ������˴���
                else if (type == CharacterUtil.CLOSE_SERVER_WINDOW)   ///6
                {
                    JOptionPane.showMessageDialog(this.chatClient, "���������ѹرգ������˳���", "��Ϣ", JOptionPane.INFORMATION_MESSAGE);

                    System.exit(0); //�ͻ����˳�
                }
                // ��������ȷ�Ϲرտͻ��˴���
                else if (type == CharacterUtil.CLOSE_CLIENT_WINDOW_CONFIRMATION)             //7 �ӷ����߳���message{type==7}  �� ִ������Ĳ���
                {
                    try {
                        this.getSocket().getInputStream().close();
                        this.getSocket().getOutputStream().close();
                        this.getSocket().close();
                    } catch (Exception ex) {

                    } finally {
                        System.exit(0);//�˳��ͻ��˳���
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}	
