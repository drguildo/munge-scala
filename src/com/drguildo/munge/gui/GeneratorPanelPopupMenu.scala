package com.drguildo.munge.gui

import java.awt.datatransfer.StringSelection
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JMenuItem, JTable, JPopupMenu}

import scala.collection.mutable.ListBuffer

class GeneratorPanelPopupMenu(table: JTable, urls: ListBuffer[String]) extends JPopupMenu {
  private val copyButton = new JMenuItem("Copy")
  copyButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      val builder = new StringBuilder()
      for (i <- table.getSelectedRows) {
        builder.append(urls(i) + "\n")
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
        DownloadPanel.add(urls(i))
      }
    }
  })
  add(downloadButton)

  addSeparator()

  private val deleteButton = new JMenuItem("Delete")
  deleteButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      // We have to delete in reverse because we're modifying the list in-place
      // and so when i is removed, any greater element becomes i-1.
      for (i <- table.getSelectedRows.reverse) {
        urls.remove(i)
        table.getModel.asInstanceOf[UrlListModel].fireTableDataChanged()
      }
    }
  })
  add(deleteButton)

  private val clearButton = new JMenuItem("Clear")
  clearButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      if (urls.length > 0) {
        urls.clear()
        table.getModel.asInstanceOf[UrlListModel].fireTableDataChanged()
      }
    }
  })
  add(clearButton)
}
