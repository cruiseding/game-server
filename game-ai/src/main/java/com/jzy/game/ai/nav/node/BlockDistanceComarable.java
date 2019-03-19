package com.jzy.game.ai.nav.node;

import java.io.Serializable;

/**
 * 方块距离比较器
 * @author CruiseDing
 * @date 2017年12月10日 
 * @mail 359135103@qq.com
 */
public class BlockDistanceComarable implements Serializable, Comparable<BlockDistanceComarable>, Cloneable{

    private static final long serialVersionUID = 1L;
    private Block block;        //方块
    private double distance;    //距离
    
    public BlockDistanceComarable(Block block, double distance) {
        super();
        this.block = block;
        this.distance = distance;
    }

    @Override
    public int compareTo(BlockDistanceComarable o) {
        double blockDistanc = o.getDistance();
        if (this.distance > blockDistanc) {
            return 1;
        } else if (this.distance < blockDistanc) {
            return -1;
        } else {
            return 0;
        }
    }

    public Block getBlock() {
        return block;
    }

    public double getDistance() {
        return distance;
    }
    
    

}
