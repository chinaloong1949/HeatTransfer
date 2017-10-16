/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Curve;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author yk
 */
public class Java3DFactory extends JPanel {

    private BoundingSphere bounds1;
    private BoundingSphere bounds2;
    private Canvas3D canvas;
    private SimpleUniverse universe;

    public Java3DFactory() {
        System.out.println("Java3DFactory");
        this.setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        canvas.setFocusable(true);
        canvas.requestFocus();
        this.add(canvas, BorderLayout.CENTER);
        bounds1 = new BoundingSphere(new Point3d(0, 0, 0), 10);
        bounds2 = new BoundingSphere(new Point3d(0, 0, 0), 100);

        BranchGroup scene = createSceneGraph();
        scene.compile();

        universe = new SimpleUniverse(canvas);
        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();
        universe.addBranchGraph(scene);
        setView();
    }

    private void setView() {
        OrbitBehavior orbit = new OrbitBehavior(canvas,
                OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM);
        orbit.setSchedulingBounds(bounds1);

        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        viewingPlatform.setNominalViewingTransform();
        viewingPlatform.setViewPlatformBehavior(orbit);
    }

    private BranchGroup createSceneGraph() {
        // Create the root of the branch graph 
        BranchGroup branchGroup = new BranchGroup();
        //objRoot.addChild(new ColorCube(0.4));

        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds2);
        branchGroup.addChild(ambientLightNode);

        Vector3f light1Direction = new Vector3f(-1.0f, 1.0f, 1.0f);
        //左面，下面，后面
        DirectionalLight light1 = new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds2);
        branchGroup.addChild(light1);

        Background back = new Background();
        back.setApplicationBounds(bounds2);
        back.setColor(0.7f, 0.1f, 0.9f); //天空颜色
        branchGroup.addChild(back);

        TransformGroup tg = new TransformGroup(getTransform());
        Shape3D shape = new Shape3D(createGeometry());
        shape.setAppearance(createAppearance());
        tg.addChild(shape);

        branchGroup.addChild(tg);
        return branchGroup;
    }

    private Transform3D getTransform() {
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(0, 0, 0));
        return t3d;
    }

    private float calz(float x, float y) {
        float z = x * x + y * y;
        return z;
    }

    private Geometry createGeometry() {
        int MAXX = 50;
        int MAXY = 50;
        QuadArray surf = new QuadArray(
                MAXX * MAXY * 4, GeometryArray.COORDINATES
                | GeometryArray.NORMALS);
        for (int i = 0; i < MAXX; i++) {
            for (int j = 0; j < MAXY * 4; j += 4) {
                int jb = j / 4;
                float x = (i - 100f) / 200.0f;
                float y = (jb - 100f) / 200.0f;
                // 计算一个平面上的4个点
                Point3f A = new Point3f(x, y, calz(x, y));
                Point3f B
                        = new Point3f(x + 0.005f, y, calz(x + 0.005f, y));
                Point3f C = new Point3f(x + 0.005f,
                        y + 0.005f, calz(x + 0.005f, y + 0.005f));
                Point3f D = new Point3f(x, y + 0.005f,
                        calz(x, y + 0.005f));
                // 计算四个点的法向量
                Vector3f a = new Vector3f(
                        A.x - B.x, A.y - B.y, A.z - B.z);
                Vector3f b = new Vector3f(
                        C.x - B.x, C.y - B.y, C.z - B.z);
                Vector3f n = new Vector3f();
                n.cross(b, a);
                n.normalize();
                // 设置点坐标
                surf.setCoordinate(i * MAXY * 4 + j, A);
                surf.setCoordinate(i * MAXY * 4 + j + 1, B);
                surf.setCoordinate(i * MAXY * 4 + j + 2, C);
                surf.setCoordinate(i * MAXY * 4 + j + 3, D);
                // 设置点法向量
                surf.setNormal(i * MAXY * 4 + j, n);
                surf.setNormal(i * MAXY * 4 + j + 1, n);
                surf.setNormal(i * MAXY * 4 + j + 2, n);
                surf.setNormal(i * MAXY * 4 + j + 3, n);
            }
        }

        return surf;
    }

    Appearance createAppearance() {
// 指定外观，这样才有明暗效果
        Appearance appear = new Appearance();
        Material material = new Material();
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
        material.setDiffuseColor(red);
        material.setSpecularColor(white);
        material.setShininess(2.0f);
        appear.setMaterial(material);
        return appear;
    }
}
