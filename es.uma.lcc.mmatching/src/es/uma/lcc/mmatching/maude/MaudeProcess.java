/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date Sep 15th 2014
 * 
 * 
 *       This file is part of MMatching.
 *
 *       MMatching is free software: you can redistribute it and/or modify it
 *       under the terms of the GNU General Public License as published by the
 *       Free Software Foundation, either version 3 of the License, or (at your
 *       option) any later version.
 * 
 *       MMatching is distributed in the hope that it will be useful, but
 *       WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *       General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License along
 *       with MMatching. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package es.uma.lcc.mmatching.maude;

import java.io.IOException;
import java.net.URL;

import es.uma.lcc.ama.maudedaemon.core.MaudeDaemonPlugin;
import es.uma.lcc.ama.maudedaemon.maude.IMaudeJob;
import es.uma.lcc.ama.maudedaemon.maude.IMaudeProcessBatch;
import es.uma.lcc.ama.maudedaemon.parser.ParseException;
import es.uma.lcc.mmatching.maude.exceptions.JobFailedException;
import es.uma.lcc.mmatching.maude.exceptions.MaudeErrorException;
import es.uma.lcc.mmatching.maude.exceptions.MaudePrefsException;

public class MaudeProcess {

	private IMaudeProcessBatch self;

	/**
	 * Inits a Maude proccess
	 */
	public void initMaudeProcess() throws MaudePrefsException, MaudeErrorException {
			System.out.println("initprocess");
		
			// creates the Maude proccess
			self = MaudeDaemonPlugin.getDefault().getNewMaudeProcesBatch();

			// we configure it
			if (!self.configMaudeFromPrefs()) {
				throw new MaudePrefsException("[error] Maude error configuring preferences.");
			}
			self.setCoreMaude();
			self.setAutoConfig(false);

			try {
	      self.execMaude();
      } catch (IOException e) {
	      throw new MaudeErrorException(e.getMessage());
      }
	}

	/**
	 * Load all Maude infrastracture
	 * 
	 * @throws IOException
	 * @throws MaudePrefsException
	 */
	public void loadEMotions() throws JobFailedException, ParseException, MaudeErrorException, MaudePrefsException, IOException {
		loadFileInternal("mOdCL.maude");
		loadFileInternal("MGDefinitions.maude");
		loadFileInternal("EcoreMM.maude");
		loadFileInternal("MGRealTimeMaude24.maude");
		loadFileInternal("e-Motions.maude");
	}

	public Boolean isRunning() {
		return self.isRunning();
	}

	private String loadFileInternal(String filePath) throws MaudeErrorException, MaudePrefsException, IOException,
	    ParseException, JobFailedException {
		URL urlArchivo = MaudeProcess.class.getResource(filePath);
		return loadFile(urlArchivo);
	}
	
	/**
	 * Send a file to the Maude process
	 */
	public String loadFile(URL filePath) throws JobFailedException, ParseException, IOException,
	    MaudeErrorException, MaudePrefsException {
		// we load the file
//		String file = FileManager.readFile(filePath);

//		return sendFile2Maude(file);
		return null;
	}
	
	public String loadFile(String filePath) throws JobFailedException, ParseException, IOException,
	MaudeErrorException, MaudePrefsException {
		// we load the file
//		String file = FileManager.readFile(filePath);
		
//		return sendFile2Maude(file);
		return null;
	}

	private String sendFile2Maude(String file) throws ParseException, JobFailedException,
      MaudeErrorException {
	  // we send it to Maude
		IMaudeJob mj = self.createAndRunJobs(file).get(0);
		self.waitUntilFinish();
		if (mj.isFailed()) {
			throw new JobFailedException("[error] Error loading file in Maude.");
		}
		
		// output
		String error = mj.getError(); // solo para debugging
		if (!error.equals("")) {
			throw new MaudeErrorException("[error] Maude returned errors when loading files: " + error);
		}
		return mj.getOut();
  }


	/**
	 * Load a String on Maude
	 */
	public String load(String string) throws JobFailedException, ParseException, IOException, MaudeErrorException,
	    MaudePrefsException {
		
//		Printer.getDefault().debug("[debug] We're going to send to Maude: "+string);
		// we send such command to maude
		IMaudeJob mj = self.createAndRunJobs(string).get(0);
    mj.waitUntilFinish();
    
    String output = mj.getOut();
//		Printer.getDefault().debug("[debug] Maude returns as error: "+mj.getError());
//		Printer.getDefault().debug("[debug] Maude returns as output: "+output);
		return output;
	}

	/**
	 * Stops Maude
	 */
	public void stop() {
		self.killMaude();
	}

}