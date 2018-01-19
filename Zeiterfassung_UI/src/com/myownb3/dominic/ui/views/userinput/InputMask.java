/**
 * 
 */
package com.myownb3.dominic.ui.views.userinput;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.ui.app.MainWindow;
import com.myownb3.dominic.ui.views.userinput.entry.Entry;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * @author Dominic
 * 
 */
public class InputMask extends JPanel {
    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private BusinessDayIncremental inc;
    private MainWindow mainWindow;
    private Dimension dimension;

    private JButton okButton;
    private JButton abbortButton;

    private JTextField bisField;
    private JTextField vonField;
    private JTextField amountOfHoursField;
    private JTextField projectNumberField;
    private ComboBox descriptionField;
    private JComboBox<String> chargeTypeSelectList;

    private JLabel vonLabel;
    private JLabel bisLabel;
    private JLabel amountOfHoursLabel;
    private JLabel projectNumberLabel;
    private JLabel chargeTypeLabel;
    private JLabel descriptionLabel;

    private InputFieldVerifier inputVerifier;

    public InputMask(MainWindow mainWindow) {
	super();
	inputVerifier = new InputFieldVerifier(this);
	this.mainWindow = mainWindow;
	createContent();
	placeContent();
	ActionListener actionListener = (e) -> {
	    if (e.getSource() == okButton) {
		submit();
	    } else if (e.getSource() == abbortButton) {
		dispose(false);
	    }
	};
	okButton.addActionListener(actionListener);
	okButton.addKeyListener(new KeyListener() {
	    @Override
	    public void keyTyped(KeyEvent arg0) {
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    submit();
		}
	    }

	    @Override
	    public void keyPressed(KeyEvent arg0) {
	    }
	});
	abbortButton.addActionListener(actionListener);

	setDimension(new Dimension(320, 260));
	addKeyListener(mainWindow);
    }

    /**
    * 
    */
    private void placeContent() {
	int minComponentLength = 0;
	int preferredComponentLength = 100;
	int maxComponentLength = 120;

	GroupLayout groupLayout = new GroupLayout(this);
	groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
		.addGroup(groupLayout.createParallelGroup().addComponent(descriptionLabel)
			.addComponent(projectNumberLabel).addComponent(vonLabel).addComponent(bisLabel)
			.addComponent(amountOfHoursLabel).addComponent(chargeTypeLabel).addComponent(okButton))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
			.addComponent(descriptionField, minComponentLength, preferredComponentLength,
				maxComponentLength)
			.addComponent(projectNumberField, minComponentLength, preferredComponentLength,
				maxComponentLength)
			.addComponent(vonField, minComponentLength, preferredComponentLength, maxComponentLength)
			.addComponent(bisField, minComponentLength, preferredComponentLength, maxComponentLength)
			.addComponent(amountOfHoursField, minComponentLength, preferredComponentLength,
				maxComponentLength)
			.addComponent(chargeTypeSelectList).addComponent(abbortButton)

	));
	groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()

	.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(descriptionLabel)
		.addComponent(descriptionField))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			.addComponent(projectNumberLabel).addComponent(projectNumberField))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(vonLabel)
			.addComponent(vonField))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bisLabel)
			.addComponent(bisField))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			.addComponent(amountOfHoursLabel).addComponent(amountOfHoursField))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			.addComponent(chargeTypeSelectList).addComponent(chargeTypeLabel))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(okButton)
			.addComponent(abbortButton)));
	groupLayout.linkSize(SwingConstants.HORIZONTAL, abbortButton, okButton);
	groupLayout.setAutoCreateGaps(true);
	this.setLayout(groupLayout);
    }

    /**
    * 
    */
    private void createContent() {
	amountOfHoursLabel = new JLabel(TextLabel.AMOUNT_OF_HOURS_LABEL);
	projectNumberLabel = new JLabel(TextLabel.PROJECT_NUMBER_LABEL);
	chargeTypeLabel = new JLabel(TextLabel.CHARGE_TYPE_LABEL);
	descriptionLabel = new JLabel(TextLabel.DESCRIPTION_LABEL);
	vonLabel = new JLabel(TextLabel.VON_LABEL);
	bisLabel = new JLabel(TextLabel.BIS_LABEL);

	amountOfHoursField = new JTextField();
	amountOfHoursField.setInputVerifier(inputVerifier);

	vonField = new JTextField();
	vonField.setEditable(false);
	bisField = new JTextField();
	bisField.setEditable(false);
	projectNumberField = new JTextField();
	projectNumberField.setEnabled(false);
	chargeTypeSelectList = new JComboBox<String>(ChargeType.CHARGE_TYPES);
	descriptionField = new ComboBox(5);
	descriptionField.setEditable(true);
	okButton = new JButton(TextLabel.OK_BUTTON_TEXT);

	abbortButton = new JButton(TextLabel.ABORT_BUTTON_TEXT);
    }

    /**
    */
    protected void changeStateUpTo() {
	float additionallyTime = 0;

	additionallyTime = Float.parseFloat(amountOfHoursField.getText()) - inc.getTotalDuration();

	long additionallyDuration = (long) (Time.getTimeRefactorValue(TimeRecorder.GLOBAL_TIME_TYPE)
		* additionallyTime);
	inc.getCurrentTimeSnippet().setEndTimeStamp(
		new Time(inc.getCurrentTimeSnippet().getEndTimeStamp().getTime() + additionallyDuration));
	initializeFields(inc);
    }

    public void initializeFields(BusinessDayIncremental inc) {
	this.inc = inc;
	amountOfHoursField.setText(String.valueOf(inc.getTotalDuration()));
	bisField.setText(String.valueOf(inc.getCurrentTimeSnippet().getEndTimeStamp()));
	vonField.setText(String.valueOf(inc.getCurrentTimeSnippet().getBeginTimeStamp()));

	if (StringUtil.isValid(inc.getProjectNumber())) {
	    projectNumberField.setText(inc.getProjectNumber());
	}
    }

    /**
    * 
    */
    protected void submit() {
	String value = descriptionField.getSelectedItem();
	if (inputVerifier.verify(amountOfHoursField) && StringUtil.isValid(value)) {
	    inputVerifier.handleValidInput(amountOfHoursField);
	    inc.setDescription(value);
	    inc.setProjectNumber(projectNumberField.getText());
	    inc.setChargeType((String) chargeTypeSelectList.getSelectedItem());
	    addSubmittedTicketText(inc.getDescription());
	    dispose(true);
	}
    }

    /**
     * @param description
     */
    private void addSubmittedTicketText(String descriptionAsString) {
	Entry description = new Entry(descriptionAsString);
	// if the maximum is reached, remove the one which was unused the most
	descriptionField.addNewItem(description);
    }

    /**
    * 
    */
    protected void dispose(boolean success) {
	mainWindow.finishOrAbortAndDispose(success);
    }

    public Dimension getDimension() {
	return dimension;
    }

    public void setDimension(Dimension dimension) {
	this.dimension = dimension;
    }
}
