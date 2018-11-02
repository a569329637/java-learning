package com.gsq.nio.demo.sever;

import java.nio.ByteBuffer;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class Call {

    private Connection connection;
    private ByteBuffer request;
    private ByteBuffer response;

    public Call(Connection connection) {
        this.connection = connection;
        this.request = connection.getDataBuffer();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ByteBuffer getRequest() {
        return request;
    }

    public void setRequest(ByteBuffer request) {
        this.request = request;
    }

    public ByteBuffer getResponse() {
        return response;
    }

    public void setResponse(ByteBuffer response) {
        this.response = response;
    }
}
