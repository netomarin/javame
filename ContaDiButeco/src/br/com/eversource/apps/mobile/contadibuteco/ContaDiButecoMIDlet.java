package br.com.eversource.apps.mobile.contadibuteco;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import br.com.eversource.apps.mobile.contadibuteco.core.CommandHandler;
import br.com.eversource.apps.mobile.contadibuteco.core.DisplayManager;
import br.com.eversource.apps.mobile.contadibuteco.core.DisplayManagerAlreadyStartedException;
import br.com.eversource.apps.mobile.contadibuteco.core.DisplayManagerException;
import br.com.eversource.apps.mobile.contadibuteco.core.DisplayManagerNotStartedException;
import br.com.eversource.apps.mobile.contadibuteco.core.FlowManager;
import br.com.eversource.apps.mobile.contadibuteco.core.FlowManagerException;
import br.com.eversource.apps.mobile.contadibuteco.ui.TelaInicialCanvas;

public class ContaDiButecoMIDlet extends MIDlet {

	private TelaInicialCanvas telaInicial;
	
	public ContaDiButecoMIDlet() {
		try {
			DisplayManager.startDisplayManager(this);
			FlowManager.startFlowManager(this);
		} catch (DisplayManagerAlreadyStartedException e) {
		} catch (FlowManagerException e) {}
		
		this.telaInicial = new TelaInicialCanvas();
		this.telaInicial.addCommand(CommandHandler.calcularCmd);
		this.telaInicial.addCommand(CommandHandler.aboutCmd);
		this.telaInicial.addCommand(CommandHandler.exitCmd);
		this.telaInicial.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().pushToShowStack(this.telaInicial);
		} catch (DisplayManagerNotStartedException e) {}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		notifyDestroyed();
	}

	protected void pauseApp() {
		notifyPaused();
	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			DisplayManager.getInstance().show();
		} catch (DisplayManagerException e) {}
	}
}