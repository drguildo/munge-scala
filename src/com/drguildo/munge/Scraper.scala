package com.drguildo.munge

import scala.collection.JavaConversions._

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Scraper {
  def scrape(url: String, depth: Int = 0, visited: List[String] = List()): List[String] = {
    require(depth >= 0)

    if (visited.contains(url)) {
      println("Skipping " + url)
      return List()
    }

    println("Scraping " + url)

    val con = Jsoup.connect(url)
    val doc = try {
      con.userAgent(Settings.userAgent).get()
    } catch {
      case e: Exception => new Document("")
    }

    val images = elemAttrs(doc, "img", "abs:src").distinct
    val urls = elemAttrs(doc, "a", "abs:href").filter(_.startsWith("http")).distinct
    if (depth > 0) {
      images ++ urls ++ urls.map(Scraper.scrape(_, depth - 1, url :: visited)).flatten
    } else {
      images ++ urls
    }
  }

  // Returns a list of the values of all the specified tag attribute contained
  // in the specified document.
  def elemAttrs(doc: Document, tag: String, attr: String): List[String] =
    doc.getElementsByTag(tag).map(_.attr(attr)).filterNot(_ == "").toList
}
