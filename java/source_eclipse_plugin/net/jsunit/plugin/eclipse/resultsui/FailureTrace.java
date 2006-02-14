package net.jsunit.plugin.eclipse.resultsui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import net.jsunit.model.TestCaseResult;
import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;

public class FailureTrace implements IMenuListener {
    private final Image jsunitIcon= JsUnitPlugin.soleInstance().createImage("jsunitlaunch.gif");
    private final Image stackTraceIcon= JsUnitPlugin.soleInstance().createImage("stkfrm_obj.gif");
    
	private Table table;
	private String inputTrace;

	public FailureTrace(Composite parent, Clipboard clipboard, JsUnitTestResultsViewPart testRunner, ToolBar toolBar) {		
		table= new Table(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		
		initMenu();
		
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				disposeIcons();
			}
		});
	}
	
	private void initMenu() {
		MenuManager menuMgr= new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(this);
		Menu menu= menuMgr.createContextMenu(table);
		table.setMenu(menu);		
	}
	
	public void menuAboutToShow(IMenuManager manager) {
	}

	public String getTrace() {
		return inputTrace;
	}
	
	private void disposeIcons(){
		if (stackTraceIcon != null && !stackTraceIcon.isDisposed()) 
			stackTraceIcon.dispose();
		if (jsunitIcon != null && !jsunitIcon.isDisposed()) 
			jsunitIcon.dispose();
	}
	
	Composite getComposite(){
		return table;
	}
	
	public void refresh() {
		updateTable(inputTrace);
	}
	
	public void showFailure(TestCaseResult failure) {	
	    String trace= "";
	    if (failure != null && !failure.wasSuccessful()) 
	        trace= failure.getProblemSummary();
	    showTrace(trace);
	}
	
	public void showTrace(String trace) {
		if (inputTrace!=null && inputTrace.equals(trace))
			return;
		inputTrace= trace;
		updateTable(trace);		
	}

	private void updateTable(String trace) {
		if(trace == null || trace.trim().equals("")) { //$NON-NLS-1$
			clear();
			return;
		}
		trace= trace.trim();
		table.setRedraw(false);
		table.removeAll();
		fillTable(filterStack(trace));
		table.setRedraw(true);
	}

	private void fillTable(String trace) {
		StringReader stringReader= new StringReader(trace);
		BufferedReader bufferedReader= new BufferedReader(stringReader);
		String line;

		try {	
			// first line contains the thrown exception
			line= bufferedReader.readLine();
			if (line == null)
				return;
				
			TableItem tableItem= new TableItem(table, SWT.NONE);
			tableItem.setImage(jsunitIcon);
			String itemLabel= line.replace('\t', ' ');
			final int labelLength= itemLabel.length();
			final int MAX_LABEL_LENGTH= 256;
			if (labelLength < MAX_LABEL_LENGTH) {
				tableItem.setText(itemLabel);
			} else {
				tableItem.setText(itemLabel.substring(0, MAX_LABEL_LENGTH));
				int offset= MAX_LABEL_LENGTH;
				while (offset < labelLength) {
					int nextOffset= Math.min(labelLength, offset + MAX_LABEL_LENGTH);
					tableItem= new TableItem(table, SWT.NONE);
					tableItem.setText(itemLabel.substring(offset, nextOffset));
					offset= nextOffset;
				}
			}
			
			// the stack frames of the trace
			boolean isStackTraceLine = false;
			while ((line= bufferedReader.readLine()) != null) {
				itemLabel= line.replace('\t', ' ');
				tableItem= new TableItem(table, SWT.NONE);

				if (isStackTraceLine) {
					tableItem.setImage(stackTraceIcon);
				}
				tableItem.setText(itemLabel);
				if ((itemLabel.equalsIgnoreCase("Stack trace follows:"))) {
					isStackTraceLine = true;
				}
			}
		} catch (IOException e) {
			TableItem tableItem= new TableItem(table, SWT.NONE);
			tableItem.setText(trace);
		}			
	}
	
	public void setInformation(String text) {
		clear();
		TableItem tableItem= new TableItem(table, SWT.NONE);
		tableItem.setText(text);
	}
	
	public void clear() {
		table.removeAll();
		inputTrace= null;
	}
	
	private String filterStack(String stackTrace) {
		return stackTrace;
	}
	
    public Shell getShell() {
        return table.getShell();
    }

}