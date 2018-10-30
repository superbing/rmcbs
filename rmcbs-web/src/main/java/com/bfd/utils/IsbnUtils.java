package com.bfd.utils;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 17:55 2018/10/22
 * @Modified by:
 */
public class IsbnUtils {


    /**
     * ISBN固定前缀,以978-7为前缀的
     */
    private static final String FIXED_PREFIX_9787 = "9787";


    /**
     * ISBN固定前缀,以7-为前缀的
     */
    private static final String FIXED_PREFIX_7 = "7";

    /**
     * 校验ISBN
     *
     * @param isbn 传入ISBN 例子: 978-7-115-42866-0
     * @return
     */
    public static boolean checkISBN(String isbn) {

        //判断是否是标准的ISBN

        int i11 = isbn.length();
        //先判断字符长度对不对
        if(isbn.length() == 17 || isbn.length() == 13 ){
            //对13位的进行校验
            if(isbn.length() == 17){
                //用"-"拆分出的数组
                String[] isbns = isbn.split("-");
                //判断数组长度
                if(isbns.length == 5){
                    //对各个数组的位数进行校验
                    if(isbns[0].length() != 3 ){
                        return false;
                    }
                    if(isbns[1].length() != 1 ){
                        return false;
                    }
                    if(isbns[2].length() + isbns[3].length() != 8 ){
                        return false;
                    }
                    if(isbns[4].length() != 1 ){
                        return false;
                    }
                }else {
                    return false;
                }
                //对10位的进行校验
            }else {
                //用"-"拆分出的数组
                String[] isbns = isbn.split("-");
                //判断数组长度
                if(isbns.length == 4){
                    //对各个数组的位数进行校验
                    if(isbns[0].length() != 1 ){
                        return false;
                    }
                    if(isbns[1].length() + isbns[2].length() != 8 ){
                        return false;
                    }
                    if(isbns[3].length() != 1 ){
                        return false;
                    }
                }else {
                    return false;
                }
            }

        }else {
            return false;
        }


        //将ISBN去掉"-"
        isbn = isbn.replace("-","");
        //前12位数字
        String frontStr = isbn.substring(0, isbn.length() - 1);
        //校验位
        String backStr = isbn.substring(isbn.length() - 1);
        boolean isNum = frontStr.matches("[0-9]+");
        if (!isNum || !(frontStr.length() == 9 || frontStr.length() == 12)) {
            return false;
        }
        char[] tmp = frontStr.toCharArray();
        int sum = 0;
        int count = 10;
        //验证10位的ISBN 假设某ISBN号码前9位是：730904547
        if (frontStr.length() == 9) {
            //验证前缀是否正确
            String str = isbn.substring(0, 1);
            if (!str.equals(FIXED_PREFIX_7)) {
                return false;
            }
            //计算加权和S：S＝7×10＋3×9＋0×8＋9×7＋0×6＋4×5＋5×4＋4×3＋7×2 = 226
            for (int i = 0; i < 9; i++) {
                int dd = Integer.parseInt(tmp[i] + "");
                sum = sum + count * dd;
                count--;

            }
            //计算S÷11的余数M：M = 226 mod 11 = 6
            int n = 11 - sum % 11;
            //计算11－M的差N：N = 11 ? 6 = 5
            //?如果N＝10，校验码是字母“X”
            //?如果N＝11，校验码是数字“0”
            //?如果N为其他数字，校验码是数字N。
            String s = "";
            if (n == 11) {
                s = "0";
            } else if (n == 10) {
                s = "x";
            } else {
                s = "" + n;
            }
            //判断校验码是否和计算结果一样 5
            if (backStr.toLowerCase().equals(s)) {
                return true;
            } else {
                return false;
            }
            //验证13位的ISBN 假设某13位ISBN号码前12位是：987730904547
        } else if (frontStr.length() == 12) {
            //验证前缀是否正确
            String str = isbn.substring(0, 4);
            if (!str.equals(FIXED_PREFIX_9787)) {
                return false;
            }
            //计算加权和S：S＝9×1＋8×3＋7×1＋7×3＋3×1＋0×3＋9×1＋0×3＋4×1＋5×3＋4×1＋7×3 = 117(奇数乘1偶数乘3)
            for (int i = 0; i < 12; i++) {
                int dd = Integer.parseInt(tmp[i] + "");
                if (i % 2 == 0) {
                    sum = sum + 1 * dd;
                } else {
                    sum = sum + 3 * dd;
                }
            }
            //计算出的校验码
            String s = "";
            //计算S÷10的余数M：M = 117 mod 10 = 7
            //计算10－M的差N：N = 10 ? 7 = 3 (如果10－M的值为10则校验码取0)
            int m = sum % 10;
            int n = 10 - m;
            if(n == 10){
                s = "0";
            }else {
                s = String.valueOf(n);
            }
            //判断校验码是否和计算结果一样 3
            if (backStr.equals(s)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }




    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {

        boolean result = IsbnUtils.checkISBN("978-7-11-005423-0");

        System.out.println("ISBN结果:" + result);

    }


}
