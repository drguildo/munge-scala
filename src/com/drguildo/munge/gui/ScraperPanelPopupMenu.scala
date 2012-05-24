package com.drguildo.munge.gui

import javax.swing.{JMenuItem, JPopupMenu, JTable}
import java.awt.datatransfer.StringSelection
import java.awt.event.{ActionEvent, ActionListener}

import scala.collection.mutable.ListBuffer

class ScraperPanelPopupMenu(table: JTable, urls: ListBuffer[String]) extends JPopupMenu {
  private val copyButton = new JMenuItem("Copy")
  copyButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      val builder = new StringBuilder()
      for (i <- table.getSelectedRows) {
        builder.append(urls(table.convertRowIndexToModel(i)) + "\n")
      }

      val selection = new StringSelection(builder.toString())
      getToolkit.getSystemClipboard.setContents(selection, selection)
    }
  })
  add(copyButton)

  private val downloadButton = new JMenuItem("Download")
  downloadButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      for (i <- table.getSelectedRows) {
        DownloadPanel.add(urls(table.convertRowIndexToModel(i)))
      }
    }
  })
  add(downloadButton)
}
