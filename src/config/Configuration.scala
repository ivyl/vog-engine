package config

import net.lag.configgy.Configgy


/**
 * @author Ivyl
 */
object Configuration {
  val dataDirectory = "data/"

  Configgy.configure(dataDirectory + "config")

  val config = Configgy.config


}