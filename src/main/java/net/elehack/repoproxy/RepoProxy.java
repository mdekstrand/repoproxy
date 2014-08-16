package net.elehack.repoproxy;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RepoProxy implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RepoProxy.class);

    @Arg(dest="config_file")
    File configFile;

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("repoproxy");
        parser.addArgument("config-file", "c")
                .metavar("FILE")
                .type(File.class)
                .required(true)
                .help("read configuration from FILE");

        RepoProxy proxy = new RepoProxy();
        try {
            parser.parseArgs(args, proxy);
        } catch (ArgumentParserException e) {
            System.err.println("error parsing command line");
            System.err.println(e.getMessage());
            System.exit(2);
        }
        proxy.run();
    }

    @Override
    public void run() {
        ServerConfiguration config = ServerConfiguration.load(configFile);
    }
}
