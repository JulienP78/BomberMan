package bbman;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Audio
{
	public Audio(String nomFichier)
	{
		URL url = Audio.class.getResource(nomFichier+".wav"); 
		AudioClip son = Applet.newAudioClip(url); 
		son.play();	
	}
	
}
