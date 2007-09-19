  package org.lams.toolbuilder.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.lams.toolbuilder.util.LamsToolBuilderLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.lams.toolbuilder.util.Constants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.lams.toolbuilder.renameTool.RenameTool;
import org.lams.toolbuilder.renameTool.RenameToolTaskList;
import org.lams.toolbuilder.LAMSToolBuilderPlugin;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class LAMSNewToolWizard extends Wizard implements INewWizard {
	private LAMSNewToolWizardPage page;
	private LAMSNewToolWizardTemplatePage page2;
	
	private ISelection selection;

	private boolean workspaceValid;
	
	// The handle to the new LAMS Tool Project to be created
	private IProject projectHandle;
	
	private String toolTemplate;
	private String toolSignature;
	private String vendor;
	private String compatibility;
	private String toolDisplayName;
	private boolean isLAMS;
	private boolean toolVisible;
	
	// The list of base LAMS projects required for the workspace
	private List<String> projectList;
	
	/**
	 * Constructor for LAMSNewToolWizard.
	 */
	public LAMSNewToolWizard() throws Exception{
		super();
		setNeedsProgressMonitor(true);
		
		Constants.initCorePrjects();
		projectList = Constants.coreProjects;
		
		
		workspaceValid = checkWorkspace();
		if (!workspaceValid)
		{
			this.performCancel();
			this.dispose();
		}
		
	}
	
	
	/**
	 * Initiate some local static variblaes
	 */
	public void initiate()
	{
		projectList = new ArrayList();
		projectList.add("lams_admin");
		projectList.add("lams_central");
		projectList.add("lams_common");
		projectList.add("lams_learning");
		projectList.add("lams_monitoring");
		projectList.add("lams_build");
		projectList.add("lams_tool_deploy");
		projectList.add("lams_www");
		
	}
	
	/**
	 * Checks the workspace contains the base LAMS project
	 * @return true if workspace contains the base lams projects
	 */
	public boolean checkWorkspace() 
	{
		boolean result = true;
		List<String> missingList = new ArrayList();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		LamsToolBuilderLog.logInfo("Checking required LAMS projects exist");
		for (String dir : projectList) {
			
			
			
			IPath path = new Path(dir);
			if (!root.exists(path))
			{
				LamsToolBuilderLog.logInfo("Project not found: " + path.toPortableString());
				missingList.add(dir);
				result = false;
			}
			
		}
		
		if (result==false)
		{
			// Print a error dialog informing that the workspace is missing projects
			String message= "You are missing the following required LAMS projects for your workspace:\n ";
			String statusMessage = "Missing LAMS projects in workspace.";
			String title = "LAMS Project Creation Error";
			for (String dir : missingList)
				message += "\t" + dir + "\n";
			
			message += "\nYou can get the required projects from the anonymous CVS account." +
					"\n\t* access method: pserver" +
					"\n\t* user name: anonymous" +
					"\n\t* server name: lamscvs.melcoe.mq.edu.au" +
					"\n\t* location: /usr/local/cvsroot";

			IStatus error = new Status(
					IStatus.ERROR, 
					LAMSToolBuilderPlugin.PLUGIN_ID, 
					IStatus.ERROR, 
					statusMessage, 
					null);
			ErrorDialog.openError(this.getShell(), title, message, error);
			
			
			
			
		}
		
		return result;
	}
	
	
	
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		if(workspaceValid)
		{
			LamsToolBuilderLog.logInfo("Adding pages to LAMS Tool Wizard");
			page = new LAMSNewToolWizardPage(selection);
			addPage(page);
			
			page2 = new LAMSNewToolWizardTemplatePage(selection);
			addPage(page2);
		}	
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		LamsToolBuilderLog.logInfo("Attempting to perform finish for LAMS Tool Wizard");
		
		// Get the project from the page
		final IProject project = page.getProjectHandle();
		
		//private static String toolName;
		vendor = page.getVendor();
		compatibility = page.getCompatibility();
		toolDisplayName = page.getToolDisplayName();
		isLAMS = page.getIsLams();
		toolVisible = page.getVisible();
		toolSignature = page.getToolSignature();
		
		// create a project descriptor
		IPath projPath = null;
		if (!page.useDefaults()) 
		{
			projPath = page.getLocationPath();
			if (projPath.toFile().exists()) {
				// add the project key to the path if this folder exists
				projPath = projPath.addTrailingSeparator().append("Sample_Project").addTrailingSeparator();
				System.out.println("Changed project location to: " + projPath);
			}
			
		}

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace.newProjectDescription(project.getName());
		description.setLocation(projPath);
		
		
		//Operation to create a new LAMS Tool Project
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				monitor.beginTask("Create LAMS Tool project", 2000);
				createLamsToolProject(project, description, 
						new SubProgressMonitor(monitor, 1000));
				monitor.done();
			}
		};
		
		// Run the project operations for creating the LAMS Tool Project
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) 
		{
			LamsToolBuilderLog.logInfo("Project creation cancelled by user");
			return false;
		} catch (InvocationTargetException e) {
			// ie.- one of the steps resulted in a core exception
			Throwable t = e.getTargetException();
			LamsToolBuilderLog.logError(t);
			if (t instanceof CoreException) {
				if (((CoreException) t).getStatus().getCode() ==
							IResourceStatus.CASE_VARIANT_EXISTS) {
					MessageDialog.openError(getShell(),
								"New Project Error:",
								NLS.bind("Case Variant Exists:", project.getName()));
				} else {
					ErrorDialog.openError(getShell(),
							"New Project Error:",
							null, // no special message
							((CoreException) t).getStatus());
				}
			} else {
				Throwable realException = e.getTargetException();
				MessageDialog.openError(getShell(), "Error", realException.getMessage());
				return false;
			}
			return false;
		}
		
		this.projectHandle = project;
		return true;
	}
	
	private void createLamsToolProject (IProject projHandle, IProjectDescription description,IProgressMonitor monitor) 
	throws CoreException, OperationCanceledException 
	{
		
		monitor.beginTask("Creating LAMS tool project", 50);
		
		
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		
		//############### test project to template from 
		toolTemplate = page2.getTemplate();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projTemplate = (IProject)root.findMember(new Path(toolTemplate));
		if (!projTemplate.exists() || !(projTemplate instanceof IContainer)) 
		{
			throwCoreException("Project \"" + toolTemplate + "\" does not exist.");
		}
		monitor.worked(2);
		
		try{
			projTemplate.copy(description, IResource.FORCE | IResource.SHALLOW, new SubProgressMonitor(monitor, 50));
		}
		catch (CoreException e)
		{
			LamsToolBuilderLog.logError(e);
		}
		
		//TODO: 
		
		RenameToolTaskList taskList = new RenameToolTaskList(Constants.FORUM_TOOL_DIR, toolSignature, toolDisplayName );
		RenameTool rt = new RenameTool();
		LamsToolBuilderLog.logInfo(projHandle.getLocation().toPortableString());
		try{
			rt.renameTool(taskList.getTasklist(), projHandle.getLocation().toPortableString());
			System.out.print(taskList.getTasklist().toString());
		}
		catch (Exception e)
		{
			LamsToolBuilderLog.logError("LAMS Tool Renaming Error: ", e);

		}
		
		root.refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, monitor);
		projHandle.open(monitor);
		monitor.worked(2);
		
		
		projHandle.open(monitor);
		monitor.done();
		
		
	}
	
	
	
	
	// Get method for the project created by this wizard
	public IProject getProjectHandle()
	{
		return this.projectHandle;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	/*private void doFinish(
		String toolName,
		String toolDisplayName,
		String vendor,
		String compatibility,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask("Creating build.properties", 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		Properties buildProperties = new Properties();
		buildProperties.setProperty(Constants.PROP_TOOL_NAME, toolName);
		buildProperties.setProperty(Constants.PROP_SIGNATURE, toolDisplayName);
		buildProperties.setProperty(Constants.PROP_PACKAGE, "org.lams.testtool");
		buildProperties.setProperty(Constants.PROP_PACKAGE_PATH, "org/lams/testtool");
		buildProperties.setProperty(Constants.PROP_TOOL_VERSION, "20070000");
		buildProperties.setProperty(Constants.PROP_SERVER_VERSION, compatibility);
		buildProperties.setProperty(Constants.PROP_HIDE_TOOL, "false");
		
		
		
		
		
		
		
		/*IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		
		monitor.worked(1);
	}*/
	
	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents =
			"This is the initial file contents for *.mpe file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.lams.toolbuilder", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}