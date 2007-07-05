/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.recorder;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Simulator {
    public static void main(String[] args) {
        int index = 0;
        boolean fail = false;

        float delay = 1.0f;
        String auth = "BASIC";
        String userid = "user ";
        String password = null;
        int iterations = 1;
        int ramp = 0;

        try {
            while (index < args.length) {
                if ("-d".equals(args[index]) || "--delay".equals(args[index]))
                    delay = new Float(args[index + 1]).floatValue();
                else if ("-a".equals(args[index]) || "--auth".equals(args[index]))
                    auth = args[index + 1];
                else if ("-u".equals(args[index]) || "--userid".equals(args[index]))
                    userid = args[index + 1];
                else if ("-p".equals(args[index]) || "--password".equals(args[index]))
                    password = args[index + 1];
                else if ("-i".equals(args[index]) || "--iterations".equals(args[index]))
                    iterations = new Integer(args[index + 1]).intValue();
                else if ("-r".equals(args[index]) || "--ramp".equals(args[index]))
                    ramp = new Integer(args[index + 1]).intValue();
                else if ("-o".equals(args[index]) || "--output".equals(args[index]))
                    System.setOut(new PrintStream(new FileOutputStream(args[index + 1])));
                else
                    break;

                index += 2;
            }
        } catch (Exception e) { fail = true; }

        if (fail || args.length < index + 2) {
            printUsage();
            System.exit(1);
        }

        String url = args[index];
        String scriptClassName = args[index + 1];

        int firstUser;
        int lastUser;
        int pos = args[index + 2].indexOf("-");
        if (pos > -1) {
            String a = args[index + 2].substring(0, pos);
            String b = args[index + 2].substring(pos + 1);
            firstUser = Integer.valueOf(a).intValue();
            lastUser = Integer.valueOf(b).intValue();
        } else {
            firstUser = 1;
            lastUser = Integer.valueOf(args[index + 2]).intValue();
        }

        try {
            Class scriptClass = Class.forName(scriptClassName);

            System.out.println("INFO: initializing clients for " +
                    userid + firstUser + " to " + userid + lastUser);
            List clients = new LinkedList();
            for (int i = firstUser; i <= lastUser; i++) {
                Script script = (Script) scriptClass.newInstance();
                script.setUrl(url);
                script.setDelay(delay);

                Client client = new Client();
                client.init(script);
                client.setUserid(userid + i);
                client.setPasswd(password);
                client.setInitialDelay((i - firstUser) * ramp);
                client.setIterations(iterations);
                clients.add(client);
            }

            System.out.println("INFO: starting clients");
            Iterator it = clients.iterator();
            while (it.hasNext()) {
                Client client = (Client) it.next();
                client.start();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    protected static void printUsage() {
        System.err.println("usage: simulator [options] url script_class user_range");
        System.err.println("arguments:");
        System.err.println("  url              url of application client jar with deployment descriptors");
        System.err.println("  script_class     full qualified classname");
        System.err.println("  user_range       1-'lastUser' or 'firstUser'-'lastUser'");
        System.err.println("options:");
        System.err.println("  -d, --delay      [0..t] factor, that is applied to the recorded delays");
        System.err.println("                   0.0 for no delays, 1.0 for same tempo as during recording (default)");
        System.err.println("  -a, --auth       the auth type, one of HTTP, JBoss (more to follow), defaults to HTTP");
        System.err.println("  -u, --userid     the base userid, to which firstUser .. lastUser will be appended, defaults to 'demo'");
        System.err.println("  -p, --password   the password for all users");
        System.err.println("  -i, --iterations number of iterations a virtual user runs the script, defaults to 'start'");
        System.err.println("  -r, --ramp       load ramp, n'th client is started after n times the delay in ms, default is 0");
        System.err.println("  -o, --output     redirect output to a file");
    }
}


