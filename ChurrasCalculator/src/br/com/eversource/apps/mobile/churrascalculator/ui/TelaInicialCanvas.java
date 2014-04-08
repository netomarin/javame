package br.com.eversource.apps.mobile.churrascalculator.ui;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class TelaInicialCanvas extends Canvas {
	
	private Image churrasImg;
	
	public TelaInicialCanvas () {
		try {
			this.churrasImg = Image.createImage("/churras.PNG");
		} catch (IOException e) {}
	}

	protected void paint(Graphics g) {
		g.drawImage(this.churrasImg, this.getWidth()/2, this.getHeight()/2, Graphics.VCENTER|Graphics.HCENTER);
	}
}
