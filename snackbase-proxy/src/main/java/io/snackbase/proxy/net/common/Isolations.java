package io.snackbase.proxy.net.common;


public interface Isolations {

    int READ_UNCOMMITTED = 1;
    int READ_COMMITTED = 2;
    int REPEATED_READ = 3;
    int SERIALIZABLE = 4;

}
