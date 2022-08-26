# 学习若依框架 ratelimiter 依赖
思路:

1、我需要一个注解@RateLimiter在HelloController的hello方法上加上就能帮我完成限流操作,如10s,限流3次,我可能还需要针对IP进行限流

2、我需要一个地方记录该接口的访问次数——>redis

3、我需要客户端访问到接口的时候获取到该接口的唯一标识:【IP】+类名+方法名，作为key->IpUtils工具类+java反射机制

4、我需要保证给每个key统计次数时保证count值的原子性-->lua脚本
