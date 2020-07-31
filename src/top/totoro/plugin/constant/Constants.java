package top.totoro.plugin.constant;

public class Constants {
    public static final String SWING_FILE_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n";
    public static final String DEFAULT_SWING_FILE_CONTENT = SWING_FILE_HEADER + "<LinearLayout\n" +
            "        id=\"main_linear_layout\"\n" +
            "        width=\"match_parent\"\n" +
            "        height=\"match_parent\"\n" +
            "        orientation=\"vertical\">\n" +
            "        <TextView\n" +
            "                width=\"match_parent\"\n" +
            "                height=\"match_parent\"\n" +
            "                textColor=\"#515151\"\n" +
            "                text=\"Hello World\"/>\n" +
            "</LinearLayout>";
    public static final String DEFAULT_MAIN_ACTIVITY_CONTENT = "package ui;\n" +
            "\n" +
            "import swing.R;\n" +
            "import top.totoro.swing.widget.context.Activity;\n" +
            "\n" +
            "import java.awt.*;\n" +
            "\n" +
            "public class MainActivity extends Activity {\n" +
            "    @Override\n" +
            "    public void onCreate() {\n" +
            "        super.onCreate();\n" +
            "        setContentView(R.layout.activity_main);\n" +
            "        startActivity(this, TargetActivity.class);\n" +
            "    }\n" +
            "\n" +
            "    public static void main(String[] args) {\n" +
            "        /* 可以指定窗体的大小 */\n" +
            "        newInstance(new Dimension(500, 500)).startActivity(MainActivity.class);\n" +
            "    }\n" +
            "\n" +
            "    private static class TargetActivity extends Activity {\n" +
            "        @Override\n" +
            "        public void onCreate() {\n" +
            "            super.onCreate();\n" +
            "            setContentView(null);\n" +
            "        }\n" +
            "    }\n" +
            "}\n";
}
