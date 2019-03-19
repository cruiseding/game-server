package com.jzy.game.ai.nav.triangle.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.jzy.game.ai.nav.triangle.Triangle;
import com.jzy.game.ai.nav.triangle.TriangleNavMesh;
import com.jzy.game.engine.math.Vector3;

/**
 * 地图显示面板
 *
 * @author CruiseDing
 */
public class TriangleViewPane extends JPanel {

    private static final long serialVersionUID = 1L;
    protected Image backImage;
    protected Graphics2D backImageGraphics2D;
    protected final TrianglePlayer player;

    Vector3 center = new Vector3(220, 0, 220);
    Vector3 test = new Vector3(250, 0, 250);
    Vector3 translate;
    Vector3 direction;
    Vector3 stop;
    Triangle triangle = null;
    protected boolean isRenderRandomPoints; // 是否渲染随机点
    protected boolean isShowTriangleIndex;  //是否显示三角形序号
    protected boolean isShowVectorIndex;  //是否显示坐标序号

    public TriangleViewPane(TrianglePlayer player) {
        this.player = player;
       stop = getStopPoint(center, test, 5);

    }

    public static Vector3 getStopPoint(Vector3 up, Vector3 end, float distance) {
        if (distance >= up.dst(end)) {
            return up;
        }
        return end.createPointFromAngle(end.findAngle(up), distance);
    }

    /**
     * 渲染界面
     */
    public void render() {
        if (player == null || player.getMap() == null) {
            return;
        }
        if (getWidth() <= 0 || getHeight() <= 0) {
            System.out.println(this.getClass().getSimpleName() + ": width &/or height <= 0!!!");
            return;
        }
        if (backImage == null || getWidth() != backImage.getWidth(null) || getHeight() != backImage.getHeight(null)) {
            backImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TRANSLUCENT);
        }
        backImageGraphics2D = (Graphics2D) backImage.getGraphics();
        renderWorld();
        backImageGraphics2D.dispose();
        if (getGraphics() != null) {
            getGraphics().drawImage(backImage, 0, 0, null);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    protected void renderOther(Graphics2D g) {

    }

    protected void renderWorld() {
        Graphics2D g = backImageGraphics2D;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        float backGroundGrey = 25f / 255f;
        g.setColor(new Color(backGroundGrey, backGroundGrey, backGroundGrey));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 渲染行走区
        float g4 = 0.4f;
        Color triangleColor = new Color(g4, g4, g4);
        g.setColor(triangleColor);
        for (Triangle triangle : player.getMap().getGraph().getTriangles()) {

            g.fill(triangle);

            //三角形顶点序号
            if (this.isShowTriangleIndex) {
                g.setColor(Color.GREEN);
                g.drawString(String.valueOf(triangle.index), triangle.center.x, triangle.center.z);
            }
            //坐标序号
            if (this.isShowVectorIndex&&triangle.vectorIndex!=null) {
                g.setColor(Color.BLUE);
                g.drawString(String.valueOf(triangle.vectorIndex[0]), triangle.a.x, triangle.a.z);
                g.drawString(String.valueOf(triangle.vectorIndex[1]), triangle.b.x, triangle.b.z);
                g.drawString(String.valueOf(triangle.vectorIndex[2]), triangle.c.x, triangle.c.z);
            }
            //三角形内随机点
			if (isRenderRandomPoints) {		
				g.setColor(Color.RED);
				
				Vector3 out=new Vector3();
				float count = triangle.area()/100;
				for(int i=0;i<count;i++) {
					triangle.getRandomPoint(out);
					g.fill(new Ellipse2D.Double(out.getX() - 3 / 2f, out.getZ() - 3 / 2f, 3, 3));
				}
			}
			 g.setColor(triangleColor);
        }

        //渲染寻路路线
        g.setColor(Color.LIGHT_GRAY);
        if (player.getPaths().size() > 0) {
            Vector3 currentPoint = player.getPos();
            for (int j = 0; j < player.getPaths().size(); j++) {
                Vector3 nextPoint = player.getPaths().get(j);
                g.draw(new Line2D.Double(currentPoint.getX(), currentPoint.getZ(), nextPoint.getX(), nextPoint.getZ()));
                float d = 5f;
                g.fill(new Ellipse2D.Double(nextPoint.getX() - d / 2f, nextPoint.getZ() - d / 2f, d, d));
                currentPoint = nextPoint;
            }
        }
        g.setColor(Color.ORANGE);
        g.draw(new Ellipse2D.Double(stop.x, stop.z, 3, 3));
        g.setColor(Color.red);
        double r = 5;
        g.fill(new Ellipse2D.Double(player.getPos().x - r, player.getPos().z - r, 2 * r, 2 * r));
        renderOther(g);
    }

    /**
     * @return the player
     */
    public TrianglePlayer getPlayer() {
        return player;
    }

    public TriangleNavMesh getMap() {
        return player.getMap();
    }

    /**
     * 改变随机点是否显示
     */
    public void changeShowRandomPoint() {
        isRenderRandomPoints = !isRenderRandomPoints;
    }

    public void changeShowTriangleIndex() {
        this.isShowTriangleIndex = !isShowTriangleIndex;
    }

    public void changeShowVectorIndex() {
        this.isShowVectorIndex = !isShowVectorIndex;
    }
}
