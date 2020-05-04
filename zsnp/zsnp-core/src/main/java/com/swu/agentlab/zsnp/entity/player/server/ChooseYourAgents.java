package com.swu.agentlab.zsnp.entity.player.server;


import java.util.Arrays;

public class ChooseYourAgents {

    /* 原M个数据数组 */
    private Object[] src;

    /* src数组的长度 */
    private int m;

    /* 需要获取N个数 */
    private int n;

    //临时变量，obj的行数
    private int objLineIndex;

    /* 存放结果的二维数组 */
    public Object[][] obj;

    public ChooseYourAgents(Object[] src, int getNum) throws Exception {
        if (src == null)
            throw new Exception("原数组为空.");
        if (src.length < getNum)
            throw new Exception("要取的数据比原数组个数还 大 .");
        this.src = src;
        m = src.length;
        n = getNum;

        /*  初始化  */
        objLineIndex = 0;
        obj = new Object[combination(m,n)][n];

        Object[] tmp = new Object[n];
        combine(src, 0, 0, n, tmp);
    }

    /**
     * <p>
     * 计算 C(m,n)个数 = (m!)/(n!*(m-n)!)
     * </p>
     * 从M个数中选N个数，函数返回有多少种选法 参数 m 必须大于等于 n m = 0; n = 0; retuan 1;
     *
     * @param m
     * @param n
     * @return
     */
    public int combination(int m, int n) {
        if (m < n)
            return 0; // 如果总数小于取出的数，直接返回0

        int k = 1;
        int j = 1;
        // 该种算法约掉了分母的(m-n)!,这样分子相乘的个数就是有n个了
        for (int i = n; i >= 1; i--) {
            k = k * m;
            j = j * n;
            m--;
            n--;
        }
        return k / j;
    }

    /**
     * <p> 递归算法，把结果写到obj二维数组对象 </p>
     * @param src
     * @param srcIndex
     * @param i
     * @param n
     * @param tmp
     */
    private void combine(Object src[], int srcIndex, int i, int n, Object[] tmp) {
        int j;
        for (j = srcIndex; j < src.length - (n - 1); j++ ) {
            tmp[i] = src[j];
            if (n == 1) {
                //System.out.println(Arrays.toString(tmp));
                System.arraycopy(tmp, 0, obj[objLineIndex], 0, tmp.length);
                //obj[objLineIndex] = tmp;
                objLineIndex ++;
            } else {
                n--;
                i++;
                combine(src, j+1, i, n, tmp);
                n++;
                i--;
            }
        }

    }

    public Object[][] getResutl() {
        return obj;
    }


    public static void main(String[] args) throws Exception {
//        String[] a = new String[]{"1about","2bb","3cc"};
        Integer[] a = new Integer[]{1,2,3};
        ChooseYourAgents ca = new ChooseYourAgents(a, 3);

        Object[][] c = ca.getResutl();
        for (int i = 0; i < c.length; i++) {
            System.out.println(Arrays.toString(c[i]));
        }
    }



}
