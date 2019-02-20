package org.graalvm.graaljshoneypot.graaljs;

import org.graalvm.graaljshoneypot.MainView;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class Graaljs {

    public static final int SCRIPT_TIMEOUT = 1000;

    public static ExecutionResult eval(String script) {
        ExecutionResult result = new ExecutionResult();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Context ctx = getContext(baos);


        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ctx.close(true);
            }
        }, SCRIPT_TIMEOUT);

        long start = -System.currentTimeMillis();
        try {
            Value value = ctx.eval("js", script);
            result.returnValue = value.toString();
            result.time = Long.toString(start + System.currentTimeMillis()) + " ms";
        } catch (PolyglotException e) {
            result.time = Long.toString(start + System.currentTimeMillis()) + " ms";
            e.printStackTrace(new PrintWriter(baos));

        }
        result.output = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        return result;
    }

    private static Context getContext(ByteArrayOutputStream baos) {

        return Context.newBuilder("js")
                .allowAllAccess(false)
                .allowCreateThread(false)
                .allowHostAccess(false)
                .allowHostClassLoading(false)
                .allowIO(false)
                .allowNativeAccess(false)
                .out(baos)
                .build();

    }
}
