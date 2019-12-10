package io.snackbase.protocol;

/**
 * 
 * <pre><b>proxy's version.</b></pre>
 * @version 1.0
 */
public interface Versions {

	byte PROTOCOL_VERSION = 10;
	byte[] SERVER_VERSION = "m2o-proxy-5.6.0-snapshot".getBytes();
}