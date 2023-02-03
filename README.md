# 哮天API


# 项目简介
无后端留言系统的后端代码（哮天API）

<p align="center" dir="auto">
<a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/15774e0f76266ef771f89b66139e3292f63202a8769e8740bb5db98005d5f648/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d694f532d6666363962342e737667"><img src="https://camo.githubusercontent.com/15774e0f76266ef771f89b66139e3292f63202a8769e8740bb5db98005d5f648/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d694f532d6666363962342e737667" data-canonical-src="https://img.shields.io/badge/platform-Linux-ff69b4.svg" style="max-width: 100%;"></a>
<a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/86b843cb8b8fe6258630bde4e25fcac376edf778d16af5d94b9e904b43141d3c/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d707974686f6e2d79656c6c6f77677265656e2e737667"><img src="https://camo.githubusercontent.com/86b843cb8b8fe6258630bde4e25fcac376edf778d16af5d94b9e904b43141d3c/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d707974686f6e2d79656c6c6f77677265656e2e737667" data-canonical-src="https://img.shields.io/badge/language-java-yellowgreen.svg" style="max-width: 100%;"></a>
<a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/8bc4017a2f8c506dc6c1793d2c93353b4dbdcdb49315f79873c7e581dc9e4c2e/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d7368656c6c2d677265656e2e737667"><img src="https://camo.githubusercontent.com/8bc4017a2f8c506dc6c1793d2c93353b4dbdcdb49315f79873c7e581dc9e4c2e/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d7368656c6c2d677265656e2e737667" data-canonical-src="https://img.shields.io/badge/language-shell-green.svg" style="max-width: 100%;"></a>
<a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/1f2b9cf8d7dc89db8cc4136b773051f4612409e02194737cd31ac78003fcfc94/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d4a6176615363726970742d79656c6c6f772e737667"><img src="https://camo.githubusercontent.com/1f2b9cf8d7dc89db8cc4136b773051f4612409e02194737cd31ac78003fcfc94/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d4a6176615363726970742d79656c6c6f772e737667" data-canonical-src="https://img.shields.io/badge/language-JavaScript-yellow.svg" style="max-width: 100%;"></a>
</p>

# 项目图片 <img width="70px" src="https://s1.ax1x.com/2023/02/03/pSsPWWT.png">
![img](https://s1.ax1x.com/2023/02/03/pSsPcoq.png "演示")


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

# 更新日志 <img width="30px"  src="https://github.githubassets.com/images/icons/emoji/unicode/1f4dd.png">
2023/1/5 留言查询接口增加分页 \
2023/1/8 新增登录和注册测试接口(get) \
2023/1/9 对关键字查询和查询留言的主接口进行了缓存 并对关键字查询接口进行了分页 \
2023/1/11 对部分接口加分布式锁 \
2023/1/13 优化了留言展示的顺序 \
2023/1/15 redis做了哨兵 实现redis服务故障自动切换 \
2023/1/30 对需要验证key的接口进行了域名授权访问（白名单）

## 留言 <img width='20px' src="https://github.githubassets.com/images/icons/emoji/unicode/1f618.png">
如果觉得好就给我一个小小的star吧！

