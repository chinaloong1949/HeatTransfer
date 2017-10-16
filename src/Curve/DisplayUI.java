package Curve;

import java.awt.BorderLayout;
//import java.awt.event.*;
//import java.awt.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JPanel;

/**
 * Generates a scene graph with the cylinders that make up the axis, and 3
 * cones. Add one Cone with rotate before translate, and another that has
 * translate before rotate. For reference, throw in a cone without translate or
 * rotate
 */
public class DisplayUI extends JPanel {

    public static final int ROTATE_TOP = 4;
    public static final int TRANSLATE_TOP = 5;
    public static final int NO_TRANSFORM = 6;
    private SimpleUniverse universe;
    private Canvas3D canvas;
    private BoundingSphere bounds
            = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);

    private Appearance red = new Appearance();
    private Appearance yellow = new Appearance();
    private Appearance purple = new Appearance();
    Transform3D rotate = new Transform3D();
    Transform3D translate = new Transform3D();

    public DisplayUI() {
        BranchGroup scene = createSceneGraph_My();
        this.setLayout(new BorderLayout());
        GraphicsConfiguration config
                = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        this.add(canvas, BorderLayout.CENTER);
        // Create a simple scene and attach it to the virtual universe
        universe = new SimpleUniverse(canvas);
        setupView();
        universe.addBranchGraph(scene);
    }

    public void setupView() {
        /**
         * Add some view related things to view branch side of scene graph
         */
        // add mouse interaction to the ViewingPlatform
        OrbitBehavior orbit = new OrbitBehavior(canvas,
                OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        viewingPlatform.setNominalViewingTransform();
        viewingPlatform.setViewPlatformBehavior(orbit);
    }

    //construct each branch of the graph, changing the order children added
    // since  Group node can only have one parent, have to construct
    // new translate and rotate group nodes for each branch.
    private Group rotateOnTop() {
        Group root = new Group();
        TransformGroup objRotate = new TransformGroup(rotate);
        TransformGroup objTranslate = new TransformGroup(translate);
        Cone redCone
                = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, red);
        root.addChild(objRotate);
        objRotate.addChild(objTranslate);
        objTranslate.addChild(redCone); //tack on red cone
        return root;
    }

    private Group translateOnTop() {
        Group root = new Group();
        TransformGroup objRotate = new TransformGroup(rotate);
        TransformGroup objTranslate = new TransformGroup(translate);
        Cone yellowCone
                = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, yellow);
        root.addChild(objTranslate);
        objTranslate.addChild(objRotate);
        objRotate.addChild(yellowCone); //tack on yellow cone
        return root;

    }

    private Group noTransform() {
        Cone purpleCone
                = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, purple);
        return purpleCone;
    }

    public TransformGroup createTempAxis(int type) {
        //appearance and lightingProps are used in
        //lighting. Each axis a different color
        Appearance appearance = new Appearance();
        Material lightingProps = new Material();
        Transform3D t = new Transform3D();
        switch (type) {
            case 3:
                t.rotX(Math.toRadians(90.0));
                lightingProps.setAmbientColor(1.0f, 0.0f, 0.0f);
                break;
            case 2:
                // no rotation needed, cylinder aligned with Y already
                lightingProps.setAmbientColor(0.0f, 1.0f, 0.0f);
                break;
            case 1:
                t.rotZ(Math.toRadians(90.0));
                lightingProps.setAmbientColor(0.0f, 0.0f, 1.0f);
                break;
            default:
                break;
        }
        appearance.setMaterial(lightingProps);
        TransformGroup objTrans = new TransformGroup(t);
        objTrans.addChild(new Cylinder(.03f, 2.5f, Primitive.GENERATE_NORMALS, appearance));
        return objTrans;
    }

    public BranchGroup createSceneGraph_My() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();
        //45 degree rotation around the X axis
        rotate.rotX(Math.toRadians(45.0));
        //translation up the Y axis
        translate.setTranslation(new Vector3f(0.0f, 2.0f, 1.0f)); //SCD 0.0f));   
        //Material objects are related to lighting, we'll cover
        //that later
        Material redProps = new Material();
        redProps.setAmbientColor(1.0f, 0.0f, 0.0f); //red cone
        red.setMaterial(redProps);
        Material yellowProps = new Material();
        yellowProps.setAmbientColor(1.0f, 1.0f, 0.0f); //yellow cone
        yellow.setMaterial(yellowProps);
        Material purpleProps = new Material();
        purpleProps.setAmbientColor(0.8f, 0.0f, 0.8f); //purple cone
        purple.setMaterial(purpleProps);
        // Create a x,y,z axis, and then 3 cone branches
        objRoot.addChild(createTempAxis(1));//X
        objRoot.addChild(createTempAxis(2));//Y
        objRoot.addChild(createTempAxis(3));//Z
        objRoot.addChild(noTransform());    //purple cone
        objRoot.addChild(rotateOnTop());      //red cone
        objRoot.addChild(translateOnTop());   //yellow cone
        //throw in some light so we aren't stumbling 
        //around in the dark
        Color3f lightColor = new Color3f(.3f, .3f, .3f);
        AmbientLight ambientLight = new AmbientLight(lightColor);
        ambientLight.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLight);
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(lightColor);
        directionalLight.setInfluencingBounds(bounds);
        objRoot.addChild(directionalLight);
        return objRoot;
    }

}
