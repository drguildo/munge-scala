package com.drguildo.munge.gui

import java.awt.Dimension
import javax.swing.{SwingUtilities, JTabbedPane, JFrame}

// For some reason we need a class for IDEA to be able to create and artifact.
class Main {
  def createAndShowGUI() {
    val frame = new JFrame("Munge")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setPreferredSize(new Dimension(800, 600))

    val tabbedPane = new JTabbedPane()
    tabbedPane.add(GeneratorPanel, "Generator")
    tabbedPane.add(ScraperPanel, "Scraper")
    tabbedPane.add(DownloadPanel, "Download")
    tabbedPane.add(SettingsPanel, "Settings")
    tabbedPane.add(LogPanel, "Log")

    frame.add(tabbedPane)

    frame.pack()
    frame.setVisible(true)
  }
}

object Main {
  def main(args: Array[String]) {
    SwingUtilities.invokeLater(new Runnable {
      def run() {
        val main = new Main()
        main.createAndShowGUI()

        DownloadPanel.add("http://www.google.com/robots.txt")
        DownloadPanel.add("http://ftp.gnome.org/pub/gnome/binaries/win32/gtk+/2.24/gtk+_2.24.10-1_win32.zip")
      }
    })
  }
}
