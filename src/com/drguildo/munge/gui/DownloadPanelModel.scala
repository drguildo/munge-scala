package com.drguildo.munge.gui

import javax.swing.table.AbstractTableModel

import scala.collection.mutable.ListBuffer

class DownloadPanelModel(bars: ListBuffer[HttpDownloadBar]) extends AbstractTableModel {
  private val columnNames = Array("URL", "Progress")

  def getColumnCount = columnNames.length

  def getRowCount = bars.length

  def getValueAt(row: Int, column: Int) = {
    if (column == 0) {
      bars(row).url
    } else {
      None
    }
  }

  override def getColumnName(column: Int): String = columnNames(column)

  override def isCellEditable(row: Int, column: Int) = false
}
