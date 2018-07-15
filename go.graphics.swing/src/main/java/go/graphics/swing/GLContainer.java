package go.graphics.swing;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import go.graphics.event.GOEventHandlerProvider;
import go.graphics.swing.contextcreator.BackendSelector;
import go.graphics.swing.contextcreator.ContextCreator;
import go.graphics.swing.contextcreator.EBackendType;
import go.graphics.swing.contextcreator.JAWTContextCreator;
import go.graphics.swing.opengl.LWJGLDrawContext;

public abstract class GLContainer extends JPanel implements GOEventHandlerProvider {


	protected ContextCreator cc;
	protected LWJGLDrawContext context;
	private boolean debug;

	public GLContainer(EBackendType backend, LayoutManager layout, boolean debug) {
		setLayout(layout);
		this.debug = debug;

		try {
			cc = BackendSelector.createBackend(this, backend, debug);
			cc.init();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not create opengl context through " + backend.cc_name + "\nPress ok to exit");
			System.exit(1);
		}
	}

	public void resize_gl(int width, int height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		// coordinate system origin at lower left with width and height same as
		// the window
		GL11.glOrtho(0, width, 0, height, -1, 1);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, width, height);
	}

	private boolean errormessage_shown = false;

	public void wrapNewContext() {
		if(cc instanceof JAWTContextCreator) ((JAWTContextCreator)cc).makeCurrent(true);
		if(context != null) context.disposeAll();
		context = new LWJGLDrawContext(GL.createCapabilities(), debug);
	}

	/**
	 * Disposes all textures / buffers that were allocated by this context.
	 */
	public void disposeAll() {
		cc.stop();
		if (context != null) {
			context.disposeAll();
		}
		context = null;
	}

	public void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
	}

	public void requestRedraw() {
		cc.repaint();
	}

	/**
	 * Forward the focus call to the Input canvas
	 */
	@Override
	public void requestFocus() {
		cc.requestFocus();
	}

	public void addCanvas(Component canvas) {
		add(canvas);
	}
}
