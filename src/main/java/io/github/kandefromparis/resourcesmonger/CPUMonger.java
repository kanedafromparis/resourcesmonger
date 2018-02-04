/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.kandefromparis.resourcesmonger;

import java.util.Random;
import org.json.JSONObject;

/**
 *
 * @author csabourdin
 */
public class CPUMonger implements Runnable {

    final JSONObject message = new JSONObject();
    final String name = "";

    public void run() {
        Random ran = new Random();
        int fill = ran.nextInt(15) * 120000;
        message.put("Edge :", fill);
        int loop = 0;
        while (loop < fill) {
            loop = loop + ran.nextInt(5);
            message.put("loop", loop);
            System.out.println(message.toString());
        };
        message.put("Dying after", loop);
        System.out.println(message.toString());
    }
}
