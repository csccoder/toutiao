package com.nowcoder.util;

import redis.clients.jedis.Jedis;

public class JedisApdater {

    public static void main(String args[]) {
        new JedisApdater().run();
    }

    public void print(int index, Object o) {
        System.out.println(index + " " + o.toString());
    }

    public void run() {
        Jedis jedis = new Jedis();
        jedis.flushAll();
        jedis.set("a", "1");
        print(1, jedis.get("a"));
        jedis.incr("a");
        print(1, jedis.get("a"));
        jedis.setnx("a", "hello");
        jedis.set("b", "hello");
        print(2, jedis.get("a"));
        print(2, jedis.get("b"));

        for (int i = 1; i < 8; i++) {
            jedis.sadd("set1", String.format("%d", i));
            jedis.sadd("set2", String.format("%d", i * 2));
        }
        print(3, jedis.smembers("set1"));
        print(3, jedis.smembers("set2"));
        print(3,jedis.scard("set1"));
        print(3,jedis.scard("set2"));
        print(4,jedis.sinter("set1","set2"));
        print(5,jedis.sunion("set1","set2"));
        print(6,jedis.sdiff("set1","set2"));
        print(6,jedis.sdiff("set2","set1"));
        jedis.smove("set1","set2","1");
        print(7,jedis.smembers("set1"));
        print(7,jedis.smembers("set2"));
        print(8,jedis.sismember("set1","7"));
        print(9,jedis.srandmember("set1"));
        print(9,jedis.srandmember("set1",3));

        jedis.hset("user","name","jack");
        jedis.hset("user","age","21");
        jedis.hset("user","sex","man");
        print(10,jedis.hgetAll("user"));



    }
}
