package com.drguildo.munge

import java.net.{HttpURLConnection, URL}
import java.io.{IOException, File, FileOutputStream, BufferedOutputStream}

import scala.collection.mutable.ListBuffer

object HttpDownloadStatus extends Enumeration {
  type Status = Value
  val Ready, Running, Finished, Stopped = Value
}

// TODO: Handle invalid URLs.
// TODO: Spoof referrer.
// TODO: Handle various HTTP status codes.
class HttpDownload(val url: URL, val path: File) extends Runnable {
  var append = false
  var progress = 0
  // The current percentage completion of the download.
  var status = HttpDownloadStatus.Ready

  private val progressListeners = new ListBuffer[Int => Unit]
  private val statusListeners = new ListBuffer[HttpDownloadStatus.Status => Unit]

  def this(url: URL) = this(url, new File(Settings.downloadLocation))

  def run() {
    val remoteFilename = UrlHelper.getFilename(url)
    val outputFile = if (path.isDirectory) {
      if (!path.exists) {
        path.mkdirs()
      }
      new File(path, remoteFilename)
    } else {
      path
    }
    val connection = url.openConnection().asInstanceOf[HttpURLConnection]
    connection.setRequestProperty("User-Agent", Settings.userAgent)

    println("HTTP response: " + connection.getResponseMessage)

    val contentLength = connection.getContentLength

    if (outputFile.exists) {
      if (outputFile.length < contentLength) {
        append = true
      } else {
        throw new IOException(remoteFilename + " already exists.")
      }
    }

    var offset = 0L
    var bytesRead = 0
    val buffer = new Array[Byte](1024)
    val in = connection.getInputStream
    val out = new BufferedOutputStream(new FileOutputStream(outputFile, append))

    if (append) {
      offset = outputFile.length
      in.skip(outputFile.length)
    }

    changeStatus(HttpDownloadStatus.Running)

    while ( {
      bytesRead = in.read(buffer)
      (bytesRead != -1 && status == HttpDownloadStatus.Running)
    }) {
      offset = offset + bytesRead

      // We know the file size.
      if (contentLength != -1) {
        val curProgress = (offset / contentLength.toFloat) * 100

        if (curProgress != progress) {
          progress = curProgress.toInt

          progressListeners.map(l => l(progress))
        }
      }

      out.write(buffer, 0, bytesRead)
    }

    if (status != HttpDownloadStatus.Stopped) {
      changeStatus(HttpDownloadStatus.Finished)
    }

    in.close()
    out.flush()
    out.close()
  }

  def stop() {
    if (status == HttpDownloadStatus.Running) {
      changeStatus(HttpDownloadStatus.Stopped)
    }
  }

  def changeStatus(newStatus: HttpDownloadStatus.Status) {
    status = newStatus
    statusListeners.map(l => l(status))
  }

  def registerProgressListener(f: Int => Unit) {
    if (!progressListeners.contains(f)) {
      progressListeners.append(f)
    }
  }

  def registerStatusListener(f: HttpDownloadStatus.Status => Unit) {
    if (!statusListeners.contains(f)) {
      statusListeners.append(f)
    }
  }
}
