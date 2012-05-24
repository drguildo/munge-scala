package com.drguildo.munge.gui

import javax.swing.JTable
import javax.swing.table.TableCellRenderer

import scala.collection.mutable.ListBuffer

class DownloadProgressRenderer(bars: ListBuffer[HttpDownloadBar]) extends TableCellRenderer {
  override def getTableCellRendererComponent(tbl: JTable, value: AnyRef,
                                             isSelected: Boolean,
                                             hasFocus: Boolean,
                                             row: Int, column: Int) = {
    bars(row)
  }
}
