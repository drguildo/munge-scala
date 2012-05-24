package com.drguildo.munge.gui

import java.lang.Thread.UncaughtExceptionHandler
import java.net.URL
import javax.swing.JProgressBar

import com.drguildo.munge.{HttpDownloadStatus, HttpDownload}

class HttpDownloadBar(val url: URL) extends JProgressBar {
  val download = new HttpDownload(url)
  var thread = new Thread(download)

  download.registerProgressListener(this.progressListener)
  download.registerStatusListener(this.statusListener)

  setBorderPainted(false)

  def start() {
    LogPanel.append("Attempting to download %s to %s".format(download.url, download.path))
    if (thread.getState == Thread.State.TERMINATED) {
      // The download thread has already been run once so we need to create
      // a new one to run it again.
      thread = new Thread(download)
    }

    setIndeterminate(true)

    thread.setUncaughtExceptionHandler(handler)
    thread.start()
  }

  def stop() {
    download.stop()
  }

  val handler = new UncaughtExceptionHandler() {
    def uncaughtException(thread: Thread, exception: Throwable) {
      setIndeterminate(false)
      setToolTipText(exception.toString)
    }
  }

  def progressListener(progress: Int) {
    setIndeterminate(false)
    setStringPainted(true)
    setValue(progress)
  }

  def statusListener(status: HttpDownloadStatus.Status) {
    println(status)
    if (status == HttpDownloadStatus.Finished) {
      setIndeterminate(false)
      setValue(100)
    }
  }

  /*
  <datura> drguildo: from looking at BasicProgressBarUI the problem could be
           that the JProgressBar is not Displayable because it's not in any
           widet containment hierarchy. if that'S the case, the
           BasicProgressBarUI's animation timer is not started.
  <datura> drguildo: http://www.javalobby.org/java/forums/t16703.html#91957095
  */
  override def isDisplayable = true

  override def repaint() {
    // TODO: This is pig disgusting and needs fixing.
    DownloadPanel.table.repaint()
  }
}
