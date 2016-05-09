package edu.cmu.ideate.zkieda.mus;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.spi.MidiFileReader;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.midi.MidiParser;
import org.jfugue.parser.ParserListener;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import edu.cmu.ideate.zkieda.viz.core.Core;
import edu.cmu.ideate.zkieda.viz.core.Core.CoreState;
import edu.cmu.ideate.zkieda.viz.graphics.ImageRenderable;
import edu.cmu.ideate.zkieda.viz.graphics.Trip;
import edu.cmu.ideate.zkieda.viz.ui.ControllerUI;
import edu.cmu.ideate.zkieda.viz.ui.Display;

public class Main {
	private static Byte getInstrument(Track t){
		for(int messIdx = 0; messIdx < t.size(); messIdx++){
			MidiMessage message = t.get(messIdx).getMessage();
			if((message.getStatus()&0xF0) == 0xC0){
				byte instrument = message.getMessage()[1];
				return instrument;
			}
		}
		return null;
	}
	private static class TrackInfo{
		Track t;
		int index;
		public TrackInfo(Track t, int index) {
			this.t = t;
			this.index = index;
		}
	}
	
	private Display display;
	private Trip trippy;
	private Core core;
	
	private Main(){
		//look at duration, look at temp
		this.core = new Core(() -> {
			//make and set up our main display
			display = new Display(true);
			trippy = new Trip();
	    	
			display.createGraphics();
			
			//add renderables
			ImageRenderable ir = new ImageRenderable(trippy, display.getWidth(), display.getHeight());
	    	
	    	display.getRenderables().add(ir);
	    	core.getUpdateables().add(trippy);
	    	core.getUpdateables().add(display);
		}, () -> {
			display.dispose();
			display.getRenderables().clear();
	        core.setCoreState(CoreState.EXIT);
		});
		new Thread(core).start();
		
	}
	
