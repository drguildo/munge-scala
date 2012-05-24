package com.drguildo.munge.gui

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._

import net.miginfocom.swing.MigLayout

import com.drguildo.munge.Settings

object SettingsPanel extends JPanel {
  setLayout(new MigLayout())

  private val browseField = new JTextField()
  browseField.setText(Settings.downloadLocation)

  private val browseButton = new JButton("Browse")
  browseButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      val fileChooser = new JFileChooser()
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)

      val returnVal = fileChooser.showOpenDialog(DownloadPanel)

      if(returnVal == JFileChooser.APPROVE_OPTION) {
        val chosenPath = fileChooser.getSelectedFile.toString

        Settings.setDownloadLocation(chosenPath)
        browseField.setText(chosenPath)
      }
    }
  })

  val downloadPanel = new JPanel()
  downloadPanel.setLayout(new MigLayout())
  downloadPanel.setBorder(BorderFactory.createTitledBorder("Default Download Directory"))

  downloadPanel.add(browseField, "w 100%, span 2")
  downloadPanel.add(browseButton, "wrap")
  downloadPanel.add(new JCheckBox())
  downloadPanel.add(new JLabel("Stuff"))

  val networkPanel = new JPanel()
  networkPanel.setLayout(new MigLayout())
  networkPanel.setBorder(BorderFactory.createTitledBorder("Network"))

  networkPanel.add(new JLabel("Concurrent Downloads"))
  networkPanel.add(new JSpinner())

  add(downloadPanel, "w 100%, wrap")
  add(networkPanel, "w 100%")
}
