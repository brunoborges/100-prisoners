module mathward {
    requires java.logging;
    requires java.desktop;
    requires info.picocli;
    requires me.tongfei.progressbar;
    requires org.java_websocket;

    opens prisoners;
    opens prisoners.gui;
}
