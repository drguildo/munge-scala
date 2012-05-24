package com.drguildo.munge

import java.net.URL

import scala.collection.mutable.ListBuffer

object UrlHelper {
  /**Generates a list of URLs by substituting the format specifier with a
   * range of numbers.
   */
  def generateList(prefix: String, suffix: String,
                   padding: Int, start: Int, amount: Int): ListBuffer[String] = {
    val urls = ListBuffer[String]()

    if (amount < 1)
      return ListBuffer[String]()

    var fmt = ""
    if (padding > 0)
      fmt = "%0" + padding + "d"
    else
      fmt = "%d"

    for (i <- start to (start + amount - 1))
      urls.append(prefix + fmt.format(i) + suffix)

    urls
  }

  /**Returns the absolute version of a relative URL.
   */
  def absolute(parent: String, child: String): String = {
    import java.net.URL
    new URL(new URL(parent), child).toString
  }

  def getDirectory(url: URL): String = {
    ""
  }

  def getFilename(url: URL): String = {
    val defaultFilename = "default.out"

    val path = url.getPath
    val filename = path.substring(path.lastIndexOf('/') + 1)

    if (filename.length > 0) {
      filename
    } else {
      defaultFilename
    }
  }
}
