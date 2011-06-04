package vog.config

import net.lag.configgy.Configgy


/**
 * Nice wrapper for configgy. Initializes for default configuration file.
 * @author Ivyl
 */
object Configuration {
  var dataDirectory = "data/"

  Configgy.configure(dataDirectory + "config")

  /**
   * Retrieve configuration class.
   * @return config
   */
  def config = Configgy.config

  /**
   * Change used configuration file.
   * @param filename location (can be relative) of file
   */
  def configFile(filename: String) {
    Configgy.configure(filename)
  }



}
