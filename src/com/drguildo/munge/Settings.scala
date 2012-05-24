package com.drguildo.munge

import java.io.File

object Settings {
  val userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0"

  var downloadLocation = System.getProperty("user.home") + File.separator + "Downloads"
  var maxConcurrentDownloads = 2

  def setDownloadLocation(path: String) {
    downloadLocation = path
  }

  def setMaxConcurrentDownloads(max: Int) {
    assert(max > 0)

    maxConcurrentDownloads = max
  }
}
