package com.drguildo.munge.gui

import java.awt.datatransfer.StringSelection
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JTable, JMenuItem, JPopupMenu}

import scala.collection.mutable.ListBuffer

class DownloadPanelPopupMenu(table: JTable, bars: ListBuffer[HttpDownloadBar]) extends JPopupMenu {
  private val copyButton = new JMenuItem("Copy")
  copyButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      val builder = new StringBuilder()
      for (i <- table.getSelectedRows) {
        //builder.append(bars(i).url + "\n")
        println(bars(i).url)
      }

      val selection = new StringSelection(builder.toString())
      getToolkit.getSystemClipboard.setContents(selection, selection)
    }
  })
  add(copyButton)

  private val removeButton = new JMenuItem("Remove")
  removeButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      // We have to remove in reverse because we're modifying the list in-place
      // and so when i is removed, any greater element becomes i-1.
      for (i <- table.getSelectedRows.reverse) {
        bars.remove(i)
        table.getModel.asInstanceOf[DownloadPanelModel].fireTableDataChanged()
      }
    }
  })
  add(removeButton)
}
