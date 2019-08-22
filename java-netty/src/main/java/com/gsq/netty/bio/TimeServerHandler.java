package com.gsq.netty.bio;

import com.gsq.netty.utils.DateTimeUtils;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * @author guishangquan
 * @date 2019-08-22
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String resp;
            while (true) {
                String body = in.readLine();
                if (body == null) {
                    break;
                }

                System.out.println("The time server receive order: " + body);
                if ("QUERY TIME ORDER".equalsIgnoreCase(body)) {
                    LocalDateTime now = LocalDateTime.now();
                    resp = DateTimeUtils.formatLocalDateTime(now);
                } else {
                    resp = "BAD ORDER";
                }
                out.println(resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
