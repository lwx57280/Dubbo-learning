package com.learn.dubbo;

import com.alibaba.dubbo.container.Main;

/**
 * 通过dubbo提供 main方法启动容器
 *
 * @Author: cong zhi
 * @CreateDate: 2021/2/12 10:48
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/12 10:48
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class DubboMain {

    public static void main(String[] args) {
        // 默认情况下会使用spring容器来启动服务
        Main.main(new String[]{"spring","log4j"});
    }
}
