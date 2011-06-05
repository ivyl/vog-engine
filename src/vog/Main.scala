package vog

import vog.config.Configuration
import vog.screen.Screen
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
  //TODO: setting icon

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
  def run() {
    var tempScreen: Option[Screen] = None

    var currentTime = Calendar.getInstance.getTimeInMillis
    var lastTime = currentTime

    visible = true

    screen.requestFocus() //needs this to respond to key events

    while(true) {
      currentTime = Calendar.getInstance.getTimeInMillis

      if (currentTime > lastTime + timeDifference) {
        lastTime = lastTime + timeDifference
        screen.tick()
        tempScreen = screen.nextScreen

        if (tempScreen.isDefined) {
          screen = tempScreen.get
          contents = screen
          tempScreen = None

          //Need this to grab key events
          screen.requestFocus()
        }

      } else {
        Thread.sleep((lastTime + timeDifference - currentTime)/2)
      }

    }
  }

}
