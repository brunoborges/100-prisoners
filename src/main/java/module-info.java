module mathward {
    requires java.logging;
    requires java.desktop;
    requires java.management;
    requires jdk.management;
    requires info.picocli;
    requires me.tongfei.progressbar;
    requires org.java_websocket;

    opens prisoners;
    opens prisoners.gui;
}
