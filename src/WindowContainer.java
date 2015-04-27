/***************************************************************
* file: WindowContainer.java
* author: Jacob Buchowiecki
* class: CS 445 – Computer Graphics
*
* assignment: Program 3
* date last modified: 4/26/2015
*
* purpose: This class contains all of the methods required to construct and show
* an OpenGL window with the given objects and runtime code.
****************************************************************/
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class WindowContainer {
    
    private Scene scene;
    private FPCameraController camera;
    
    private final float MOUSE_SENSITIVITY = 0.09f;
    private final float MOVEMENT_SPEED = 0.35f;
    
    //method: constructor
    //purpose: Builds this WindowContainer.
    public WindowContainer(Scene s) {
        scene = s;
        camera = new FPCameraController(0, 0, 0);
    }
    
    //method: start
    //purpose: Initializes the display window, OpenGL, camera position and begins rendering.
    public void start() {
        try {
            initUI();
            initGL();
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //method: initUI
    //purpose: Initializes the display window.
    private void initUI() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
                    && d[i].getHeight() == 480
                    && d[i].getBitsPerPixel() == 32) {
                Display.setDisplayMode(d[i]);
                break;
            }
        }
        Display.setTitle("Project 3");
        Display.create();
        
        Mouse.setGrabbed(true);
    }
    
    //method: initGL
    //purpose: Initializes the OpenGL rendering suite used.
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100.0f
                , (float)Display.getDisplayMode().getWidth()/(float)Display.getDisplayMode().getHeight()
                , 0.1f, 300.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnable(GL_DEPTH_TEST);
    }
    
    //method: processInput
    //purpose: Changes the camera position based on if the user is pressing certain keys.
    //W moves the camera in the positive Z direction.
    //S moves the camera in the negative Z direction.
    //D moves the camera in the positive X direction.
    //A moves the camera in the negative X direction.
    //Space moves the camera in the positive Y direction.
    //Left shift moves the camera in the negative Y direction.
    private void processInput() {
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            camera.moveLeft(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            camera.moveRight(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            camera.moveBackward(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            camera.moveForward(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            camera.moveDown(MOVEMENT_SPEED);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            camera.moveUp(MOVEMENT_SPEED);
        }
    }
    
    //method: render
    //purpose: Renders the objects that were specified when this WindowContainer was made.
    //If the user presses "Esc" or closes the window, the display is destroyed.
    private void render() {
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            camera.incYaw(Mouse.getDX() * MOUSE_SENSITIVITY);
            camera.incPitch(Mouse.getDY() * MOUSE_SENSITIVITY);
            
            processInput();
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                camera.lookThrough();
                
                scene.drawScene();
                
                Display.update();
                Display.sync(60);
            } catch (Exception e) {
                
            }
        }
        Display.destroy();
    }
}