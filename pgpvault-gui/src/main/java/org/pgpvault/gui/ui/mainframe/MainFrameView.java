package org.pgpvault.gui.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.pgpvault.gui.app.Messages;
import org.pgpvault.gui.ui.tools.UiUtils;
import org.pgpvault.gui.ui.tools.WindowIcon;

import com.google.common.base.Preconditions;

import ru.skarpushin.swingpm.base.HasWindow;
import ru.skarpushin.swingpm.base.ViewBase;

public class MainFrameView extends ViewBase<MainFramePm> implements HasWindow {
	private JFrame frame;

	private JPanel panelRoot;

	private JMenuBar menuBar;
	private JMenuItem miConfigExit;
	private JMenuItem miAbout;

	@Override
	protected void internalInitComponents() {
		panelRoot = new JPanel(new BorderLayout());

		initMenuBar();
		initFormComponents();
	}

	private void highlight(JComponent subject) {
		subject.setBackground(Color.blue);
	}

	private void initFormComponents() {
		JLabel tbd = new JLabel("!             TBD             !", SwingConstants.CENTER);
		panelRoot.add(tbd, BorderLayout.CENTER);
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();

		JMenu menuFile = new JMenu(Messages.get("term.appTitle"));
		menuFile.add(miAbout = new JMenuItem());
		menuFile.add(miConfigExit = new JMenuItem());
		menuBar.add(menuFile);
	}

	@Override
	protected void internalBindToPm() {
		super.internalBindToPm();

		updateWindowTitle();
		bindToActions();
	}

	private void bindToActions() {
		miConfigExit.setAction(pm.getActionConfigExit());
		miAbout.setAction(pm.getActionAbout());
	}

	private void updateWindowTitle() {
		if (frame != null) {
			frame.setTitle(Messages.get("term.appTitle"));
		}
	}

	@Override
	protected void internalUnbindFromPm() {
		super.internalUnbindFromPm();

		updateWindowTitle();
		unbindFromActions();
	}

	private void unbindFromActions() {
		miConfigExit.setAction(null);
		miAbout.setAction(null);
	}

	@Override
	protected void internalRenderTo(Container owner, Object constraints) {
		Preconditions.checkArgument(owner == null || owner instanceof Window,
				"Target must not be specified or be sub-calss of Window");
		Preconditions.checkState(pm != null, "PM is required for this view");

		if (frame != null && frame.getOwner() != owner) {
			frame.remove(panelRoot);
			frame.dispose();
			frame = null;
		}

		if (frame == null) {
			frame = new JFrame();
			frame.setSize(new Dimension(UiUtils.getFontRelativeSize(70), UiUtils.getFontRelativeSize(40)));
			frame.setLayout(new BorderLayout());
			frame.setResizable(true);
			frame.setMinimumSize(new Dimension(UiUtils.getFontRelativeSize(60), UiUtils.getFontRelativeSize(30)));
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			updateWindowTitle();
			frame.add(panelRoot, BorderLayout.CENTER);
			frame.addWindowListener(windowAdapter);
			frame.setJMenuBar(menuBar);

			UiUtils.centerWindow(frame);
			WindowIcon.setWindowIcon(frame);
		}

		frame.setVisible(true);
	}

	protected WindowAdapter windowAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			if (isAttached()) {
				pm.getActionConfigExit().actionPerformed(null);
			}
			super.windowClosing(e);
		};
	};

	@Override
	protected void internalUnrender() {
		frame.setVisible(false);
	}

	@Override
	public Window getWindow() {
		return frame;
	}

}