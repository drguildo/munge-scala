package com.drguildo.munge.gui

import javax.swing.table.AbstractTableModel

import scala.collection.mutable.ListBuffer

class UrlListModel(urls: ListBuffer[String]) extends AbstractTableModel {
  private val columnNames = Array("URL")

  def getColumnCount = columnNames.length

  def getRowCount = urls.length

  def getValueAt(row: Int, column: Int) = {
    if (column == 0) {
      urls(row)
    } else {
      None
    }
  }

  override def getColumnName(column: Int): String = columnNames(column)

  override def isCellEditable(row: Int, column: Int): Boolean = false
}
