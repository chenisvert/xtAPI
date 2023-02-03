<h1 align="center" dir="auto"><a id="user-content-vctgo-platform-哮天犬API" class="anchor" aria-hidden="true" href="#vctgo-platform-哮天Api"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z"></path></svg></a>无后端留言系统的后端代码（哮天API）
</h1>
<p align="center" dir="auto">
<img src="https://img.shields.io/badge/platform-Linux-ff69b4.svg" style="max-width: 100%;"></a>
<img src="https://img.shields.io/badge/language-java-yellowgreen.svg" style="max-width: 100%;"></a>
<a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/8bc4017a2f8c506dc6c1793d2c93353b4dbdcdb49315f79873c7e581dc9e4c2e/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d7368656c6c2d677265656e2e737667"><img src="https://camo.githubusercontent.com/8bc4017a2f8c506dc6c1793d2c93353b4dbdcdb49315f79873c7e581dc9e4c2e/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c616e67756167652d7368656c6c2d677265656e2e737667" data-canonical-src="https://img.shields.io/badge/language-shell-green.svg" style="max-width: 100%;"></a>
<img src="https://img.shields.io/badge/language-Vue-yellow.svg" style="max-width: 100%;"></a>
</p>
<img src="https://img.shields.io/badge/QQ-2726225713-orange.svg?style=flat-square" style="max-width: 100%;">

## 模块介绍
<pre class="notranslate"><code>com.api.freeapi     
├── api         //调用外部API模块
├── controller            // 外部暴露后端接口模块 [39200]
├── common             // 通用模块
├── config          // 配置模块
├── entity          // 实体类模块
├── filter          // 拦截器模块
├── job          // 系统任务模块
├── mapper          // 数据操作模块
├── service          // 业务模块
├── utils          // 工具类模块
├── service          // 业务模块
</code></pre>

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
<p dir="auto"><g-emoji class="g-emoji" alias="warning" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/26a0.png">⚠</g-emoji> 正在施工中···</p>

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
# LICENSE
<blockquote>
<p dir="auto">此项目仅为学习,请勿用作商业用途,否则后果自负！</p>
</blockquote>
