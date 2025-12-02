module DataHammer {
    requires javafx.controls;
    requires javafx.graphics;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;

    requires static lombok;

    exports pers.ryoko.gui;
}
