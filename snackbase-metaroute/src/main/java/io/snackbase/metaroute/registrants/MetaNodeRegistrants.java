package io.snackbase.metaroute.registrants;

import com.google.common.base.Joiner;
import io.jopen.json.Json;
import io.snackbase.common.Configuration;
import io.snackbase.common.registrants.BaseZKPathRegistrants;
import io.snackbase.common.registrants.Registrants;
import io.snackbase.common.util.NetWorkUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * meta node info registry
 * {@link io.snackbase.common.registrants.BaseZKPathRegistrants#META_NODE_BASE_PATH}
 *
 * @author maxuefeng
 * @since 2019/12/8
 */
public class MetaNodeRegistrants extends Registrants {

    // meta node Id (unique)
    private final String metaNodeId = Configuration.get("meta.node.id").toString();

    public MetaNodeRegistrants() throws IOException {
    }

    /**
     * init base path zookeeper node
     */
    @Override
    public void initializationBasePath() throws Exception {

        // registry current node info
        // ip mac alive
        registry(Joiner.on("/").join(new String[]{BaseZKPathRegistrants.META_NODE_BASE_PATH, this.metaNodeId}),
                this.getMetaRouteNodeInfo(),
                CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    /**
     * @return bytes;the current system network configuration
     * @throws Exception ex
     * @see Configuration
     */
    private byte[] getMetaRouteNodeInfo() throws Exception {

        // unique tag
        String mac = NetWorkUtil.getAddress("mac");
        // Intranet ip address
        String intranetIp = NetWorkUtil.getAddress("ip");
        // Extranet ip address
        String extranetIp = String.valueOf(Configuration.get("bind_ip"));
        extranetIp = (extranetIp.equals("null") || StringUtils.isBlank(extranetIp)) ? intranetIp : extranetIp;

        return Json.of("mac", mac, "intranetIp", intranetIp, "extranetIp", extranetIp).toJSONString().getBytes(StandardCharsets.UTF_8);
    }
}
