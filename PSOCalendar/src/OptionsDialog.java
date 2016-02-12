/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Muhammad Saad Shamim
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import archive.Scheduler;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class OptionsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JButton browseButton;
    private JFileChooser fc;
    private File file;



    public OptionsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        spinner2.setValue(2);
        fc = new JFileChooser();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(onOK())
                    dispose();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        browseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    textField1.setText(file.getAbsolutePath());
                }
            }
        });
    }

    private boolean onOK() {
        if(file == null){
            JOptionPane.showMessageDialog(new JFrame(), "File not selected", "Dialog", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Integer minShifts = (Integer) spinner1.getValue();
        Integer maxShifts = (Integer) spinner2.getValue();

        if(minShifts < 0 || minShifts >= maxShifts){
            JOptionPane.showMessageDialog(new JFrame(), "Invalid shift values selected", "Dialog", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Scheduler.run(file, minShifts, maxShifts);
        return true;
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        OptionsDialog dialog = new OptionsDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
