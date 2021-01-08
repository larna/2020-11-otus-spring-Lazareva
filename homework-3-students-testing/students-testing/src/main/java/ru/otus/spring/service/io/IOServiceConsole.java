package ru.otus.spring.service.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Сервис ввода/вывода
 */
public class IOServiceConsole implements IOService {
    private static final Logger logger = LoggerFactory.getLogger(IOServiceConsole.class);
    private final BufferedReader in;
    private final PrintStream out;

    public IOServiceConsole(InputStream in, PrintStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = out;
    }

    @Override
    public void close() {
        try {
            this.in.close();
        } catch (IOException e) {
            logger.error("Close error", e);
        }
    }

    @Override
    public String readMessage() throws IOException {
        return in.readLine();
    }

    @Override
    public void outMessage(String message) {
        out.println(message);
    }
}
