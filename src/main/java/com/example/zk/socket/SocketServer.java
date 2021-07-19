package com.example.zk.socket;

import com.example.zk.DTO.ZKDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class SocketServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(8688);
        while (true) {
            Socket accept = serverSocket.accept();
            System.out.println("======收到消息======");
            InputStream inputStream = accept.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ZKDTO zk = (ZKDTO) objectInputStream.readObject();
            System.out.println(zk);
        }
    }
}
