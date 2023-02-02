# 哮天API


# 项目简介
无后端留言系统的后端代码（哮天API）



# 项目图片 <img width="90px" src="https://pic.imgdb.cn/item/63c63337be43e0d30e841217.png">
![项目图片1-演示](https://pic.imgdb.cn/item/63c631dabe43e0d30e81d03f.png "项目图片1")

<p style="color: palevioletred">当然前端配色只是我个人审美，完全可以二次修改，甚至可以二次开发<p>

# 演示地址
https://cloud66.top/chat \
http://qaiji.gitee.io/message/

# 前端代码
<a href="https://gitee.com/qAiJi/message">进入仓库(演示)</a>

# 部署教程（普通部署）
1.需要一台服务器
2.安装 jdk11 \
3.安装nginx(并上传nginx文件夹内的配置文件) \
4.安装mysql \
6.修改配置文件 \
7.maven打包项目 \
8.上传jar包 \
9. 后台启动 nohup java -jar xxx.jar &
# 部署教程（docker部署）
待完善

# 更新日志
2023/1/5 留言查询接口增加分页 \
2023/1/8 新增登录和注册测试接口(get) \
2023/1/9 对关键字查询和查询留言的主接口进行了缓存 并对关键字查询接口进行了分页 \
2023/1/11 对部分接口加分布式锁 \
2023/1/13 优化了留言展示的顺序 \
2023/1/15 redis做了哨兵 实现redis服务故障自动切换 \
2023/1/30 对需要验证key的接口进行了域名授权访问（白名单）

## 留言
如果觉得好就给我一个小小的star吧！

