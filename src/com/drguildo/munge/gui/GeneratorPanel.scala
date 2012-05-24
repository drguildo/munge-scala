package com.drguildo.munge.gui

import java.awt.event._
import javax.swing._

import scala.collection.mutable.ListBuffer

import net.miginfocom.swing.MigLayout

import com.drguildo.munge.UrlHelper

object GeneratorPanel extends JPanel {
  private val urls = ListBuffer[String]()
  private val model = new UrlListModel(urls)
  private val table = new JTable(model)
  private val menu = new GeneratorPanelPopupMenu(table, urls)

  private val prefixField = new JTextField()
  private val suffixField = new JTextField()
  private val startSpinner = new JSpinner()
  private val amountSpinner = new JSpinner()
  private val paddingSpinner = new JSpinner()
  private val goButton = new JButton("Go")
  goButton.setMnemonic(KeyEvent.VK_G)
  goButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      val prefix = prefixField.getText
      val suffix = suffixField.getText
      val start = startSpinner.getValue.toString.toInt
      val amount = amountSpinner.getValue.toString.toInt
      val padding = paddingSpinner.getValue.toString.toInt

      for (url <- UrlHelper.generateList(prefix, suffix, padding, start, amount)) {
        if (!urls.contains(url)) {
          urls.append(url)
        }
      }

      model.fireTableDataChanged()
    }
  })

  table.setTableHeader(null)
  table.setFillsViewportHeight(true)
  table.addMouseListener(new PanelPopupAdapter(table, menu))

  setLayout(new MigLayout())

  add(new JLabel("Prefix"))
  add(prefixField, "w 100%")
  add(goButton, "spany 2, wrap")

  add(new JLabel("Suffix"))
  add(suffixField, "w 100%, wrap")

  add(new JLabel("Start"), "split 6, skip 1")
  add(startSpinner, "w button")
  add(new JLabel("Amount"))
  add(amountSpinner, "w button")
  add(new JLabel("Padding"))
  add(paddingSpinner, "w button, wrap")

  add(new JScrollPane(table), "w 100%, h 100%, span")
}
