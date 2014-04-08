package br.com.eversource.apps.mobile.contadibuteco.ui;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class TelaInicialCanvas extends Canvas {
	
	private Image butecoImg;
	
	public TelaInicialCanvas () {
		try {
			this.butecoImg = Image.createImage("/lata.png");
		} catch (IOException e) {}
	}

	protected void paint(Graphics g) {
		g.drawImage(this.butecoImg, this.getWidth()/2, this.getHeight()/2, Graphics.VCENTER|Graphics.HCENTER);
	}
}
