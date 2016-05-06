import org.apache.commons.cli.*;

public class Cmd {
    private CommandLine cmd = null;
    private final static Options options = createOptions();

    public Cmd(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp(options);
        }
    }

    public String getOptionValue(String opt) {
        if (cmd == null || !cmd.hasOption(opt)) {
            return "";
        }

        return cmd.getOptionValue(opt);
    }

    public int getIntegerValue(String name) {
        try {
            return  Integer.parseInt(cmd.getOptionValue(name));
        } catch (NumberFormatException ignored) {}

        return 0;
    }

    public boolean hasOptionValue(String opt) {
        return cmd.hasOption(opt);
    }

    public boolean isOk() {
        return cmd != null;
    }

    private static Options createOptions() {
        Options options = new Options();
        addRequiredOptions(options);
        addOptionalOptions(options);
        return options;
    }

    private static void addRequiredOptions(Options options) {
        options.addOption(Option.builder()
                .longOpt("dir")
                .desc("directory where to search for images")
                .argName("dir")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("img")
                .desc("path to output image")
                .argName("path")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("css")
                .desc("path to output css")
                .argName("path")
                .numberOfArgs(1)
                .required()
                .build());
    }

    private static void addOptionalOptions(Options options) {
        options.addOption(Option.builder()
                .longOpt("layout")
                .desc("specify layout orientation (vertical (default), horizontal, packed)")
                .argName("orientation")
                .numberOfArgs(1)
                .build());

        options.addOption(Option.builder()
                .longOpt("white-spacing")
                .desc("add white space (in pixels) between images to avoid overlapping")
                .argName("number of pixels")
                .numberOfArgs(1)
                .build());

        options.addOption(Option.builder()
                .longOpt("cssurl")
                .desc("specify custom string to use for css image urls (default: url(<output image path>) )")
                .argName("url")
                .numberOfArgs(1)
                .build());

        options.addOption(Option.builder()
                .longOpt("percents")
                .desc("use percents for background-position and background-size in CSS")
                .build());
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(130);
        formatter.printHelp("css-sprites [options]", options);
    }

}
