package vog

import config.Configuration
import screen.Screen
import swing.{MainFrame,Dimension}
import java.util.Calendar

/**
 * @author Ivyl
 */
object Main {

  def main(argv: Array[String]) {
    (new Main(new Screen {
      def nextScreen = None

      def tick = {}
    })).run

  }

}

class Main(var screen: Screen) extends MainFrame {
  val width = Configuration.config.getInt("window.width", 1024)
  val height = Configuration.config.getInt("window.height", 765)

  /**
   * Ticks per second.
   * Tick is screen behaviour unit.
   */
  val tps = Configuration.config.getInt("game.tps", 25)

  private val timeDifference: Long = (1000 / tps)
  private val iconFile = Configuration.config.getString("window.icon")

  size = new Dimension(width, height)
  preferredSize = size
  resizable = false

  title = Configuration.config.getString("window.title", "VoG framework")

  //if (iconFile.isDefined) iconImage = CacheManager.image.retrieve(iconFile.get)

  contents = screen


  //TODO: Make this cleaner.
  /**
   * Runs main loop starting with initial screen.
   */
  def run {
    var tempScreen: Option[Screen] = None

    var currentTime = Calendar.getInstance.getTimeInMillis
    var lastTime = currentTime

    visible = true

    while(true) {
      currentTime = Calendar.getInstance.getTimeInMillis
      if (currentTime > lastTime + timeDifference) {
        lastTime = currentTime
        screen.tick
        tempScreen = screen.nextScreen
        if (tempScreen.isDefined) {
          screen = tempScreen.get
          contents = screen
          //TODO: Make sure it doesn't need revalidation
          tempScreen = None
        }
      } else {
        Thread.sleep((lastTime + timeDifference - currentTime)/2)
      }
    }
  }

}