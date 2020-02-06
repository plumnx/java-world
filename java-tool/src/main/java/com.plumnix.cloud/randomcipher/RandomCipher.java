package com.plumnix.cloud.randomcipher;

import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomCipher {

    @Test
    public void randomNumber() {
        StringBuilder code = new StringBuilder();
        Random rand = new Random();//生成随机数
        StringBuilder newCode = code;
        IntStream.range(0, 6).forEach(value -> {
            newCode.append(rand.nextInt(10));
        });

        System.out.println("randomNumber: " + newCode.toString());
    }

    @Test
    public void randomNumberAndAlpha() {
        System.out.println(stringRandom(16));
    }

    private String stringRandom(int length) {
        String val = "";
        Random random = new Random();
        //length为几位密码
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}
