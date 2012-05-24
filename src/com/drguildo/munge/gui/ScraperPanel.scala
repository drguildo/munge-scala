package com.drguildo.munge.gui

import java.awt.event._
import java.util.regex.PatternSyntaxException
import javax.swing._
import javax.swing.event.{DocumentEvent, DocumentListener}
import javax.swing.table.TableRowSorter

import scala.collection.mutable.ListBuffer

import net.miginfocom.swing.MigLayout

import com.drguildo.munge.Scraper

object ScraperPanel extends JPanel {
  private val urls = new ListBuffer[String]()
  private val model = new UrlListModel(urls)
  private val sorter = new TableRowSorter[UrlListModel](model)
  private val table = new JTable(model)
  private val menu = new ScraperPanelPopupMenu(table, urls)

  private val depthSpinnerModel = new SpinnerNumberModel(0, 0, 10, 1)
  private val depthSpinner = new JSpinner(depthSpinnerModel)

  private val urlField = new JTextField()
  private val scrapeButton = new JButton("Scrape")
  scrapeButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      val scrape = new Thread(new Runnable {
        def run() {
          val url = urlField.getText

          LogPanel.append("Attempting to scrape " + url)

          try {
            val links = Scraper.scrape(url, depthSpinnerModel.getNumber.intValue())

            SwingUtilities.invokeLater(new Runnable {
              def run() {
                urls.clear()

                links.foreach(urls.append(_))

                model.fireTableDataChanged()
              }
            })
          } catch {
            case e: IllegalArgumentException => LogPanel.append(e.getMessage)
          }
        }
      })
      scrape.start()
    }
  })
  private val filterField = new JTextField()
  filterField.getDocument.addDocumentListener(new DocumentListener {
    def changedUpdate(e: DocumentEvent) {}

    def removeUpdate(e: DocumentEvent) {
      newFilter()
    }

    def insertUpdate(e: DocumentEvent) {
      newFilter()
    }

    def newFilter() {
      var rowFilter: RowFilter[UrlListModel, Object] = null
      try {
        rowFilter = RowFilter.regexFilter(filterField.getText, 0)
      } catch {
        case e: PatternSyntaxException => return
      }
      sorter.setRowFilter(rowFilter)
    }
  })

  table.setFillsViewportHeight(true)
  table.setTableHeader(null)
  table.setRowSorter(sorter)
  table.addMouseListener(new PanelPopupAdapter(table, menu))

  setLayout(new MigLayout())

  add(new JLabel("URL"))
  add(urlField, "w 100%")
  add(new JLabel("Depth"))
  add(depthSpinner)
  add(scrapeButton, "wrap")

  add(new JScrollPane(table), "w 100%, h 100%, span")

  add(new JLabel("Filter"))
  add(filterField, "w 100%, span")
}
