package com.drguildo.munge.gui

import java.awt.event.{MouseEvent, MouseAdapter}
import javax.swing.{JPopupMenu, JTable}

class PanelPopupAdapter(table: JTable, menu: JPopupMenu) extends MouseAdapter {
  override def mousePressed(e: MouseEvent) {
    maybeShowPopup(e)
  }

  override def mouseReleased(e: MouseEvent) {
    maybeShowPopup(e)
  }

  def maybeShowPopup(e: MouseEvent) {
    if (table.getSelectedRowCount > 0 && e.isPopupTrigger) {
      menu.show(e.getComponent, e.getX, e.getY)
    }
  }
}
