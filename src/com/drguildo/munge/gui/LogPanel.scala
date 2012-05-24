package com.drguildo.munge.gui

import java.awt.Font
import java.util.Date
import javax.swing.{JScrollPane, JTextArea, JPanel}

import net.miginfocom.swing.MigLayout

object LogPanel extends JPanel {
  val textArea = new JTextArea()

  textArea.setFont(Font.decode(Font.MONOSPACED))
  textArea.setLineWrap(true)
  textArea.setWrapStyleWord(true)

  setLayout(new MigLayout())

  add(new JScrollPane(textArea), "w 100%, h 100%")

  def append(text: String) {
    val date = new Date()
    textArea.append("[%s] %s".format(date, text))
  }
}
