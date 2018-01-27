/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.kandefromparis.ressourcesmonger;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.json.JSONObject;

/**
 *
 * @author csabourdin
 */
public class DinnerTimeServer {

    public static void main(String[] args) throws IOException {

        int port = 8080;

// tag::start-server[]
        HttpServer server
                = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new KRootHandler());
        server.setExecutor(null);
        server.start();
// end::start-server[]
    }
    static final Map m = new HashMap();
    static int nbUser = 0;

    static class KRootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath().trim();
            System.out.println("[access] " + path);
            if (path.equals("/api/0.0.1/kaboom/ram")) {
                getKaboomRam(t);
                return;
            }
            if (path.equals("/api/0.0.1/kaboom/cpu")) {
                getKaboomCpu(t);
                return;
            }
            if (path.equals("/api/0.0.1/infos/env")) {
                getInfoEnv(t);
                return;
            }
            if (path.equals("/api/0.0.1/infos/runtime")) {
                getInfoRuntime(t);
                return;
            }
            if (path.equals("/liveness")) {
                getLivenessPage(t);
                return;
            }
            if (path.equals("/readiness")) {
                getReadinessPage(t);
                return;
            }
            if (path.equals("/")) {
                getDefaultPage(t);
                return;
            }
            getDefaultPage(t);
            return;

        }

        private void getDefaultPage(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            StringBuilder sb = new StringBuilder();
            sb.append("<html><title>Kaboom server</title><body>");
            sb.append("<div><h3>Kaboom server</h3>This server is intended to crash all the memory of the host if you go to <a href=\"/kaboom\">this links<a><div>");
            t.sendResponseHeaders(200, sb.length());
            os.write(sb.toString().getBytes());
            os.close();
        }

        private void getLivenessPage(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            JSONObject message = new JSONObject();
            message.put("status", "ok");
            message.put("HashMapSize", m.size());

            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
        }

        private void getReadinessPage(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            JSONObject message = new JSONObject();
            message.put("status", "ok");
            message.put("HashMapSize", m.size());

            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
        }

        /**
         *
         * @param t
         * @throws IOException
         * @see
         * http://alvinalexander.com/blog/post/java/java-program-consume-all-memory-ram-on-computer
         */
        private void getInfoEnv(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            JSONObject message = new JSONObject();
            Enumeration<Object> keys = System.getProperties().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = System.getProperties().get(key);
                message.put("properties java:" + String.valueOf(key), String.valueOf(value));
            }

            Iterator<String> keys_ = System.getenv().keySet().iterator();
            while (keys_.hasNext()) {
                Object key = keys_.next();
                Object value = System.getenv().get(key);

                message.put(String.valueOf(key), String.valueOf(value));
            }

            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
        }

        private void getInfoRuntime(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            JSONObject message = new JSONObject();

            Runtime runtime = Runtime.getRuntime();
            NumberFormat format = NumberFormat.getInstance();
            StringBuilder sb = new StringBuilder();
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();

            message.put("free memory", format.format(freeMemory / 1024));
            message.put("allocated memory", format.format(allocatedMemory / 1024));
            message.put("max memory", format.format(maxMemory / 1024));
            message.put("total free memory", format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
            message.put("available processors", String.valueOf(runtime.availableProcessors()));

            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
        }

        /**
         *
         * @param t
         * @throws IOException
         * @see
         * http://alvinalexander.com/blog/post/java/java-program-consume-all-memory-ram-on-computer
         */
        private void getKaboomCpu(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            JSONObject message = new JSONObject();

            message.put("title", "Not Yet");
            message.put("message", "@todo");

            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
        }

        /**
         *
         * @param t
         * @throws IOException
         * @see
         * http://alvinalexander.com/blog/post/java/java-program-consume-all-memory-ram-on-computer
         */
        private void getKaboomRam(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            JSONObject message = new JSONObject();

            message.put("title", "AH AH AH");
            message.put("message", "RamMonger started");

            t.sendResponseHeaders(200, message.toString().length());
            os.write(message.toString().getBytes());
            os.close();
// tag::memory-monger[]

            int i = 0, j = 0;
            Random ran = new Random(10);
            int fill = ran.nextInt(15) * 120000;
            int stack = 0;
            while (true) {
                i++;
                j++;
                if (i > 10000) {
                    i = 0;
                    // the Thread.sleep is here in order to allows
                    // to see the docker host swapping
                    try {
                        Thread.sleep(10);                 //1000 milliseconds is one second.
                        stack = stack + fill;
                        byte b[] = new byte[stack];
                        Object put = m.put(j, b);
                        Runtime runtime = Runtime.getRuntime();
                        NumberFormat format = NumberFormat.getInstance();
                        StringBuilder sb = new StringBuilder();
                        long maxMemory = runtime.maxMemory();
                        long allocatedMemory = runtime.totalMemory();
                        long freeMemory = runtime.freeMemory();
                        message.put("free memory", format.format(freeMemory / 1024));
                        message.put("allocated memory", format.format(allocatedMemory / 1024));
                        message.put("max memory", format.format(maxMemory / 1024));
                        System.out.println(message.toString());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

            }

        }
    }
}
