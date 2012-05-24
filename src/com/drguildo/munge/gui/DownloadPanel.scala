package com.drguildo.munge.gui

import java.awt.event.{ActionEvent, ActionListener}
import java.net.{MalformedURLException, URL}
import javax.swing._

import scala.collection.mutable.ListBuffer

import net.miginfocom.swing.MigLayout

object DownloadPanel extends JPanel {
  private val bars = new ListBuffer[HttpDownloadBar]()
  private val model = new DownloadPanelModel(bars)
  val table = new JTable(model)
  private val menu = new DownloadPanelPopupMenu(table, bars)

  private val urlField = new JTextField()
  private val addButton = new JButton("Add")
  addButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      add(urlField.getText)
    }
  })

  private val startButton = new JButton("Start")
  startButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      for (i <- table.getSelectedRows) {
        bars(i).start()
      }
    }
  })

  private val stopButton = new JButton("Stop")
  stopButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      for (i <- table.getSelectedRows) {
        bars(i).stop()
      }
    }
  })

  def add(url: String) {
    try {
      val u = new URL(url)
      bars.append(new HttpDownloadBar(u))
    } catch {
      case e: MalformedURLException => LogPanel.append(e.getMessage)
    }
    model.fireTableRowsInserted(0, bars.length)
  }

  table.addMouseListener(new PanelPopupAdapter(table, menu))
  table.setFillsViewportHeight(true)
  table.getColumnModel.getColumn(0).setPreferredWidth(600)
  table.getColumnModel.getColumn(1).setCellRenderer(new DownloadProgressRenderer(bars))

  setLayout(new MigLayout())

  add(urlField, "w 100%")
  add(addButton, "wrap")

  add(new JScrollPane(table), "w 100%, h 100%, span, wrap")

  add(startButton)
  add(stopButton)
}
