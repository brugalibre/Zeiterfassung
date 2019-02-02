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
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * @author Dominic
 * 
 */
public class InputMask extends JPanel {

    private static final long serialVersionUID = 1L;

    private BusinessDayIncremental inc;
    private MainWindow mainWindow;
    private Dimension dimension;

    private JButton okButton;
    private JButton abbortButton;

    private JTextField bisField;
    private JTextField vonField;
    private JTextField amountOfHoursField;
    private ComboBox ticketNumberField;
    private ComboBox descriptionField;
    private JComboBox<String> chargeTypeSelectList;

    private JLabel vonLabel;
    private JLabel bisLabel;
    private JLabel amountOfHoursLabel;
    private JLabel ticketNumberLabel;
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

    private void placeContent() {
	int minComponentLength = 0;
	int preferredComponentLength = 100;
	int maxComponentLength = 120;

	GroupLayout groupLayout = new GroupLayout(this);
	groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
		.addGroup(groupLayout.createParallelGroup().addComponent(ticketNumberLabel)
			.addComponent(descriptionLabel).addComponent(vonLabel).addComponent(bisLabel)
			.addComponent(amountOfHoursLabel).addComponent(chargeTypeLabel).addComponent(okButton))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
			.addComponent(ticketNumberField, minComponentLength, preferredComponentLength,
				maxComponentLength)
			.addComponent(descriptionField, minComponentLength, preferredComponentLength,
				maxComponentLength)
			.addComponent(vonField, minComponentLength, preferredComponentLength, maxComponentLength)
			.addComponent(bisField, minComponentLength, preferredComponentLength, maxComponentLength)
			.addComponent(amountOfHoursField, minComponentLength, preferredComponentLength,
				maxComponentLength)
			.addComponent(chargeTypeSelectList).addComponent(abbortButton)

	));
	groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()

		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			.addComponent(ticketNumberField).addComponent(ticketNumberLabel))
		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(descriptionLabel)
			.addComponent(descriptionField))
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
	ticketNumberLabel = new JLabel(TextLabel.TICKET_NUMBER_LABEL);
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
	ticketNumberField = new ComboBox(5);
	ticketNumberField.setEditable(true);
	ticketNumberField.setInputVerifier(inputVerifier);
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
    }

    /**
    * 
    */
    protected void submit() {
	if (isInputValid()) {
	    inputVerifier.handleValidInput(amountOfHoursField);
	    inc.setDescription(descriptionField.getSelectedItem());
	    inc.setTicketNumber(ticketNumberField.getSelectedItem());
	    inc.setChargeType((String) chargeTypeSelectList.getSelectedItem());
	    addSubmittedTicketText();
	    dispose(true);
	}
    }

    /*
     * Actually this should happen as soo as the component is about to lose its
     * focuse. But somehow the verifier is not triggered
     */
    private boolean isInputValid() {
	return inputVerifier.verify(ticketNumberField);
    }

    /**
     * @param description
     */
    private void addSubmittedTicketText() {
	// if the maximum is reached, remove the one which was unused the most
	if (StringUtil.isNotEmptyOrNull(inc.getDescription())) {
	    descriptionField.addNewItem(inc.getDescription());
	}
	ticketNumberField.addNewItem(inc.getTicketNumber());
    }

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
