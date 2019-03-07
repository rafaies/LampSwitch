// References
// https://www.dummies.com/programming/java/how-to-write-java-code-to-show-an-image-on-the-screen/
// https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
// https://docs.oracle.com/javase/tutorial/uiswing/events/index.html
// https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html#getresource
// https://code.i-harness.com/es/q/66c1
// https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples

// Compile
// javac LampSwitch.java

// Create jar file
// jar cfm LampSwitch-unsigned.jar Manifest.txt *.class images sound

// Execute the jar file
// java -jar LampSwitch-unsigned.jar

// Signing LampSwitch.jar
// Warning: The signer's certificate is self-signed.
//
// keytool -genkey -keyalg RSA -alias myKey -keystore myKeystore -validity 360
// jarsigner -keystore myKeystore -verbose -signedjar LampSwitch.jar -tsa http://timestamp.digicert.com LampSwitch-unsigned.jar myKey

// Verify: jarsigner -verify -keystore myKeystore LampSwitch.jar

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.sound.sampled.*;
import java.net.URL;
import java.io.IOException;

class LampSwitch {
	static String state = "off";

	public static void Sound() {
		try {
			// Open an audio input stream.
			URL url = LampSwitch.class.getClassLoader().getResource("sound/switch.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

			AudioFormat format = audioIn.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);

			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("Off");
		ImageIcon off = new ImageIcon(LampSwitch.class.getResource("images/lamp_off.jpg"));
		ImageIcon on = new ImageIcon(LampSwitch.class.getResource("images/lamp_on.jpg"));
		JLabel label = new JLabel(off);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		frame.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent event) {

				Sound();

				if (event.getClickCount() == 2) { // double clicked

					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

				} else if (state.equals("off")) {
					frame.setTitle("On");
					label.setIcon(on);
					state = "on";
				} else {
					frame.setTitle("Off");
					label.setIcon(off);
					state = "off";
				}
			}
		});
	}
}
