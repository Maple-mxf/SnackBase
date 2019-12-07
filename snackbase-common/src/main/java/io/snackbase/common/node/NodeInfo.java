package io.snackbase.common.node;

import java.io.Serializable;

/**
 * @author maxuefeng
 * @see io.snackbase.common.util.NetWorkUtil
 * @since 2019/12/7
 */
public class NodeInfo implements Serializable {

    protected String nodeId;
    protected String nodeName;
    protected String ip;
    protected String mac;
}
