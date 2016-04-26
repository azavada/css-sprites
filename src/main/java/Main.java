import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Cmd cmd = new Cmd(args);

        if (cmd.isOk()) {
            Runner runner = new Runner(cmd);
            runner.run();
        }
    }
}