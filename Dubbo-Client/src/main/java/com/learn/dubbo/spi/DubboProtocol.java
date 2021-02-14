package com.learn.dubbo.spi;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 创建扩展RPC协议接口实现类：
 *
 * @Author: cong zhi
 * @CreateDate: 2021/2/13 20:09
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/13 20:09
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class DubboProtocol implements Protocol {

    @Override
    public int getDefaultPort() {
        return 8888;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> aClass, URL url) throws RpcException {
        return null;
    }

    @Override
    public void destroy() {

    }
}
