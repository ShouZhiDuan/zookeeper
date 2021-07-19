package com.example.zk.socket;

import com.example.zk.DTO.ZKDTO;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socketClient = new Socket("127.0.0.1", 8688);
        OutputStream outputStream = socketClient.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(new ZKDTO());
    }
}
