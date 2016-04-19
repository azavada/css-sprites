import org.apache.commons.cli.*;

public class Cmd {
    private CommandLine cmd = null;

    public Cmd(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("i")
                .longOpt("input-dir")
                .desc("Directory where to search for images")
                .argName("dir")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder("o")
                .longOpt("output-dir")
                .desc("Directory where to save css and image")
                .argName("dir")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("layout")
                .desc("specify layout orientation (horizontal, vertical, packed)")
                .argName("orientation")
                .numberOfArgs(1)
                .build());

        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            printHelp(options);
        }
    }

    public String getOptionValue(String opt) {
        return cmd == null ? "" : cmd.getOptionValue(opt);
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("css-sprites", options);
    }

}
