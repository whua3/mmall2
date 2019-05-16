### This is an enterprise Java web backend project
&emsp;&emsp;This project is built with SSM(Spring + SpringMVC + Mybatis) model.
This project contains several modules:
- user module
- product module
- category module
- cart module
- order module
- payment module (alipay)
- shipping module

Based on mmall-snapshot-1.0, I add redis, JsonUtil, CookieUtil, RedisShardedUtil, Spring session, Exception Resolver, Spring MVC inteceptor, distributed redis lock, redisson lock.

And I use tomcat cluster and redis cluster, and nginx for load balancing.