	public static void main(String[] args){
		File[] allMidis = new File("midis").listFiles();
		Main main = new Main();
		
		try {
			Sequence[] s =  new Sequence[allMidis.length];
			
			//preprocessing
			for(int i = 0; i < allMidis.length; i++) s[i] = MidiFileManager.load(allMidis[i]);
			
			Map<Byte, List<TrackInfo>> trackByInstrument = new HashMap<>();
			for(int i = 0; i < s.length; i++){
				Sequence x = s[i];
				for(Track t : x.getTracks()){
					Byte b = getInstrument(t);
					if(b != null) {
						if(trackByInstrument.get(b) == null){
							ArrayList<TrackInfo> list = new ArrayList<>();
							list.add(new TrackInfo(t, i));
							trackByInstrument.put(b, list);
						} else trackByInstrument.get(b).add(new TrackInfo(t, i));
					}
				}
			}

			//initial time
			
			//video documentation )youtube, 3d video)
			//context of what is being demonstrated.
			//two pdfs -- one for each project
			// short article for the work that I did.
			// research paper type of thing
			// results
			// and what was learned (general pdf)
			// put technical paper.
			
			//may 11, before midnight.
			
			long millis = System.currentTimeMillis();
			AtomicInteger atomicInt = new AtomicInteger(5);
			for(int channelNo = 5; channelNo < 10; channelNo++){
				final int channel = channelNo;
				Thread kk = new Thread(() -> {
					try{
						Synthesizer synth = MidiSystem.getSynthesizer();
						synth.open();
						
						Byte currentInstrument;
						
						int currentSequenceIdx = 0;
						Track currentTrack = s[currentSequenceIdx].getTracks()[channel];
						currentInstrument = getInstrument(currentTrack);
						Receiver synthRcvr = synth.getReceiver();
						
						//set initial instrument, sequence, and 
						//sequence index
						
						//every iteration run ltsm to get new instrument/sequence
						//and sequence index
						while(true){
							double entireTrack = 1.2;
							for(int i = 0; i < currentTrack.size(); i++) {
								double wave = (System.currentTimeMillis() - millis)*.0001;
								double time = ((double)currentTrack.ticks())/(50 - 12.5*Math.sin(wave));
								double largeCurve = Math.abs(Math.sin(Math.PI/200. * wave * wave));
								
								entireTrack = .45*(1.0-Math.tanh((wave-15.0)*8.0/(3.0))) + .3;
								
								double sl = time*.5*(largeCurve + 1.5)/entireTrack;
//								if(channel == 1){
								if(wave >.61){
									double alphaIncrement = sl/ (255.0);
//									System.out.println(alphaIncrement);
									main.trippy.setDeltaAlpha((float)(alphaIncrement * alphaIncrement));
									main.trippy.setLambda((float)((1.0 - largeCurve)*.25 + 1.0));
								}
//								}
								MidiEvent evt = currentTrack.get(i);
								MidiMessage basicMessage = evt.getMessage();
								
								if(basicMessage instanceof ShortMessage){
									ShortMessage shortMessage = (ShortMessage)basicMessage;
									synthRcvr.send(shortMessage, -1);
									
								} else if(basicMessage instanceof MetaMessage){
									MetaMessage metaMessage = (MetaMessage)basicMessage;
									synthRcvr.send(metaMessage, -1);
								} else {} // throw out
								try{
									Thread.sleep((long)(sl));
								} catch(Exception e){e.printStackTrace();}
							}
							//alpha rate
							
							if(entireTrack < .4) {
								atomicInt.decrementAndGet();
								break;
							}
							if(Math.random() < .35){
								//same track
								continue;
							}
							double random = Math.random();
//							if(random < 1.0/3.0 && currentInstrument != null){
//								//same instrument
//								List<TrackInfo> tracks = trackByInstrument.get(currentInstrument);
//								int randomTrack = (int)(Math.random()*tracks.size());
//								TrackInfo info = tracks.get(randomTrack);
//								currentTrack = info.t;
//								currentSequenceIdx = info.index;
//								
//							} else 
							if(random < 2.0/3.0){
								//same sequence
								Sequence current = s[currentSequenceIdx];
								Track[] list = current.getTracks();
								int randomTrack = (int)(Math.random()*list.length);
								currentTrack = list[randomTrack];
								currentInstrument = getInstrument(currentTrack);
							} else{
								//completely new track
								int randomSequence = (int)(Math.random()*s.length);
								Sequence current = s[randomSequence];
								Track[] list = current.getTracks();
								int randomTrack = (int)(Math.random()*list.length);
								currentSequenceIdx = randomSequence;
								currentTrack = list[randomTrack];
								currentInstrument = getInstrument(currentTrack);
							}
						}
						
					} catch (Exception e){e.printStackTrace();}
				});
				kk.start();
				new Thread(() -> {
					while(atomicInt.get() != 0){
						try{
							Thread.currentThread().sleep(55);
							
						} catch(Exception e){
							
						}
					}
					main.trippy.setDeltaAlpha(0);
					main.trippy.setAlpha(0);
					main.trippy.setLambda(0);
				});
			}
			
			
//			for(int j = 0; j < s[0].getTracks().length; j++) {
//				Track t = s[0].getTracks()[j];
//				final int trackNo = j;
//				Thread kk = new Thread(() -> {
//					try{
//						Synthesizer synth = MidiSystem.getSynthesizer();
//						synth.open();
//						
//						Receiver synthRcvr = synth.getReceiver();
//						while(true){
//							for(int i = 0; i < t.size(); i++){
//								double wave = (System.currentTimeMillis() - millis)*.0001;
//								double time = ((double)t.ticks())/(50 - 12.5*Math.sin(wave));
//								double largeCurve = Math.abs(Math.sin(Math.PI/(2.0 * wave * 60)));
//								double sl = time*.5*(largeCurve + 1.5);
//								
//								MidiEvent evt = t.get(i);
//								MidiMessage basicMessage = evt.getMessage();
//								
//								if(basicMessage instanceof ShortMessage){
//									ShortMessage shortMessage = (ShortMessage)basicMessage;
//									synthRcvr.send(shortMessage, -1);
//									
//								} else if(basicMessage instanceof MetaMessage){
//									MetaMessage metaMessage = (MetaMessage)basicMessage;
//									synthRcvr.send(metaMessage, -1);
//								} else {} // throw out
//								try{
//									Thread.sleep((long)(sl));
//								} catch(Exception e){}
//							}
//						}
//					} catch (Exception e){}
//				});
//				
//				
//				kk.start();
//			}
//		System.out.println(MidiFileManager.loadPatternFromMidi(f).getTokens());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
