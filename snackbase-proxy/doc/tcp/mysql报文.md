# **MySQL报文**

MySql协议与众多基于TCP的应用层一样，为了解决"粘包"问题，自定义了自己的帧格式。
其主要通过Packet头部的length字段来确定整个报文的大小。

# **MySql报文的分层**
MySql报文分为两层，一层是解决"粘包"的length-body.然后body中对应不同的格式有不同的字段含义。

